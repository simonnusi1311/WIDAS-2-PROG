package thd.gameobjects.base;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;

import java.util.Objects;

/**
 * Represents an object in the game.
 */
public abstract class GameObject {

    protected final GameView gameView;
    protected final Position position;
    protected final Position targetPosition;
    protected double speedInPixel;
    protected double rotation;
    protected double size;
    protected double width;
    protected double height;
    protected final GamePlayManager gamePlayManager;


    /**
     * Crates a new GameObject.
     *
     * @param gameView        GameView to show the game object on.
     * @param gamePlayManager Managed the Game Play from River Raid.
     */
    public GameObject(GameView gameView, GamePlayManager gamePlayManager) {
        this.gameView = gameView;
        this.gamePlayManager = gamePlayManager;
        position = new Position();
        targetPosition = new Position();
    }


    /**
     * Change the condition of a gaming object.
     */
    public void updateStatus() {

    }

    /**
     * Updates the position of the game object.
     */
    public void updatePosition() {
    }

    /**
     * Draws the game object to the canvas.
     */
    public abstract void addToCanvas();

    /**
     * Returns the current position of the game object.
     *
     * @return position of the game object.
     */
    public Position getPosition() {
        return position;
    }

    protected boolean gameObjectHitsLowerBoundary() {
        return position.getY() >= GameView.HEIGHT;
    }

    protected boolean gameObjectHitsLeftBoundary() {
        return false;
    }

    protected boolean gameObjectHitsRightBoundary() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameObject other = (GameObject) o;
        return Objects.equals(position, other.position)
                && Objects.equals(targetPosition, other.targetPosition)
                && Double.compare(speedInPixel, other.speedInPixel) == 0
                && Double.compare(rotation, other.rotation) == 0
                && Double.compare(size, other.size) == 0
                && Double.compare(width, other.width) == 0
                && Double.compare(height, other.height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, targetPosition, speedInPixel, rotation, size, width, height);
    }

}