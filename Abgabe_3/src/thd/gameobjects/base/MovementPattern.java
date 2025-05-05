package thd.gameobjects.base;

import java.util.Random;

/**
 * Represents a movement pattern for the different game objects.
 */
public class MovementPattern {
    private Position position;
    protected final Random random;
    /**
     * If true, enables horizontal movement.
     */
    public boolean shouldMove;
    /**
     * Movement direction (true=right, false=left).
     */
    public boolean movingRight;

    protected MovementPattern() {
        position = new Position(0, 0);
        random = new Random();
    }

    protected Position nextPosition() {
        return position;
    }

    protected Position startPosition() {
        return position;
    }
}
