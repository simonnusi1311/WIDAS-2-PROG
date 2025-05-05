package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;

/**
 * Represents an enemy tank in the {@link GameView} window.
 * The tank is a movable object that has different properties,
 * such as speed and size. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class Tank extends GameObject implements ShiftableGameObject {
    private final TankMovementPattern tankMovementPattern;
    private ShootFromTank shootFromTank;
    private final int randomNumberForShoot;

    /**
     * Creates a new ship object with default position, speed, size and other properties.
     *
     * @param gameView        The gaming window where the ship will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */
    public Tank(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        tankMovementPattern = new TankMovementPattern();
        position.updateCoordinates(tankMovementPattern.startPosition());
        speedInPixel = 1.3;
        size = 0.80;
        rotation = 0;
        width = 150;
        height = 33;
        randomNumberForShoot = (int) (Math.random() * 6000) + 2000;
        distanceToBackground = 2;
    }

    @Override
    protected boolean gameObjectHitsLeftBoundary() {
        return position.getX() >= 175;
    }

    @Override
    protected boolean gameObjectHitsRightBoundary() {
        return position.getX() <= GameView.WIDTH - 220;
    }

    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }
        shootOnPlayer();
    }

    private void shootOnPlayer() {
        boolean tankHitsRightOrLeftBoundary = (gameObjectHitsLeftBoundary() && tankMovementPattern.movingRight)
                || (gameObjectHitsRightBoundary() && !tankMovementPattern.movingRight);
        if (tankHitsRightOrLeftBoundary) {
            shootFromTank = new ShootFromTank(gameView, gamePlayManager, this);
            double shootXCoordinate = tankMovementPattern.movingRight ? position.getX() + 38 : position.getX() - 8;
            shootFromTank.updateTheStartPositionFromShoot(shootXCoordinate, position.getY() + 10);

            if (gameView.timer(randomNumberForShoot, 0, this)) {
                gamePlayManager.spawnGameObject(shootFromTank);
            }
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
        moveHorizontally();
    }

    private void moveHorizontally() {
        if (tankMovementPattern.movingRight && !gameObjectHitsLeftBoundary()) {
            position.right(speedInPixel);
        } else if (!tankMovementPattern.movingRight && !gameObjectHitsRightBoundary()) {
            position.left(speedInPixel);
        }
    }


    /**
     * Returns the movement pattern of the tank.
     * This pattern determines the direction in which the shoot appears and moves.
     *
     * @return the tankMovementPattern from the tank.
     */
    TankMovementPattern getTankMovementPattern() {
        return tankMovementPattern;
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
        if (tankMovementPattern.movingRight) {
            gameView.addImageToCanvas("tank.png", position.getX(), position.getY(), size, 0);
        }
        if (!tankMovementPattern.movingRight) {
            gameView.addImageToCanvas("tank_left.png", position.getX(), position.getY(), size, 0);
        }
    }
}
