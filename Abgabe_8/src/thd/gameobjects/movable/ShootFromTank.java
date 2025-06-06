package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;

/**
 * The class which contains the white shoot from the tank.
 *
 * @see Tank
 */
class ShootFromTank extends CollidingGameObject implements ShiftableGameObject {

    private final Tank tank;

    ShootFromTank(GameView gameView, GamePlayManager gamePlayManager, Tank tank) {
        super(gameView, gamePlayManager);
        this.tank = tank;
        speedInPixel = 5;
        size = 0.80;
        rotation = 0;
        width = 9;
        height = 2;
        distanceToBackground = 2;
        hitBoxOffsets(0, 0, 1, 0);
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
    public void reactToCollisionWith(CollidingGameObject other) {
        if (other instanceof JetFighter) {
            gamePlayManager.destroyGameObject(this);
            gamePlayManager.lifeLost();
        }
    }

    @Override
    public void updatePosition() {
        position.down(0.5);
        if (tank.getTankMovementPattern().movingRight) {
            position.right(speedInPixel);
        }
        if (!tank.getTankMovementPattern().movingRight) {
            position.left(speedInPixel);
        }
    }

    @Override
    protected boolean gameObjectHitsLeftBoundary() {
        return position.getX() <= 0;
    }

    @Override
    protected boolean gameObjectHitsRightBoundary() {
        return position.getX() >= GameView.WIDTH;
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
        gameView.addBlockImageToCanvas(ShootFromTankBlockImages.SHOOT_FROM_TANK_BLOCK_IMAGES, position.getX(), position.getY(), 4, 0);
    }
}
