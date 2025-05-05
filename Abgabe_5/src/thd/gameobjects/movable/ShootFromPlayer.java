package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * The class is responsible for the shoot from the JetFighter,
 *
 * @see JetFighter
 */
class ShootFromPlayer extends GameObject {

    ShootFromPlayer(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        position.updateCoordinates(0, 0);
        speedInPixel = 4;
        size = 0.80;
        rotation = 0;
        width = 0;
        height = 0;
    }

    @Override
    public void updateStatus() {
        if (shootHitsUpperBoundary()) {
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
