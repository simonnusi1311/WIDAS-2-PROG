package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.MainCharacter;
import thd.gameobjects.base.Position;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents the main playable jet fighter in the {@link GameView} window.
 * The jet fighter can move horizontally and its position is defined by the
 * x and y coordinates from {@link Position}
 * The jet fighter also can shoot {@link ShootFromPlayerBlockImages} on the enemies.
 *
 * @see GameView
 * @see Position
 */

public class JetFighter extends CollidingGameObject implements MainCharacter {
    private final int shotDurationInMilliseconds;
    private final List<CollidingGameObject> collidingGameObjectsForPathDecision;
    private boolean collisionWithFuelItem;
    private boolean increaseTheSpeed;
    private final RedFuelBar redFuelBar;

    /**
     * Creates a new jet fighter object with position, speed, size and other properties.
     *
     * @param gameView        The gaming window where the jet fighter will be displayed.
     * @param gamePlayManager The main gameplay logic.
     * @param redFuelBar      The fuel bar for the interaction between it.
     */

    public JetFighter(GameView gameView, GamePlayManager gamePlayManager, RedFuelBar redFuelBar) {
        super(gameView, gamePlayManager);
        position.updateCoordinates(GameView.WIDTH / 2.0, 600);
        speedInPixel = 3;
        size = 0.80;
        rotation = 0;
        width = 38;
        height = 36;
        shotDurationInMilliseconds = 300;
        hitBoxOffsets(6, 7, -9, -3);
        collidingGameObjectsForPathDecision = new LinkedList<>();
        distanceToBackground = 4;
        this.redFuelBar = redFuelBar;
    }

    /**
     * The jet moves the left by the given number of pixels.
     *
     * @see Position
     */
    public void left() {
        position.left(speedInPixel);
        for (CollidingGameObject collidingGameObject : collidingGameObjectsForPathDecision) {
            if (collidesWith(collidingGameObject)) {
                position.right(speedInPixel);
                break;
            }
        }
    }

    /**
     * The jet moves the right by the given number of pixels.
     *
     * @see Position
     */
    public void right() {
        position.right(speedInPixel);
        for (CollidingGameObject collidingGameObject : collidingGameObjectsForPathDecision) {
            if (collidesWith(collidingGameObject)) {
                position.left(speedInPixel);
                break;
            }
        }
    }

    /**
     * The jet shoots.
     *
     * @see ShootFromPlayerBlockImages
     */
    @Override
    public void shoot() {
        ShootFromPlayer shootFromPlayer = new ShootFromPlayer(gameView, gamePlayManager);
        shootFromPlayer.getPosition().updateCoordinates(position.getX() + 20, position.getY() - 11);
        if (gameView.timer(shotDurationInMilliseconds, 0, this)) {
            gamePlayManager.spawnGameObject(shootFromPlayer);
        }
    }

    /**
     * Increase the speed and sets increaseTheSpeed on true.
     */
    public void speedUp() {
        increaseTheSpeed = true;
    }

    /**
     * Stops the speed and sets increaseTheSpeed on false.
     */
    public void stopSpeedUp() {
        increaseTheSpeed = false;
    }


    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (other instanceof FuelItem) {
            collisionWithFuelItem = true;
            gamePlayManager.fillUpTheFuelGage();
        }
    }

    /**
     * Adds a game object to the list of objects for path decision.
     *
     * @param collidingGameObject The game object to be checked for collision.
     */
    public void addPathDecisionObjects(CollidingGameObject collidingGameObject) {
        collidingGameObjectsForPathDecision.add(collidingGameObject);
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    @Override
    public void updatePosition() {
    }

    @Override
    public void updateStatus() {
        if (!collisionWithFuelItem) {
            gamePlayManager.stopFuelUpTheFuelGage();
        }
        collisionWithFuelItem = false;
        if (increaseTheSpeed) {
            gamePlayManager.moveWorldDown(1.3);
            redFuelBar.getPosition().left(0.10);
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
        gameView.addImageToCanvas("jet_fighter.png", position.getX(), position.getY(), size, 0);
    }
}
