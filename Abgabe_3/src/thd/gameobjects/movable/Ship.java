package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * Represents an enemy ship in the {@link GameView} window.
 * The ship is a movable object that has different properties,
 * such as speed and size. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class Ship extends GameObject {
    private final ShipMovementPattern shipMovementPattern;

    /**
     * Creates a new ship object with default position, speed, size and other properties.
     *
     * @param gameView The gaming window where the ship will be displayed.
     */
    public Ship(GameView gameView) {
        super(gameView);
        shipMovementPattern = new ShipMovementPattern();
        position.updateCoordinates(shipMovementPattern.startPosition());
        speedInPixel = 1;
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
    @Override
    public void updatePosition() {
        if (gameObjectHitsLowerBoundary()) {
            speedInPixel = 0;
        }
        position.down(speedInPixel);
        if (shipMovementPattern.movingRight) {
            position.right(speedInPixel);
        }
        if (!shipMovementPattern.movingRight) {
            position.left(speedInPixel);
        }

        if (gameObjectHitsRightBoundary()) {
            shipMovementPattern.movingRight = false;
        } else if (gameObjectHitsLeftBoundary()) {
            shipMovementPattern.movingRight = true;
        }
    }

    /**
     * Adds the gaming object to the game canvas in {@link GameView}
     * by placing an image or shape (oval, rectangle, etc.) at the respective position.
     *
     * @see GameView
     * @see Position
     */
    @Override
    public void addToCanvas() {
        if (shipMovementPattern.movingRight) {
            gameView.addImageToCanvas("ship.png", position.getX(), position.getY(), 0.80, 0);
        } else {
            gameView.addImageToCanvas("ship_left.png", position.getX(), position.getY(), 0.80, 0);
        }

    }
}
