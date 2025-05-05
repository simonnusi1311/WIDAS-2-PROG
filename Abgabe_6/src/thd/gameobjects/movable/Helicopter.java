package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.unmovable.SceneryLeft;
import thd.gameobjects.unmovable.SceneryRight;

/**
 * Represents an enemy helicopter in the {@link GameView} window.
 * The helicopter is a movable object that has different properties,
 * such as speed and size. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class Helicopter extends CollidingGameObject {
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
        width = 41;
        height = 26;
        hitBoxOffsets(8, 3, -6, 0);
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
        if (other instanceof SceneryLeft || other instanceof SceneryRight) {
            helicopterMovementPattern.changeDirectionIfObjectHitsBoundary();
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
        helicopterMovementPattern.gamingObjectCanMoveHorizontal(this);
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

