package za.co.entelect.challenge;

import za.co.entelect.challenge.entities.Building;
import za.co.entelect.challenge.entities.CellStateContainer;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.Player;
import za.co.entelect.challenge.enums.BuildingType;
import za.co.entelect.challenge.enums.PlayerType;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Bot {

    private GameState gameState;

    // Bot ctor
    public Bot(GameState gameState)
    {
        
        // Read current game state
        this.gameState = gameState;

        // Get game map of the gamestate
        this.gameState.getGameMap();
    }

    // Generate command for the bot to execute
    public String run() {
        String command = "";
        
        command = this.placeBuildingRandomlyFromBack(BuildingType.ENERGY);        
        System.out.println(this.getEnergyBuildingCount(PlayerType.A));

        return command;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-= GAME DETAILS GETTER -=-=-=-=-=-=-=-=-=-=-=-= */
    // Private methods for getting game details, such as health, energy, etc.
    
    // Get playing area width
    private int mapWidth()
    {
        return this.gameState.gameDetails.mapWidth/2;
    }
    
    // Get playing area height
    private int mapHeight()
    {
        return this.gameState.gameDetails.mapHeight;
    }
    
    // Get player health of playerType
    private int getPlayerHealth(PlayerType playerType)
    {
        return this.gameState.getPlayers().stream()
        .filter(x -> x.playerType == playerType)
        .mapToInt(x -> x.health)
        .sum();
    }

    // Get player energy of playerType
    private int getPlayerEnergy(PlayerType playerType)
    {
        return this.gameState.getPlayers().stream()
        .filter(x -> x.playerType == playerType)
        .mapToInt(x -> x.energy)
        .sum();
    }

    // Get current energy income of playerType
    private int getEnergyIncome(PlayerType playerType) 
    {
        return this.gameState.gameDetails.roundIncomeEnergy + this.getEnergyBuildingCount(playerType)*3;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-= BUILDINGS GETTER -=-=-=-=-=-=-=-=-=-=-=-= */
    // Private methods for getting game details, such as health, energy, etc.
    
    // Get building price
    private int getBuildingPrice(BuildingType buildingType) {
        return gameState.gameDetails.buildingsStats.get(buildingType).price;
    }
    
    // Get energy building count of playerType
    private int getEnergyBuildingCount(PlayerType playerType)
    {
        return this.getAllBuildingsForPlayer(
            playerType, 
            x->x.buildingType==BuildingType.ENERGY && x.constructionTimeLeft<=0
        )
        .size();
    }

    // Get attack building count of playerType
    private int getAttackBuildingCount(PlayerType playerType)
    {
        return this.getAllBuildingsForPlayer(
            playerType, 
            x->x.buildingType==BuildingType.ATTACK && x.constructionTimeLeft<=0
        )
        .size();
    }
    
    // Get defense building count of playerType
    private int getDefenseBuildingCount(PlayerType playerType)
    {
        return this.getAllBuildingsForPlayer(
            playerType, 
            x->x.buildingType==BuildingType.DEFENSE && x.constructionTimeLeft<=0
            )
            .size();
    }
        
    // Get defense building count of playerType
    private int getTeslaBuildingCount(PlayerType playerType)
    {
        return this.getAllBuildingsForPlayer(
            playerType, 
            x->x.buildingType==BuildingType.TESLA && x.constructionTimeLeft<=0
        )
        .size();
    }
        
        
            // Placing building randomly in the back
            private String placeBuildingRandomlyFromBack(BuildingType buildingType) {
        for (int i = 0; i < gameState.gameDetails.mapWidth / 2; i++) {
            List<CellStateContainer> listOfFreeCells = getListOfEmptyCellsForColumn(i);
            if (!listOfFreeCells.isEmpty()) {
                CellStateContainer pickedCell = listOfFreeCells.get((new Random()).nextInt(listOfFreeCells.size()));
                return buildCommand(pickedCell.x, pickedCell.y, buildingType);
            }
        }
        return "";
    }

    // Placing building randomly in the front
    private String placeBuildingRandomlyFromFront(BuildingType buildingType) {
        for (int i = (gameState.gameDetails.mapWidth / 2) - 1; i >= 0; i--) {
            List<CellStateContainer> listOfFreeCells = getListOfEmptyCellsForColumn(i);
            if (!listOfFreeCells.isEmpty()) {
                CellStateContainer pickedCell = listOfFreeCells.get((new Random()).nextInt(listOfFreeCells.size()));
                return buildCommand(pickedCell.x, pickedCell.y, buildingType);
            }
        }
        return "";
    }

    // Placing building in a row from front line
    private String placeBuildingInRowFromFront(BuildingType buildingType, int y) {
        for (int i = (gameState.gameDetails.mapWidth / 2) - 1; i >= 0; i--) {
            if (isCellEmpty(i, y)) {
                return buildCommand(i, y, buildingType);
            }
        }
        return "";
    }

    // Placing building in a row from back line
    private String placeBuildingInRowFromBack(BuildingType buildingType, int y) {
        for (int i = 0; i < gameState.gameDetails.mapWidth / 2; i++) {
            if (isCellEmpty(i, y)) {
                return buildCommand(i, y, buildingType);
            }
        }
        return "";
    }

    // Build command construction
    private String buildCommand(int x, int y, BuildingType buildingType) {
        return String.format("%s,%d,%s", String.valueOf(x), y, buildingType.getCommandCode());
    }

    private List<Building> getAllBuildingsForPlayer(PlayerType playerType, Predicate<Building> filter) {
        return gameState.getGameMap().stream()
                .filter(c -> c.cellOwner == playerType)
                .flatMap(c -> c.getBuildings().stream())
                .filter(filter)
                .collect(Collectors.toList());
    }
    
    private List<CellStateContainer> getListOfEmptyCellsForColumn(int x) {
        return gameState.getGameMap().stream()
                .filter(c -> c.x == x && isCellEmpty(x, c.y))
                .collect(Collectors.toList());
    }

    /**
     * Checks if cell at x,y is empty
     *
     * @param x the x
     * @param y the y
     * @return the result
     **/
    private boolean isCellEmpty(int x, int y) {
        Optional<CellStateContainer> cellOptional = gameState.getGameMap().stream()
                .filter(c -> c.x == x && c.y == y)
                .findFirst();

        if (cellOptional.isPresent()) {
            CellStateContainer cell = cellOptional.get();
            return cell.getBuildings().size() <= 0;
        } else {
            System.out.println("Invalid cell selected");
        }
        return true;
    }


}
