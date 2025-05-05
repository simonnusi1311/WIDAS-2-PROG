package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
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
     * @param gameView        The gaming window where the ship will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */
    public Ship(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        shipMovementPattern = new ShipMovementPattern();
        position.updateCoordinates(shipMovementPattern.startPosition());
        speedInPixel = 1.3;
        size = 0.80;
        rotation = 0;
        width = 150;
        height = 33;
    }

    @Override
    protected boolean gameObjectHitsLeftBoundary() {
        return position.getX() <= 220;
    }

    @Override
    protected boolean gameObjectHitsRightBoundary() {
        return position.getX() >= GameView.WIDTH - 300;
    }

    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
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
        shipMovementPattern.moveHorizontally(this);
        shipMovementPattern.changeDirectionIfObjectHitsBoundary(this);
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
            gameView.addImageToCanvas("ship.png", position.getX(), position.getY(), size, 0);
        } else {
            gameView.addImageToCanvas("ship_left.png", position.getX(), position.getY(), size, 0);
        }
    }
}
