package thd.gameobjects.base;

import java.util.Random;

import thd.gameobjects.unmovable.SceneryRight;
import thd.gameobjects.unmovable.SceneryLeft;

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
     * Checks if the gaming object is allowed to move horizontally.
     *
     * @param gameObject the object that should be checked.
     */
    public void gamingObjectCanMoveHorizontal(GameObject gameObject) {
        if (movingRight) {
            gameObject.getPosition().right(gameObject.speedInPixel);
        }
        if (!movingRight) {
            gameObject.getPosition().left(gameObject.speedInPixel);
        }
    }

    /**
     * If the gaming object hits left or right {@link SceneryLeft}, {@link SceneryRight} the direction changes.
     */
    public void changeDirectionIfObjectHitsBoundary() {
        movingRight = !movingRight;
    }
}
