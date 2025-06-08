package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.*;

/**
 * Represents an enemy fuel-item in the {@link GameView} window.
 * The fuel-item is a movable object which appears in the top-mid
 * position. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */
public class FuelItem extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {
    private final FuelItemMovementPattern fuelItemMovementPattern;
    private State currentState;
    private ExplosionState explosionState;
    private boolean isExplosionSound;

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
        speedInPixel = 2.2;
        size = 0.80;
        rotation = 0;
        width = 30;
        height = 90;
        hitBoxOffsets(5, 4, -6, 0);
        distanceToBackground = 3;
        currentState = State.MOVING;
        explosionState = ExplosionState.EXPLOSION_1;
    }

    private enum State {
        MOVING, EXPLODING
    }

    private enum FuelItemAnimation {

    }


    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }

        switch (currentState) {
            case MOVING -> {

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
        if (other instanceof ShootFromPlayer) {
            gamePlayManager.addPoints(80);
            currentState = State.EXPLODING;
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
        if (currentState == State.EXPLODING) {
            gameView.addImageToCanvas(explosionState.getImage(), position.getX() - 12, position.getY() + 15, size, 0);
        } else {
            gameView.addImageToCanvas("fuel.png", position.getX(), position.getY(), size, 0);
        }
    }

    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
