package thd.gameobjects.base;

import java.util.Random;

/**
 * Represents a movement pattern for the different game objects.
 */
public class MovementPattern {
    private final Position position;
    protected final Random random;

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

    /**
     * Moves the game object horizontally based on the movement direction.
     *
     * @param gameObject the object that to be moved horizontally.
     */
    public void moveHorizontally(GameObject gameObject) {
        if (movingRight) {
            gameObject.getPosition().right(gameObject.speedInPixel);
        }
        if (!movingRight) {
            gameObject.getPosition().left(gameObject.speedInPixel);
        }
    }

    /**
     * If the gaming object hits left or right boundary, the direction changes.
     *
     * @param gameObject the object that is checked for collisions.
     */
    public void changeDirectionIfObjectHitsBoundary(GameObject gameObject) {
        if (gameObject.gameObjectHitsRightBoundary()) {
            movingRight = false;
        } else if (gameObject.gameObjectHitsLeftBoundary()) {
            movingRight = true;
        }
    }

}
