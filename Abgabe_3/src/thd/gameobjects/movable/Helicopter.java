package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * Represents an enemy helicopter in the {@link GameView} window.
 * The helicopter is a movable object that has different properties,
 * such as speed and size. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

class Helicopter extends GameObject {
    private final HelicopterMovementPattern helicopterMovementPattern;

    /**
     * Creates a new helicopter object with random position, speed, size and other properties.
     *
     * @param gameView The gaming window where the helicopter will be displayed.
     */

    private Helicopter(GameView gameView) {
        super(gameView);
        helicopterMovementPattern = new HelicopterMovementPattern();
        position.updateCoordinates(helicopterMovementPattern.startPosition());
        speedInPixel = 1;
        size = 0;
        rotation = 0;
        width = 0;
        height = 0;
    }

    @Override
    public String toString() {
        return "Helicopter: " + position;
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    @Override
    public void updatePosition() {
        position.down(speedInPixel);
        if (helicopterMovementPattern.movingRight) {
            position.right(speedInPixel + 1);
        }
        if (!helicopterMovementPattern.movingRight) {
            position.left(speedInPixel + 1);
        }

        if (gameObjectHitsRightBoundary()) {
            helicopterMovementPattern.movingRight = false;
        } else if (gameObjectHitsLeftBoundary()) {
            helicopterMovementPattern.movingRight = true;
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
        gameView.addImageToCanvas("helicopter.png", position.getX(), position.getY(), 0.80, 0);

    }


}
