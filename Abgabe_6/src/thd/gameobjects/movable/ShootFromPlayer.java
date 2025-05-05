package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;

/**
 * The class which contains the String for the shoot.
 *
 * @see JetFighter
 */
class ShootFromPlayer extends CollidingGameObject {

    ShootFromPlayer(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        position.updateCoordinates(0, 0);
        speedInPixel = 4;
        size = 0.80;
        rotation = 0;
        width = 1;
        height = 5;
        hitBoxOffsets(0, 0, 0, 8);
    }

    @Override
    public void updateStatus() {
        if (shootHitsUpperBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (other instanceof Balloon || other instanceof Bridge || other instanceof FuelItem || other instanceof GreyJet
                || other instanceof Helicopter || other instanceof Ship) {
            gamePlayManager.destroyGameObject(this);
        }
    }

    private boolean shootHitsUpperBoundary() {
        return position.getY() <= 0;
    }

    @Override
    public void updatePosition() {
        position.up(speedInPixel);
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
        gameView.addBlockImageToCanvas(ShootFromPlayerBlockImages.SHOOT_FROM_PLAYER_BLOCK_IMAGES, position.getX(), position.getY(), 3, 0);
    }
}
