package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.*;
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
    private State currentState;
    private BalloonAnimationState balloonAnimationState;
    private ExplosionState explosionState;

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
        distanceToBackground = 4;
        currentState = State.FLYING;
        balloonAnimationState = BalloonAnimationState.FLYING_1;
        explosionState = ExplosionState.EXPLOSION_1;
    }

    private enum State {
        FLYING, DAMAGED, EXPLODING,
    }

    private enum BalloonAnimationState {
        FLYING_1("balloon_animation_1.png"),
        FLYING_2("balloon_animation_2.png"),
        FLYING_3("balloon_animation_3.png");

        private final String image;

        BalloonAnimationState(String image) {
            this.image = image;
        }

        private BalloonAnimationState next() {
            return values()[(ordinal() + 1) % values().length];
        }
    }

    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }
        switch (currentState) {
            case FLYING -> {
                if (gameView.timer(100, 0, this)) {
                    balloonAnimationState = balloonAnimationState.next();
                }
            }
            case DAMAGED -> {
            }
            case EXPLODING -> {
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
            gamePlayManager.addPoints(60);
            currentState = State.EXPLODING;
        }
        if (other instanceof JetFighter) {
            gamePlayManager.lifeLost();
            currentState = State.EXPLODING;
        }
        if (other instanceof SceneryRight || other instanceof SceneryLeft || other instanceof MovableSceneryLeft
                || other instanceof MovableSceneryRight || other instanceof BigIsland
                || other instanceof SmallIsland || other instanceof MovableSceneryFill) {
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
        if (currentState == State.FLYING) {
            balloonMovementPattern.gamingObjectCanMoveHorizontal(this);
        }
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
        if (currentState == State.EXPLODING) {
            gameView.addImageToCanvas(explosionState.getImage(), position.getX(), position.getY(), size, 0);
        } else {
            gameView.addImageToCanvas(balloonAnimationState.image, position.getX() + 11, position.getY() + 57, 0.18, 0);
            gameView.addImageToCanvas("balloon.png", position.getX(), position.getY(), size, 0);
        }
    }

    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
