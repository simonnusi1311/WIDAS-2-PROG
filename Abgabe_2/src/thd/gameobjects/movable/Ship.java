package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.Position;

/**
 * Represents an enemy ship in the {@link GameView} window.
 * The ship is a movable object. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class Ship {
    private final GameView gameView;
    private final Position position;
    private final double speedInPixel;
    private final double size;
    private final double rotation;
    private final double width;
    private final double height;


    /**
     * Creates a new Ship object with default position, speed, size and other properties.
     *
     * @param gameView The gaming window where the ship will be displayed.
     */
    public Ship(GameView gameView) {
        this.gameView = gameView;
        position = new Position(1100, 650);
        speedInPixel = 2;
        size = 30;
        rotation = 0;
        width = 150;
        height = 33;
    }

    @Override
    public String toString() {
        return "Ship: " + position;
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    public void updatePosition() {
        position.left(speedInPixel);
    }

    /**
     * Adds the gaming object to the game canvas in {@link GameView}
     * by placing an image or shape (oval, rectangle, etc.) at the respective position.
     *
     * @see GameView
     * @see Position
     */
    public void addToCanvas() {
        gameView.addImageToCanvas("ship.png", position.getX(), position.getY(), 0.85, 0);
    }


}
