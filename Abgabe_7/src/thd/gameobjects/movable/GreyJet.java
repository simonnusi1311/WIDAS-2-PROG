package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;

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
        distanceToBackground = 3;
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
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (other instanceof ShootFromPlayer) {
            gamePlayManager.addPoints(100);
            gamePlayManager.destroyGameObject(this);
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
        position.down(1.0);
        greyJetMovementPattern.gamingObjectCanMoveHorizontal(this);
        teleportToOppositeSide();
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
     * @param movingRight {code true} for moving right else left.
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
        if (greyJetMovementPattern.movingRight) {
            gameView.addImageToCanvas("grey_jet.png", position.getX(), position.getY(), size, 0);
        } else {
            gameView.addImageToCanvas("grey_jet_left.png", position.getX(), position.getY(), size, 0);
        }
    }

    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
