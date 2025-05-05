package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * The class is responsible for the shoot from the Tank.
 *
 * @see JetFighter
 */
class ShootFromTank extends GameObject {
    private final Tank tank;

    ShootFromTank(GameView gameView, GamePlayManager gamePlayManager, Tank tank) {
        super(gameView, gamePlayManager);
        this.tank = tank;
        speedInPixel = 4;
        size = 0.80;
        rotation = 0;
        width = 0;
        height = 0;
    }

    @Override
    protected boolean gameObjectHitsLeftBoundary() {
        return position.getX() <= 208;
    }

    @Override
    protected boolean gameObjectHitsRightBoundary() {
        return position.getX() >= GameView.WIDTH - 228;
    }

    @Override
    public void updateStatus() {
        if (gameObjectHitsLeftBoundary()) {
            gamePlayManager.destroyGameObject(this);
        } else if (gameObjectHitsRightBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }
    }

    @Override
    public void updatePosition() {
        if (tank.getTankMovementPattern().movingRight) {
            position.right(speedInPixel);
        }
        if (!tank.getTankMovementPattern().movingRight) {
            position.left(speedInPixel);
        }
    }

    void updateTheStartPositionFromShoot(double xCoordinate, double yCoordinate) {
        position.updateCoordinates(xCoordinate, yCoordinate);
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
        gameView.addBlockImageToCanvas(ShootFromTankBlockImages.SHOOT_FROM_PLAYER_BLOCK_IMAGES, position.getX(), position.getY(), 4, 0);
    }
}
