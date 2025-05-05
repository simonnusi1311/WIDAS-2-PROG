package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.Position;

/**
 * Represents an enemy jet in grey in the {@link GameView} window.
 * The jet is a movable object. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class GreyJet {
    private final GameView gameView;
    private final Position position;
    private final double speedInPixel;
    private final double size;
    private double rotation;
    private final double width;
    private final double height;

    /**
     * Creates a new GreyJet object with default position, speed, size,
     * rotation and other properties.
     *
     * @param gameView The gaming window where the ship will be displayed.
     */

    public GreyJet(GameView gameView) {
        this.gameView = gameView;
        position = new Position(0, GameView.HEIGHT / 2.0);
        speedInPixel = 5;
        size = 30;
        rotation = 0;
        width = 0;
        height = 0;
    }

    @Override
    public String toString() {
        return "GreyJet: " + position;
    }


    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */

    public void updatePosition() {
        position.right(speedInPixel);
        rotation++;
    }

    /**
     * Adds the gaming object to the game canvas in {@link GameView}
     * by placing an image or shape (oval, rectangle, etc.) at the respective position.
     *
     * @see GameView
     * @see Position
     */
    public void addToCanvas() {
        gameView.addImageToCanvas("grey_jet.png", position.getX(), position.getY(), 0.85, rotation);
    }
}
