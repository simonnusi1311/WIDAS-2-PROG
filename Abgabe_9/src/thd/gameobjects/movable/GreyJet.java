package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.*;

/**
 * Represents an enemy jet in grey in the {@link GameView} window.
 * The jet is a movable object that has different properties,
 * such as speed and size. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class GreyJet extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {
    private final GreyJetMovementPattern greyJetMovementPattern;
    private State currentState;
    private GreyJetAnimationState greyJetAnimationState;
    private ExplosionState explosionState;

    /**
     * Creates a new grey jet object with position, speed and size.
     * The jet appears random on the left or right upper center in {@link GameView}.
     *
     * @param gameView        The gaming window where the grey jet will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public GreyJet(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        greyJetMovementPattern = new GreyJetMovementPattern();
        position.updateCoordinates(greyJetMovementPattern.startPosition());
        speedInPixel = 6;
        size = 0.80;
        rotation = 0;
        width = 35;
        height = 25;
        hitBoxOffsets(5, 3, -5, -8);
        distanceToBackground = 4;
        currentState = State.FLYING;
        greyJetAnimationState = greyJetMovementPattern.movingRight ? GreyJetAnimationState.RIGHT_1
                : GreyJetAnimationState.LEFT_1;
        explosionState = ExplosionState.EXPLOSION_1;
    }

    private enum State {
        FLYING, DAMAGED, EXPLODING
    }

    private enum GreyJetAnimationState {
        RIGHT_1("grey_jet_animation_right_one.png"),
        RIGHT_2("grey_jet_animation_right_two.png"),
        RIGHT_3("grey_jet_animation_right_three.png"),

        LEFT_1("grey_jet_animation_left_one.png"),
        LEFT_2("grey_jet_animation_left_two.png"),
        LEFT_3("grey_jet_animation_left_three.png");

        private final String image;

        GreyJetAnimationState(String image) {
            this.image = image;
        }

        private GreyJetAnimationState nextRight() {
            return switch (this) {
                case RIGHT_1 -> RIGHT_2;
                case RIGHT_2 -> RIGHT_3;
                case RIGHT_3, LEFT_3, LEFT_2, LEFT_1 -> RIGHT_1;
            };
        }

        private GreyJetAnimationState nextLeft() {
            return switch (this) {
                case LEFT_1 -> LEFT_2;
                case LEFT_2 -> LEFT_3;
                case LEFT_3, RIGHT_1, RIGHT_2, RIGHT_3 -> LEFT_1;
            };
        }

        private String getImage() {
            return image;
        }
    }

    @Override
    protected boolean gameObjectHitsLeftBoundary() {
        return position.getX() <= -10;
    }

    @Override
    protected boolean gameObjectHitsRightBoundary() {
        return position.getX() >= GameView.WIDTH;
    }

    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }
        switch (currentState) {
            case FLYING -> {
                if (gameView.timer(80, 0, this)) {
                    if (greyJetMovementPattern.movingRight) {
                        greyJetAnimationState = greyJetAnimationState.nextRight();
                    } else {
                        greyJetAnimationState = greyJetAnimationState.nextLeft();
                    }
                }
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
            gamePlayManager.addPoints(100);
            currentState = State.EXPLODING;
        }
        if (other instanceof JetFighter && currentState == State.FLYING) {
            gamePlayManager.lifeLost();
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
        if (currentState == State.FLYING) {
            greyJetMovementPattern.gamingObjectCanMoveHorizontal(this);
            teleportToOppositeSide();
        }
        position.down(1.3);
    }

    private void teleportToOppositeSide() {
        if (gameObjectHitsLeftBoundary()) {
            position.updateCoordinates(GameView.WIDTH, position.getY());
        } else if (gameObjectHitsRightBoundary()) {
            position.updateCoordinates(0, position.getY());
        }
    }

    /**
     * Determines the direction for the movement of the GreyJet.
     *
     * @param movingRight {@code true} for moving right else left.
     */
    public void initializeTheSpawnPoint(boolean movingRight) {
        greyJetMovementPattern.movingRight = movingRight;
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

            if (greyJetMovementPattern.movingRight) {
                gameView.addImageToCanvas("grey_jet.png", position.getX(), position.getY(), size, 0);
                gameView.addImageToCanvas(greyJetAnimationState.getImage(), position.getX() - 25, position.getY(), 0.07, 0);
            }
            if (!greyJetMovementPattern.movingRight) {
                gameView.addImageToCanvas("grey_jet_left.png", position.getX(), position.getY(), size, 0);
                gameView.addImageToCanvas(greyJetAnimationState.getImage(), position.getX() + 35, position.getY(), 0.07, 0);
            }
        }
    }

    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
