package thd.gameobjects.movable;

import thd.game.level.Difficulty;
import thd.game.level.Level;
import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.*;

/**
 * Represents an enemy tank in the {@link GameView} window.
 * The tank is a movable object that has different properties,
 * such as speed and size. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class Tank extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {
    private final TankMovementPattern tankMovementPattern;
    private ShootFromTank shootFromTank;
    private boolean shotIsActive;
    private boolean stopHorizontalMovement;
    private State currentState;
    private boolean shootSoundFromTank;

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
        if (Level.difficulty == Difficulty.EASY) {
            speedInPixel = 1.8;
        } else {
            speedInPixel = 2.2;
        }
        size = 0.80;
        rotation = 0;
        width = 150;
        height = 33;
        hitBoxOffsets(8, 3, -100, 0);
        distanceToBackground = 4;
        shotIsActive = false;
        stopHorizontalMovement = false;
        currentState = tankMovementPattern.movingRight ? State.RIGHT : State.LEFT;
    }

    private enum State {
        RIGHT("tank.png"), RIGHT_CHANGE("tank_change_right.png"),
        LEFT("tank_left.png"), LEFT_CHANGE("tank_change_left.png");

        private final String image;

        State(String image) {
            this.image = image;
        }

        private State nextRight() {
            return this == RIGHT ? RIGHT_CHANGE : RIGHT;
        }

        private State nextLeft() {
            return this == LEFT ? LEFT_CHANGE : LEFT;
        }

        private String getImage() {
            return image;
        }
    }


    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (other instanceof SpecialBorderForTank) {
            stopHorizontalMovement = true;
        }
    }

    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }
        shootOnPlayer();
        if (!stopHorizontalMovement && gameView.timer(80, 0, this)) {
            if (tankMovementPattern.movingRight) {
                currentState = currentState.nextRight();
            } else {
                currentState = currentState.nextLeft();
            }
        }
    }

    private void shootOnPlayer() {
        if (stopHorizontalMovement && !shotIsActive) {
            shootFromTank = new ShootFromTank(gameView, gamePlayManager, this);
            double shootXCoordinate = tankMovementPattern.movingRight ? position.getX() + 38 : position.getX() - 8;
            shootFromTank.updateTheStartPositionFromShoot(shootXCoordinate, position.getY() + 10);
            changeShootToActive(shootFromTank);
        }
    }

    private void changeShootToActive(ShootFromTank shootFromTank) {
        if (position.getY() >= 585) {
            if (!shootSoundFromTank) {
                gameView.playSound("tank_shoot.wav", false);
                shootSoundFromTank = true;
            }
            gamePlayManager.spawnGameObject(shootFromTank);
            shotIsActive = true;
        }
    }

    /**
     * Determines the direction for the movement of the Tank.
     *
     * @param movingRight {@code true} for moving right else left.
     */
    public void initializeTheSpawnPoint(boolean movingRight) {
        tankMovementPattern.movingRight = movingRight;
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
        if (stopHorizontalMovement) {
            return;
        }
        tankMovementPattern.gamingObjectCanMoveHorizontal(this);
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
        gameView.addImageToCanvas(currentState.getImage(), position.getX(), position.getY(), size, 0);
    }

    @Override
    public boolean tryToActivate(JetFighter info) {
        return position.getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
