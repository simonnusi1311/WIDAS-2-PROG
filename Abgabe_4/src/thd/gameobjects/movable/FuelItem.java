package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * Represents an enemy fuel-item in the {@link GameView} window.
 * The fuel-item is a movable object which appears in the top-mid
 * position. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class FuelItem extends GameObject {
    private final FuelItemMovementPattern fuelItemMovementPattern;

    /**
     * Creates a new fuel-item object with a random x Coordinate in the top-mid position.
     *
     * @param gameView The gaming window where the fuel-item will be displayed.
     */

    public FuelItem(GameView gameView) {
        super(gameView);
        fuelItemMovementPattern = new FuelItemMovementPattern();
        position.updateCoordinates(fuelItemMovementPattern.startPosition());
        speedInPixel = 1;
        size = 0.80;
        rotation = 0;
        width = 0;
        height = 0;
        randomNumberForTheSpawn = (int) (Math.random() * 5000) + 5000;
    }

    @Override
    public String toString() {
        return "FuelItem: " + position;
    }

    @Override
    protected boolean gameObjectHitsLeftBoundary() {
        return position.getX() <= 320;
    }

    @Override
    protected boolean gameObjectHitsRightBoundary() {
        return position.getX() >= 960;
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
        }
        if (gameObjectHitsLowerBoundary()) {
            position.updateCoordinates(fuelItemMovementPattern.nextPosition());
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
        gameView.addImageToCanvas("fuel.png", position.getX(), position.getY(), size, 0);

    }


}
