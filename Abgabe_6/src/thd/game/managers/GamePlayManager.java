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
public class GamePlayManager extends UserControlledGameObjectPool {
    private final GameObjectManager gameObjectManager;
    private final Random random;
    private static final int LIVES = 3;
    protected int points;
    protected int lives;
    private int levelCounterForBridge;

    protected GamePlayManager(GameView gameView) {
        super(gameView);
        gameObjectManager = new GameObjectManager();
        random = new Random();
        lives = LIVES;
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
    public void fuelUpTheFuelGage() {
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

    public void spawnGameObject(GameObject gameObject) {
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

    public void destroyGameObject(GameObject gameObject) {
        gameObjectManager.remove(gameObject);
    }

    protected void destroyAllGameObjects() {
        gameObjectManager.removeAll();
    }

    private void gamePlayManagement() {
        var bridge = new Bridge(gameView, this);
        if (gameView.timer(18000, 0, this)) {
            spawnGameObject(bridge);
            bridge.setCounterForLevel(levelCounterForBridge);
            spawnGameObject(new Tank(gameView, this));
        }
        if (gameView.timer(random.nextInt(1500, 3000), 0, this)) {
            spawnRandomEnemy();
        }
        if (gameView.timer(random.nextInt(6000, 9000), 0, this)) {
            spawnGameObject(new FuelItem(gameView, this));
        }
        if (gameView.timer(random.nextInt(6500, 7000), 0, this)) {
            spawnGameObject(new GreyJet(gameView, this));
        }
        if (gameView.timer(random.nextInt(7000, 10000), 0, this)) {
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

    @Override
    protected void gameLoop() {
        super.gameLoop();
        gameObjectManager.gameLoop();
        gamePlayManagement();
    }
}
