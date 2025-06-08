package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.*;
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

public class Helicopter extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {
    private final HelicopterMovementPattern helicopterMovementPattern;
    private HelicopterAnimationState helicopterAnimationState;
    private State currentState;
    private ExplosionState explosionState;
    private boolean isExplosionSound;

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
        speedInPixel = 2.2;
        size = 0.80;
        rotation = 0;
        width = 41;
        height = 26;
        hitBoxOffsets(8, 3, -6, 0);
        distanceToBackground = 4;
        helicopterAnimationState = helicopterMovementPattern.movingRight ? HelicopterAnimationState.RIGHT
                : HelicopterAnimationState.LEFT;
        currentState = State.FLYING;
        explosionState = ExplosionState.EXPLOSION_1;
    }

    private enum State {
        FLYING, EXPLODING
    }

    private enum HelicopterAnimationState {
        RIGHT("helicopter.png"), RIGHT_CHANGE("helicopter_change_right.png"),
        LEFT("helicopter_left.png"), LEFT_CHANGE("helicopter_change_left.png");

        private final String image;

        HelicopterAnimationState(String image) {
            this.image = image;
        }

        private HelicopterAnimationState nextRight() {
            return this == RIGHT ? RIGHT_CHANGE : RIGHT;
        }

        private HelicopterAnimationState nextLeft() {
            return this == LEFT ? LEFT_CHANGE : LEFT;
        }
    }

    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }
        switch (currentState) {
            case FLYING -> {
                if (gameView.timer(10, 0, this)) {
                    if (helicopterMovementPattern.movingRight) {
                        helicopterAnimationState = helicopterAnimationState.nextRight();
                    } else {
                        helicopterAnimationState = helicopterAnimationState.nextLeft();
                    }
                }
            }
            case EXPLODING -> {
                if (!isExplosionSound) {
                    gameView.playSound("explosion.wav", false);
                    isExplosionSound = true;
                }
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
        if (other instanceof JetFighter jetFighter) {
            if (jetFighter.isInvincible()) {
                return;
            }
            if (currentState == State.FLYING) {
                currentState = State.EXPLODING;
            }
        }

        if (other instanceof ShootFromPlayer) {
            gamePlayManager.addPoints(60);
            currentState = State.EXPLODING;
        }
        if (other instanceof SceneryRight || other instanceof SceneryLeft || other instanceof MovableSceneryLeft
                || other instanceof MovableSceneryRight || other instanceof BigIsland
                || other instanceof SmallIsland || other instanceof IslandTopHitBox || other instanceof IslandTopHitBoxTwo
                || other instanceof IslandBottomHitBox || other instanceof IslandBottomHitBoxTwo) {
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
        if (currentState == State.FLYING) {
            helicopterMovementPattern.gamingObjectCanMoveHorizontal(this);
        }
        helicopterMovementPattern.gameObjectMovesVertical(this);
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
            gameView.addImageToCanvas(explosionState.getImage(), position.getX() - 12, position.getY() - 12, size, 0);
        } else {
            gameView.addImageToCanvas(helicopterAnimationState.image, position.getX(), position.getY(), size, 0);
        }

    }

    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}

