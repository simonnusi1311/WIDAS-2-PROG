package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.*;
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
    private ShipAnimationState shipAnimationState;
    private ExplosionState explosionState;

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
        currentState = State.DRIVING;
        shipAnimationState = ShipAnimationState.WAVE_1;
        explosionState = ExplosionState.EXPLOSION_1;
    }

    private enum State {
        DRIVING, DAMAGED, EXPLODING
    }

    private enum ShipAnimationState {
        WAVE_1("ship_wave_one.png"),
        WAVE_2("ship_wave_three.png"),
        WAVE_3("ship_wave_two.png");

        private final String image;

        ShipAnimationState(String image) {
            this.image = image;
        }

        private ShipAnimationState next() {
            return values()[(ordinal() + 1) % values().length];
        }

    }


    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }
        switch (currentState) {
            case DRIVING -> {
                if (gameView.timer(65, 0, this)) {
                    shipAnimationState = shipAnimationState.next();
                }
            }
            case DAMAGED -> {
            }
            case EXPLODING -> {
                height = 0;
                width = 0;
                if (gameView.timer(100, 0, this)) {
                    if (explosionState == ExplosionState.EXPLOSION_3) {
                        gamePlayManager.destroyGameObject(this);
                    } else {
                        explosionState = explosionState.next();
                    }
                }
            }
        }
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (other instanceof ShootFromPlayer) {
            gamePlayManager.addPoints(30);
            currentState = State.EXPLODING;
        }
        if (other instanceof JetFighter && currentState == State.DRIVING) {
            currentState = State.EXPLODING;
            gamePlayManager.lifeLost();
        }
        if (other instanceof SceneryRight || other instanceof SceneryLeft || other instanceof MovableSceneryLeft
                || other instanceof MovableSceneryRight || other instanceof BigIsland
                || other instanceof SmallIsland || other instanceof IslandTopHitBox || other instanceof IslandTopHitBoxTwo
                || other instanceof IslandBottomHitBox || other instanceof IslandBottomHitBoxTwo) {
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
        if (currentState == State.DRIVING) {
            shipMovementPattern.gamingObjectCanMoveHorizontal(this);
        }
        shipMovementPattern.gameObjectMovesVertical(this);
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
        if (currentState == State.EXPLODING) {
            gameView.addImageToCanvas(explosionState.getImage(), position.getX() + 15, position.getY() - 10, size, 0);
        } else {
            gameView.addImageToCanvas(shipAnimationState.image, position.getX(), position.getY() + 27, 0.15, 0);
            if (shipMovementPattern.movingRight) {
                gameView.addImageToCanvas("ship.png", position.getX(), position.getY(), size, 0);
            } else {
                gameView.addImageToCanvas("ship_left.png", position.getX(), position.getY(), size, 0);
            }
        }
    }

    @Override
    public boolean tryToActivate(JetFighter info) {
        return position.getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
