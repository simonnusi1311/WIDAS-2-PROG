package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * Represents an enemy balloon in the {@link GameView} window.
 * The balloon is a movable object that has different properties,
 * such as speed and size. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class Balloon extends GameObject {
    private final BalloonMovementPattern balloonMovementPattern;

    /**
     * Creates a new balloon object with random position, speed, size and other properties.
     *
     * @param gameView        The gaming window where the balloon will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public Balloon(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        balloonMovementPattern = new BalloonMovementPattern();
        position.updateCoordinates(balloonMovementPattern.startPosition());
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
        return position.getX() >= GameView.WIDTH - 255;
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
        balloonMovementPattern.moveHorizontally(this);
        balloonMovementPattern.changeDirectionIfObjectHitsBoundary(this);
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
        gameView.addImageToCanvas("balloon.png", position.getX(), position.getY(), size, 0);
    }


}
