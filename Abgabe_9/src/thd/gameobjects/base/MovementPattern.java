package thd.gameobjects.base;

import java.util.Random;

import thd.game.utilities.GameView;
import thd.gameobjects.unmovable.SceneryRight;
import thd.gameobjects.unmovable.SceneryLeft;

/**
 * Represents a movement pattern for the different game objects.
 */
public class MovementPattern {
    private final Position position;
    protected final Random random;
    private final VerticalMovementPattern verticalMovementPattern;

    /**
     * Movement direction (true=right, false=left).
     */
    public boolean movingRight;

    protected MovementPattern() {
        position = new Position(0, 0);
        random = new Random();
        verticalMovementPattern = new VerticalMovementPattern();
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
        double yCoordinate = gameObject.getPosition().getY();
        if (movingRight && yCoordinate >= -100 && yCoordinate <= GameView.HEIGHT + 100) {
            gameObject.getPosition().right(gameObject.speedInPixel);
        }
        if (!movingRight && yCoordinate >= -100 && yCoordinate <= GameView.HEIGHT + 100) {
            gameObject.getPosition().left(gameObject.speedInPixel);
        }
    }

    /**
     * Moves the given game object vertically based on a predefined movement pattern.
     *
     * @param gameObject the game object to be moved vertically
     */
    public void gameObjectMovesVertical(GameObject gameObject) {
        for (Position moves : verticalMovementPattern.loadPattern("down")) {
            gameObject.getPosition().down(moves.getY() * gameObject.speedInPixel);
        }
    }

    /**
     * If the gaming object hits left or right {@link SceneryLeft}, {@link SceneryRight} the direction changes.
     */
    public void changeDirectionIfObjectHitsBoundary() {
        movingRight = !movingRight;
    }
}
