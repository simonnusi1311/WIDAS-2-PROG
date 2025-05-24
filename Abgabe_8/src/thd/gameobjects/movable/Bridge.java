package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;

import java.awt.*;

/**
 * Represents a bridge in the {@link GameView} window.
 * The bridge is a movable object and is a border to the
 * next level section. {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class Bridge extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {
    private final BridgeMovementPattern bridgeMovementPattern;
    private int counterForLevel;
    private State currentState;


    /**
     * Creates a new bridge in game view.
     *
     * @param gameView        The gaming window where the Bridge will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public Bridge(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        bridgeMovementPattern = new BridgeMovementPattern();
        position.updateCoordinates(bridgeMovementPattern.startPosition());
        speedInPixel = 1.3;
        size = 0.50;
        rotation = 0;
        width = 135;
        height = 75;
        counterForLevel = 1;
        hitBoxOffsets(4, 10, 0, -5);
        distanceToBackground = 3;
        currentState = State.STANDARD;
    }

    private enum State {
        STANDARD, DAMAGED, EXPLODING, EXPLODED
    }

    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            counterForLevel++;
            gamePlayManager.destroyGameObject(this);
        }
        switch (currentState) {
            case STANDARD:

            case DAMAGED:

            case EXPLODING:

            case EXPLODED:

        }
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (other instanceof ShootFromPlayer) {
            gamePlayManager.addPoints(100);
            gamePlayManager.destroyGameObject(this);
            gamePlayManager.levelCounter();
        }
        if (other instanceof JetFighter) {
            gamePlayManager.lifeLost();
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
    }

    //    /**
    //     * Set the counter for the current level.
    //     *
    //     * @param counterForLevel the value to set.
    //     */
    //    public void setCounterForLevel(int counterForLevel) {
    //        this.counterForLevel = counterForLevel;
    //    }

    /**
     * Adds the gaming object to the game canvas in {@link GameView}
     * by placing an image or shape (oval, rectangle, etc.) at the respective position.
     *
     * @see GameView
     * @see Position
     */
    @Override
    public void addToCanvas() {
        gameView.addImageToCanvas("bridge.png", position.getX(), position.getY(), size, 0);
        gameView.addTextToCanvas(String.format("%04d", counterForLevel), position.getX() + 26, position.getY() + 24, 35, true, Color.BLACK, 0, "font.ttf");
    }


    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
