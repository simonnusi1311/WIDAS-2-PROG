package thd.game.level;

/**
 * Exception thrown when there are no more levels available in the game.
 */
public class NoMoreLevelsAvailableException extends RuntimeException {

    /**
     * Creates a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public NoMoreLevelsAvailableException(String message) {
        super(message);
    }
}
