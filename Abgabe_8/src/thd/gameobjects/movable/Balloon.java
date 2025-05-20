package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;
import thd.gameobjects.unmovable.SceneryLeft;
import thd.gameobjects.unmovable.SceneryRight;

/**
 * Represents an enemy balloon in the {@link GameView} window.
 * The balloon is a movable object that has different properties,
 * such as speed and size. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class Balloon extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {
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
        width = 28;
        height = 55;
        hitBoxOffsets(7, 5, -4, -5);
        distanceToBackground = 2;
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
            gamePlayManager.addPoints(60);
            gamePlayManager.destroyGameObject(this);
        }
        if (other instanceof JetFighter) {
            gamePlayManager.lifeLost();
            gamePlayManager.destroyGameObject(this);
        }
        if (other instanceof SceneryRight || other instanceof SceneryLeft) {
            balloonMovementPattern.changeDirectionIfObjectHitsBoundary();
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
        balloonMovementPattern.gamingObjectCanMoveHorizontal(this);
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

    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
