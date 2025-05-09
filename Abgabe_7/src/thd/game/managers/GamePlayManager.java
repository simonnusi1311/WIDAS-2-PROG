package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.StatusBar;

import java.util.Random;

/**
 * Manages the main gameplay logic and its interaction with the different
 * {@link GameObject}.
 *
 * @see GameObject
 * @see GameView
 */
public class GamePlayManager extends WorldShiftManager {
    private final GameObjectManager gameObjectManager;
    private final Random random;
    private static final int LIVES = 3;
    protected int points;
    protected int lives;
    private int levelCounterForBridge;
    private Tank activeTank;
    private boolean bridgeIsActive;

    protected GamePlayManager(GameView gameView) {
        super(gameView);
        gameObjectManager = new GameObjectManager();
        random = new Random();
        lives = LIVES;
        bridgeIsActive = false;
    }

    /**
     * Reduces the {@link JetFighter}'s life, when he collides with
     * a another gaming object or if he gets hit by the tanks shot.
     */
    public void lifeLost() {
        lives--;
        lifeCounter.setLifeCounter(lives);
        if (lives == 0) {
            destroyGameObject(jetFighter);
        }
    }

    /**
     * When the fuel from {@link JetFighter} is empty, it
     * immediately reduces the JetFighter's life to zero.
     */
    public void fuelIsEmpty() {
        lifeCounter.setLifeCounter(0);
        destroyGameObject(jetFighter);
    }

    /**
     * Adds points to the Score if {@link JetFighter} destroys enemies.
     * The amount of points depends on the type of enemy destroyed.
     *
     * @param points the number of points from the enemy.
     */
    public void addPoints(int points) {
        this.points += points;
        score.setScorePoints(this.points);
    }

    /**
     * Fills up the {@link JetFighter}'s fuel level,
     * that the player can continue his flight.
     *
     * @see RedFuelBar the display in the Fuel Gage.
     */
    public void fillUpTheFuelGage() {
        redFuelBar.setJetHitsFuelItem(true);
    }

    /**
     * Stops filling up the {@link JetFighter}'s fuel level,
     * if the player don't collide with {@link FuelItem}.
     *
     * @see RedFuelBar the display in the Fuel Gage.
     */
    public void stopFuelUpTheFuelGage() {
        redFuelBar.setJetHitsFuelItem(false);
    }

    /**
     * Increase the level section in {@link StatusBar} by one.
     */
    public void levelCounter() {
        statusBar.increaseLevelCounterByOne();
    }

    /**
     * Adds a game object to the game. The object will be managed
     * by the GameObjectManager.
     *
     * @param gameObject the game objects to spawn.
     */
    @Override
    public void spawnGameObject(GameObject gameObject) {
        super.spawnGameObject(gameObject);
        if (gameObject instanceof Bridge) {
            levelCounterForBridge++;
        }
        gameObjectManager.add(gameObject);
    }

    /**
     * Removes a game object from the game. The object will be managed
     * by the GameObjectManager.
     *
     * @param gameObject the game objects to remove.
     */
    @Override
    public void destroyGameObject(GameObject gameObject) {
        super.destroyGameObject(gameObject);
        if (gameObject == activeTank) {
            activeTank = null;
        }
        gameObjectManager.remove(gameObject);
    }

    @Override
    protected void destroyAllGameObjects() {
        super.destroyAllGameObjects();
        gameObjectManager.removeAll();
    }

    private void gamePlayManagement() {
        setBridgeToActiveStatus();
        if (gameView.timer(15000, 0, this)) {
            spawnBridgeBorders();
        }

        if (!bridgeIsActive && gameView.timer(random.nextInt(500, 2000), 0, this)) {
            spawnRandomEnemy();
        }
        if (!bridgeIsActive && gameView.timer(random.nextInt(4000, 8000), 0, this)) {
            spawnGameObject(new FuelItem(gameView, this));
        }
        if (gameView.timer(random.nextInt(3000, 6000), 0, this)) {
            spawnGameObject(new GreyJet(gameView, this));
        }
        if (gameView.timer(random.nextInt(5000, 8000), 0, this) && activeTank == null) {
            spawnGameObject(new Tank(gameView, this));
        }
    }

    private void spawnRandomEnemy() {
        var ship = new Ship(gameView, this);
        var helicopter = new Helicopter(gameView, this);
        var balloon = new Balloon(gameView, this);
        GameObject[] randomGameObject = new GameObject[]{ship, helicopter, balloon};
        int randomNumber = random.nextInt(3);
        spawnGameObject(randomGameObject[randomNumber]);
    }

    private void spawnBridgeBorders() {
        bridgeIsActive = true;
        var bridge = new Bridge(gameView, this);
        var bridgeLeft = new BridgeLeft(gameView, this);
        var bridgeRight = new BridgeRight(gameView, this);
        spawnGameObject(bridge);
        spawnGameObject(bridgeLeft);
        spawnGameObject(bridgeRight);
        jetFighter.addPathDecisionObjects(bridgeLeft);
        jetFighter.addPathDecisionObjects(bridgeRight);
        bridge.setCounterForLevel(levelCounterForBridge);
        spawnTankOnBridgeStreet();
    }

    private void spawnTankOnBridgeStreet() {
        Tank tank = new Tank(gameView, this);
        tank.getPosition().updateCoordinates(tank.getPosition().getX(), tank.getPosition().getY() - 30);
        spawnGameObject(tank);
    }

    private void setBridgeToActiveStatus() {
        if (bridgeIsActive && gameView.timer(3000, 0, this)) {
            bridgeIsActive = false;
        }
    }

    @Override
    protected void gameLoop() {
        super.gameLoop();
        gameObjectManager.gameLoop();
        gamePlayManagement();
    }
}
