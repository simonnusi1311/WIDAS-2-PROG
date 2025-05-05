package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
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

public class Helicopter extends GameObject {
    private final HelicopterMovementPattern helicopterMovementPattern;

    /**
     * Creates a new helicopter object with random position, speed, size and other properties.
     *
     * @param gameView        The gaming window where the helicopter will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public Helicopter(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        helicopterMovementPattern = new HelicopterMovementPattern();
        position.updateCoordinates(helicopterMovementPattern.startPosition());
        speedInPixel = 1.3;
        size = 0.80;
        rotation = 0;
        width = 0;
        height = 0;
    }

    @Override
    protected boolean gameObjectHitsLeftBoundary() {
        return position.getX() <= 220;
    }

    @Override
    protected boolean gameObjectHitsRightBoundary() {
        return position.getX() >= GameView.WIDTH - 265;
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
        helicopterMovementPattern.moveHorizontally(this);
        helicopterMovementPattern.changeDirectionIfObjectHitsBoundary(this);
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
        if (helicopterMovementPattern.movingRight) {
            gameView.addImageToCanvas("helicopter.png", position.getX(), position.getY(), size, 0);
        } else {
            gameView.addImageToCanvas("helicopter_left.png", position.getX(), position.getY(), size, 0);
        }
    }

}

