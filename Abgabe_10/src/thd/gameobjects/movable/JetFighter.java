package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.ExplosionState;
import thd.gameobjects.base.MainCharacter;
import thd.gameobjects.base.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
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
    private FlyingState flyingState;
    private State currentState;
    private SpeedingState speedingState;
    private boolean isFlyingRight;
    private boolean isFlyingLeft;
    private InitializeSpawnPoint initializeSpawnPoint;
    private ExplosionState explosionState;
    private boolean blinkVisible;
    private boolean isInRespawnPhase;

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
        flyingState = FlyingState.FLYING_STANDARD;
        currentState = State.FLYING;
        speedingState = SpeedingState.FLYING_1;
        explosionState = ExplosionState.EXPLOSION_1;
        isInRespawnPhase = false;
    }

    private enum State {
        FLYING, EXPLODING, RESPAWNING
    }

    private enum FlyingState {
        FLYING_STANDARD("jet_fighter.png"),
        FLYING_LEFT("jet_fighter_left.png"),
        FLYING_RIGHT("jet_fighter_right.png");

        private final String image;

        FlyingState(String image) {
            this.image = image;
        }

        private String getImage() {
            return image;
        }
    }

    private enum SpeedingState {
        FLYING_1("exhaust_one.png"),
        FLYING_2("exhaust_two.png");

        private final String image;

        SpeedingState(String image) {
            this.image = image;
        }

        private SpeedingState next() {
            return values()[(ordinal() + 1) % values().length];
        }

        private String getImage() {
            return image;
        }
    }

    /**
     * The jet moves the left by the given number of pixels.
     *
     * @see Position
     */
    public void left() {
        position.left(speedInPixel);
        isFlyingLeft = true;
        for (CollidingGameObject collidingGameObject : collidingGameObjectsForPathDecision) {
            if (collidesWith(collidingGameObject) && isBlockingObject(collidingGameObject)) {
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
        isFlyingRight = true;
        for (CollidingGameObject collidingGameObject : collidingGameObjectsForPathDecision) {
            if (collidesWith(collidingGameObject) && isBlockingObject(collidingGameObject)) {
                position.left(speedInPixel);
                break;
            }
        }
    }

    private boolean isBlockingObject(CollidingGameObject collidingGameObject) {
        return collidingGameObject instanceof BridgeLeft || collidingGameObject instanceof BridgeRight;
    }

    /**
     * The jet shoots.
     *
     * @see ShootFromPlayerBlockImages
     */
    @Override
    public void shoot() {
        if (currentState != State.RESPAWNING) {
            ShootFromPlayer shootFromPlayer = new ShootFromPlayer(gameView, gamePlayManager);
            shootFromPlayer.getPosition().updateCoordinates(position.getX() + 20, position.getY() - 11);
            if (gameView.timer(shotDurationInMilliseconds, 0, this)) {
                gamePlayManager.spawnGameObject(shootFromPlayer);
            }
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
        if (currentState != State.FLYING) {
            return;
        }
        if (other instanceof FuelItem) {
            collisionWithFuelItem = true;
            gamePlayManager.fillUpTheFuelGage();
            return;
        }
        if (!isInvincible() && (isEnemyCollidingWithJet(other) || isSceneryCollidingWithJet(other))) {
            gamePlayManager.lifeLost();
            triggerExplosion();
        }
    }

    private boolean isEnemyCollidingWithJet(CollidingGameObject other) {
        return other instanceof Balloon
                || other instanceof Ship
                || other instanceof Helicopter
                || other instanceof GreyJet
                || other instanceof ShootFromTank;
    }

    private boolean isSceneryCollidingWithJet(CollidingGameObject other) {
        return other instanceof BigIsland
                || other instanceof SmallIsland
                || other instanceof IslandBottomHitBox
                || other instanceof IslandBottomHitBoxTwo
                || other instanceof IslandTopHitBox
                || other instanceof IslandTopHitBoxTwo
                || other instanceof MovableSceneryRight
                || other instanceof MovableSceneryLeft;
    }

    /**
     * Checks whether the JetFighter is currently invincible during the respawn phase.
     *
     * @return true if the JetFighter is in the respawn phase and cannot be damaged.
     */
    public boolean isInvincible() {
        return isInRespawnPhase;
    }

    private void triggerExplosion() {
        currentState = State.EXPLODING;
        explosionState = ExplosionState.EXPLOSION_1;
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
     * Removes all game objects of the list.
     */
    public void removePathDecisionObjects() {
        collidingGameObjectsForPathDecision.clear();
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    @Override
    public void updatePosition() {
    }

    /**
     * Sets the spawn point based on the given {@link InitializeSpawnPoint} object.
     *
     * @param initializeSpawnPoint the object used to determine the spawn point.
     */
    public void setInitializeSpawnPoint(InitializeSpawnPoint initializeSpawnPoint) {
        this.initializeSpawnPoint = initializeSpawnPoint;
    }

    @Override
    public void updateStatus() {
        initializeSpawnPointForPlayer();
        handleFuelLogic();
        handleSpeedLogicOrFreeze();

        switch (currentState) {
            case FLYING -> {
                if (increaseTheSpeed && gameView.timer(100, 0, this)) {
                    speedingState = speedingState.next();
                }
                if (isFlyingRight) {
                    flyingState = FlyingState.FLYING_RIGHT;
                } else if (isFlyingLeft) {
                    flyingState = FlyingState.FLYING_LEFT;
                } else {
                    flyingState = FlyingState.FLYING_STANDARD;
                }
            }
            case EXPLODING -> {
                if (gameView.timer(100, 0, this)) {
                    explosionState = explosionState.next();
                }
                if (explosionState == ExplosionState.EXPLOSION_3) {
                    position.updateCoordinates(findSafeRespawnPosition());
                    currentState = State.RESPAWNING;
                    isInRespawnPhase = true;
                    blinkVisible = true;
                }
            }
            case RESPAWNING -> {
                flyingState = FlyingState.FLYING_STANDARD;

                if (gameView.timer(150, 0, this)) {
                    blinkVisible = !blinkVisible;
                }

                if (gameView.timer(1000, 0, this)) {
                    currentState = State.FLYING;
                    isInRespawnPhase = false;
                    blinkVisible = true;
                }
            }
        }
        isFlyingLeft = false;
        isFlyingRight = false;
    }

    private void initializeSpawnPointForPlayer() {
        if (position.getY() <= initializeSpawnPoint.getPosition().getY()) {
            gamePlayManager.finishedLevel();
        }
    }

    private void handleFuelLogic() {
        if (!collisionWithFuelItem) {
            gamePlayManager.stopFuelUpTheFuelGage();
        }
        collisionWithFuelItem = false;
    }

    private void handleSpeedLogicOrFreeze() {
        if (currentState == State.FLYING) {
            if (increaseTheSpeed) {
                gamePlayManager.moveWorldDown(2.5);
                redFuelBar.getPosition().left(0.15);
            }
        } else if (currentState == State.EXPLODING || currentState == State.RESPAWNING) {
            gamePlayManager.moveWorldUp(1.8);
        }
    }

    private Position findSafeRespawnPosition() {
        ArrayList<Double> possibleXCoordinatesToSpawn = new ArrayList<>();

        for (double xCoordinate = 0; xCoordinate <= GameView.WIDTH - 40.0; xCoordinate += 80.0) {
            possibleXCoordinatesToSpawn.add(xCoordinate);
        }
        double currentXCoordinate = position.getX();
        possibleXCoordinatesToSpawn.sort(Comparator.comparingDouble(spawnX -> Math.abs(spawnX - currentXCoordinate)));

        for (double xCoordinate : possibleXCoordinatesToSpawn) {
            position.updateCoordinates(xCoordinate, 600);
            boolean collides = false;

            for (CollidingGameObject object : collidingGameObjectsForPathDecision) {
                if (collidesWith(object)) {
                    collides = true;
                    break;
                }
            }
            if (!collides) {
                return new Position(xCoordinate, 600);
            }
        }
        return new Position(GameView.WIDTH / 2.0, 600);
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
            return;
        }

        if (currentState == State.RESPAWNING) {
            showOvalIfJetIsInRespawnPhase();
        }
        if (currentState == State.RESPAWNING && !blinkVisible) {
            return;
        }

        if (increaseTheSpeed) {
            gameView.addImageToCanvas(speedingState.getImage(), position.getX() - 82.5, position.getY() - 9, 0.2, 0);
        }
        gameView.addImageToCanvas(flyingState.getImage(), position.getX(), position.getY(), size, 0);
    }

    private void showOvalIfJetIsInRespawnPhase() {
        gameView.addOvalToCanvas(position.getX() + 20, position.getY() + 23, 60, 55, 2, false, Color.WHITE);
    }
}

