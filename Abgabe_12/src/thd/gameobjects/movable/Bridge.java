package thd.gameobjects.movable;

import thd.game.level.Difficulty;
import thd.game.level.Level;
import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.*;

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
    private ExplosionState explosionState;
    private BridgeDamaged bridgeDamaged;
    private int hitCountForAnimation;
    private boolean isExplosionSound;


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
        if (Level.difficulty == Difficulty.EASY) {
            speedInPixel = 1.8;
        } else {
            speedInPixel = 2.2;
        }
        size = 0.50;
        rotation = 0;
        width = 135;
        height = 75;
        hitBoxOffsets(4, 10, 0, -5);
        distanceToBackground = 2;
        currentState = State.STANDARD;
        explosionState = ExplosionState.EXPLOSION_1;
        bridgeDamaged = BridgeDamaged.BRIDGE_DAMAGED_1;
        hitCountForAnimation = 0;
    }

    private enum State {
        STANDARD, DAMAGED, EXPLODED
    }

    private enum BridgeDamaged {
        BRIDGE_DAMAGED_1("bridge_smoke_1.png"),
        BRIDGE_DAMAGED_2("bridge_smoke_2.png"),
        BRIDGE_DAMAGED_3("bridge_smoke_3.png"),
        BRIDGE_DAMAGED_4("bridge_smoke_4.png"),
        BRIDGE_DAMAGED_5("bridge_smoke_5.png"),
        BRIDGE_DAMAGED_6("bridge_smoke_6.png");

        private final String image;

        BridgeDamaged(String image) {
            this.image = image;
        }

        private BridgeDamaged firstHit() {
            return switch (this) {
                case BRIDGE_DAMAGED_6 -> BRIDGE_DAMAGED_1;
                case BRIDGE_DAMAGED_1 -> BRIDGE_DAMAGED_5;
                case BRIDGE_DAMAGED_5, BRIDGE_DAMAGED_2, BRIDGE_DAMAGED_3, BRIDGE_DAMAGED_4 -> BRIDGE_DAMAGED_6;
            };
        }

        private BridgeDamaged secondHit() {
            return switch (this) {
                case BRIDGE_DAMAGED_1 -> BRIDGE_DAMAGED_5;
                case BRIDGE_DAMAGED_5 -> BRIDGE_DAMAGED_2;
                case BRIDGE_DAMAGED_2 -> BRIDGE_DAMAGED_3;
                case BRIDGE_DAMAGED_3 -> BRIDGE_DAMAGED_4;
                case BRIDGE_DAMAGED_4, BRIDGE_DAMAGED_6 -> BRIDGE_DAMAGED_2;
            };
        }

        private String getImage() {
            return image;
        }
    }

    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }
        switch (currentState) {
            case DAMAGED -> {
                if (gameView.timer(60, 0, this) && hitCountForAnimation == 1) {
                    bridgeDamaged = bridgeDamaged.firstHit();
                } else if (gameView.timer(60, 0, this) && hitCountForAnimation == 2) {
                    bridgeDamaged = bridgeDamaged.secondHit();
                }
            }
            case EXPLODED -> {
                if (!isExplosionSound) {
                    gameView.playSound("bridge.wav", false);
                    isExplosionSound = true;
                }
                height = 0;
                width = 0;
                if (gameView.timer(100, 0, this)) {
                    if (explosionState == ExplosionState.EXPLOSION_3) {
                        gamePlayManager.addPoints(100);
                        gamePlayManager.bridgeDestroyed();
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
        if (other instanceof ShootFromPlayer shootFromPlayer) {
            gamePlayManager.destroyGameObject(shootFromPlayer);
            hitCountForAnimation++;

            if (hitCountForAnimation == 1) {
                bridgeDamaged = BridgeDamaged.BRIDGE_DAMAGED_1;
                currentState = State.DAMAGED;
            } else if (hitCountForAnimation == 2) {
                bridgeDamaged = BridgeDamaged.BRIDGE_DAMAGED_4;
            } else if (hitCountForAnimation >= 3) {
                currentState = State.EXPLODED;
            }
        }
        if (other instanceof JetFighter) {
            gamePlayManager.lifeLost();
            currentState = State.EXPLODED;
        }
    }

    /**
     * Change the Level Section if bridge gets destroyed.
     *
     * @param counterForLevel the next level to set.
     */
    public void setCounterForLevel(int counterForLevel) {
        this.counterForLevel = counterForLevel;
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    @Override
    public void updatePosition() {
        position.down(speedInPixel);
        if (position.getY() >= 600) {
            gamePlayManager.finishedLevel();
        }
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
        if (currentState == State.EXPLODED) {
            gameView.addImageToCanvas(explosionState.getImage(), position.getX() + 10, position.getY() - 5, 0.8, 0);
            gameView.addImageToCanvas(explosionState.getImage(), position.getX() + 70, position.getY() - 5, 0.8, 0);
        } else {
            if ((currentState == State.DAMAGED)) {
                gameView.addImageToCanvas(bridgeDamaged.getImage(), position.getX() + 20, position.getY() - 85, size, 0);
            }

            gameView.addImageToCanvas("bridge.png", position.getX(), position.getY(), size, 0);
            gameView.addTextToCanvas(String.format("%04d", counterForLevel), position.getX() + 26, position.getY() + 24, 35, true, Color.BLACK, 0, "font.ttf");
        }
    }


    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
