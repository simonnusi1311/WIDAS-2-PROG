package thd.gameobjects.base;

import thd.game.utilities.GameView;

/**
 * Represents an object in the game.
 */
public class GameObject {

    protected final GameView gameView;
    protected final Position position;
    protected final Position targetPosition;
    protected double speedInPixel;
    protected double rotation;
    protected double size;
    protected double width;
    protected double height;
    protected int randomNumberForTheSpawn;

    /**
     * Crates a new GameObject.
     *
     * @param gameView GameView to show the game object on.
     */
    public GameObject(GameView gameView) {
        this.gameView = gameView;
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
    public void addToCanvas() {
    }

    /**
     * Returns the current position of the game object.
     *
     * @return position of the game object.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns width of game object.
     *
     * @return Width of game object
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns height of game object.
     *
     * @return Height of game object
     */
    public double getHeight() {
        return height;
    }

    protected boolean gameObjectHitsLeftBoundary() {
        return false;
    }

    protected boolean gameObjectHitsRightBoundary() {
        return false;
    }

    protected boolean readyToSpawn() {
        return false;
    }

    protected boolean gameObjectHitsLowerBoundary() {
        return position.getY() >= GameView.HEIGHT - 75;
    }

}