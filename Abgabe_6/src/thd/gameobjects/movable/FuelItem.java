package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
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

public class FuelItem extends CollidingGameObject {
    private final FuelItemMovementPattern fuelItemMovementPattern;

    /**
     * Creates a new fuel-item object with a random x Coordinate in the top-mid position.
     *
     * @param gameView        The gaming window where the fuel-item will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public FuelItem(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        fuelItemMovementPattern = new FuelItemMovementPattern();
        position.updateCoordinates(fuelItemMovementPattern.startPosition());
        speedInPixel = 1.3;
        size = 0.80;
        rotation = 0;
        width = 30;
        height = 90;
        hitBoxOffsets(5, 4, -6, 0);
    }


    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (other instanceof ShootFromPlayer) {
            gamePlayManager.addPoints(80);
            gamePlayManager.destroyGameObject(this);
        }
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    @Override
    public void updatePosition() {
        position.down(speedInPixel);
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
