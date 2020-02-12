package za.co.entelect.challenge.enums;

public enum BuildingType {
    DEFENSE("0"),
    ATTACK("1"),
    ENERGY("2"),
    DECONSTRUCT("3"),
    TESLA("4"),
    CURTAIN("5");

    private final String commandCode;

    BuildingType(String commandCode) {
        this.commandCode = commandCode;
    }

    public String getCommandCode() {
        return commandCode;
    }
}
