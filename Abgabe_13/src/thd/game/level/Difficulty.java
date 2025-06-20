package thd.game.level;

/**
 * Enum to handle the Level difficulty.
 */
public enum Difficulty {

    /**
     * Easy game mode with lower difficulty.
     */
    EASY("Easy"),

    /**
     * Standard game mode with normal difficulty.
     */
    STANDARD("Standard");

    /**
     * The Name representation of the respective Difficulty.
     */
    public String name;

    Difficulty(String name) {
        this.name = name;
    }

    /**
     * This method returns the Difficulty Enum to a String name representation.
     *
     * @param name the Name of the Difficulty in a String.
     * @return the respective Difficulty.
     */
    public static Difficulty fromName(String name) {
        switch (name) {
            case "Standard" -> {
                return Difficulty.STANDARD;
            }
            default -> {
                return Difficulty.EASY;
            }
        }
    }
}
