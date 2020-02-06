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

    // Bot constructor
    public Bot(GameState gameState) {
        this.gameState = gameState;
        gameState.getGameMap();
    }

    // Generate command for the bot to execute
    public String run() {
        String command = "";

//        //If the enemy has an attack building and I don't have a blocking wall, then block from the front.
//        for (int i = 0; i < gameState.gameDetails.mapHeight; i++) {
//            int enemyAttackOnRow = getAllBuildingsForPlayer(PlayerType.B, b -> b.buildingType == BuildingType.ATTACK, i).size();
//            int myDefenseOnRow = getAllBuildingsForPlayer(PlayerType.A, b -> b.buildingType == BuildingType.DEFENSE, i).size();
//
//            if (enemyAttackOnRow > 0 && myDefenseOnRow == 0) {
//                if (canAffordBuilding(BuildingType.DEFENSE))
//                    command = placeBuildingInRowFromFront(BuildingType.DEFENSE, i);
//                else
//                    command = "";
//                break;
//            }
//        }
//
//        //If there is a row where I don't have energy and there is no enemy attack building, then build energy in the back row.
//        if (command.equals("")) {
//            for (int i = 0; i < gameState.gameDetails.mapHeight; i++) {
//                int enemyAttackOnRow = getAllBuildingsForPlayer(PlayerType.B, b -> b.buildingType == BuildingType.ATTACK, i).size();
//                int myEnergyOnRow = getAllBuildingsForPlayer(PlayerType.A, b -> b.buildingType == BuildingType.ENERGY, i).size();
//
//                if (enemyAttackOnRow == 0 && myEnergyOnRow == 0) {
//                    if (canAffordBuilding(BuildingType.ENERGY))
//                        command = placeBuildingInRowFromBack(BuildingType.ENERGY, i);
//                    break;
//                }
//            }
//        }
//
//        //If I have a defense building on a row, then build an attack building behind it.
//        if (command.equals("")) {
//            for (int i = 0; i < gameState.gameDetails.mapHeight; i++) {
//                if (getAllBuildingsForPlayer(PlayerType.A, b -> b.buildingType == BuildingType.DEFENSE, i).size() > 0
//                        && canAffordBuilding(BuildingType.ATTACK)) {
//                    command = placeBuildingInRowFromFront(BuildingType.ATTACK, i);
//                }
//            }
//        }
//
//        //If I don't need to do anything then either attack or defend randomly based on chance (65% attack, 35% defense).
//        if (command.equals("")) {
//            if (getEnergy(PlayerType.A) >= getMostExpensiveBuildingPrice()) {
//                if ((new Random()).nextInt(100) <= 35) {
//                    return placeBuildingRandomlyFromFront(BuildingType.DEFENSE);
//                } else {
//                    return placeBuildingRandomlyFromBack(BuildingType.ATTACK);
//                }
//            }
//        }
        // If bot health above 50%...
        // if(this.getHealth(PlayerType.A)>=50)
        // {
        //     if(command == "" && )
        // }
        // else
        // {

        // }
        // if(command == "" && this.getEnergy(PlayerType.A)<50)
        // {
        //     command = this.placeBuildingRandomlyFromBack(BuildingType.ENERGY);
        //     this.getEnergyBuilding(PlayerType.A);
        // }
        // if (command.equals("")) {
        //     for (int i = 0; i < gameState.gameDetails.mapHeight; i++) {
        //         if (getAllBuildingsForPlayer(PlayerType.A, b -> b.buildingType == BuildingType.DEFENSE, i).size() > 0
        //                 && canAffordBuilding(BuildingType.ATTACK)) {
        //             command = placeBuildingInRowFromFront(BuildingType.ATTACK, i);
        //         }
        //     }
        command = this.placeBuildingRandomlyFromBack(BuildingType.ENERGY);
        
        System.out.println(this.getEnergyBuilding(PlayerType.A));

        return command;
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

    /**
     * Get all buildings for player in row y
     *
     * @param playerType the player type
     * @param filter     the filter
     * @param y          the y
     * @return the result
     **/
    private List<Building> getAllBuildingsForPlayer(PlayerType playerType, Predicate<Building> filter) {
        return gameState.getGameMap().stream()
                .filter(c -> c.cellOwner == playerType)
                .flatMap(c -> c.getBuildings().stream())
                .filter(filter)
                .collect(Collectors.toList());
    }
    

    /**
     * Get all empty cells for column x
     *
     * @param x the x
     * @return the result
     **/
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

    /**
     * Checks if building can be afforded
     *
     * @param buildingType the building type
     * @return the result
     **/
    private boolean canAffordBuilding(BuildingType buildingType) {
        return getEnergy(PlayerType.A) >= getPriceForBuilding(buildingType);
    }

    /**
     * Gets energy for player type
     *
     * @param playerType the player type
     * @return the result
     **/
    private int getEnergy(PlayerType playerType) {
        return gameState.getPlayers().stream()
                .filter(p -> p.playerType == playerType)
                .mapToInt(p -> p.energy)
                .sum();
    }

    private int getHealth(PlayerType playerType)
    {
        return this.gameState.getPlayers().stream()
                .filter(p -> p.playerType == playerType)
                .mapToInt(p -> p.health)
                .sum();
    }

    private int getEnergyBuilding(PlayerType playerType)
    {
        int result = this.getAllBuildingsForPlayer(playerType, x->x.buildingType==BuildingType.ENERGY).size();
        return result;
    }

    /**
     * Gets price for building type
     *
     * @param buildingType the player type
     * @return the result
     **/
    private int getPriceForBuilding(BuildingType buildingType) {
        return gameState.gameDetails.buildingsStats.get(buildingType).price;
    }

    /**
     * Gets price for most expensive building type
     *
     * @return the result
     **/
    private int getMostExpensiveBuildingPrice() {
        return gameState.gameDetails.buildingsStats
                .values().stream()
                .mapToInt(b -> b.price)
                .max()
                .orElse(0);
    }
}
