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
 * Represents an enemy ship in the {@link GameView} window.
 * The ship is a movable object that has different properties,
 * such as speed and size. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class Ship extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {
    private final ShipMovementPattern shipMovementPattern;
    private State currentState;

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
        width = 70;
        height = 30;
        hitBoxOffsets(7, 4, -2, -5);
        distanceToBackground = 2;
        currentState = State.STANDARD;
    }

    private enum State {
        STANDARD, DAMAGED, EXPLODING, LEFT, RIGHT
    }


    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }
        switch (currentState) {
            case STANDARD -> {
            }
            case DAMAGED -> {
            }
            case LEFT -> {
            }
            case RIGHT -> {
            }
            case EXPLODING -> {
            }
        }
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (other instanceof ShootFromPlayer) {
            gamePlayManager.addPoints(30);
            gamePlayManager.destroyGameObject(this);
        }
        if (other instanceof JetFighter) {
            gamePlayManager.lifeLost();
            gamePlayManager.destroyGameObject(this);
        }
        if (other instanceof SceneryLeft || other instanceof SceneryRight || other instanceof MovableSceneryLeft
                || other instanceof MovableSceneryRight || other instanceof BigIsland
                || other instanceof SmallIsland || other instanceof MovableSceneryFill) {
            shipMovementPattern.changeDirectionIfObjectHitsBoundary();
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
        shipMovementPattern.gamingObjectCanMoveHorizontal(this);
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

    @Override
    public boolean tryToActivate(JetFighter info) {
        return position.getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
