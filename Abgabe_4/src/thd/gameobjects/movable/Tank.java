package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * Represents an enemy tank in the {@link GameView} window.
 * The tank is a movable object that has different properties,
 * such as speed and size. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class Tank extends GameObject {
    private final TankMovementPattern tankMovementPattern;

    /**
     * Creates a new ship object with default position, speed, size and other properties.
     *
     * @param gameView The gaming window where the ship will be displayed.
     */
    public Tank(GameView gameView) {
        super(gameView);
        tankMovementPattern = new TankMovementPattern();
        position.updateCoordinates(tankMovementPattern.startPosition());
        speedInPixel = 1;
        size = 0.80;
        rotation = 0;
        width = 150;
        height = 33;
        randomNumberForTheSpawn = (int) (Math.random() * 7000) + 1000;
    }

    @Override
    public String toString() {
        return "Tank: " + position;
    }

    @Override
    protected boolean gameObjectHitsLeftBoundary() {
        return position.getX() >= 320;
    }

    @Override
    protected boolean gameObjectHitsRightBoundary() {
        return position.getX() <= 960;
    }

    @Override
    protected boolean readyToSpawn() {
        return gameView.timer(randomNumberForTheSpawn, 200000, this);
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    @Override
    public void updatePosition() {
        if (readyToSpawn()) {
            position.down(speedInPixel);
            if (tankMovementPattern.movingRight && !gameObjectHitsLeftBoundary()) {
                position.right(speedInPixel);
            } else if (!tankMovementPattern.movingRight && !gameObjectHitsRightBoundary()) {
                position.left(speedInPixel);
            }
        }
        if (gameObjectHitsLowerBoundary()) {
            position.updateCoordinates(tankMovementPattern.nextPosition());
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
        if (tankMovementPattern.movingRight) {
            gameView.addImageToCanvas("tank.png", position.getX(), position.getY(), size, 0);
        } else {
            gameView.addImageToCanvas("tank_left.png", position.getX(), position.getY(), size, 0);
        }
    }
}
