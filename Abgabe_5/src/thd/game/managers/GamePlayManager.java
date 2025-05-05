package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.movable.*;

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
    private int currentNumberOfVisibleSquares;
    private final Random random;

    protected GamePlayManager(GameView gameView) {
        super(gameView);
        gameObjectManager = new GameObjectManager();
        random = new Random();
    }

    /**
     * Adds a game object to the game. The object will be managed
     * by the GameObjectManager.
     *
     * @param gameObject the game objects to spawn.
     */

    public void spawnGameObject(GameObject gameObject) {
        gameObjectManager.add(gameObject);
        if (gameObject instanceof Square) {
            currentNumberOfVisibleSquares++;
        }
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
        if (gameView.timer(1000, 0, this) && currentNumberOfVisibleSquares < 5) {
            spawnGameObject(new Square(gameView, this));
        }
        if (gameView.timer(18000, 0, this)) {
            spawnGameObject(new Bridge(gameView, this));
            spawnGameObject(new Tank(gameView, this));
        }
        if (gameView.timer(random.nextInt(1500, 4000), 0, this)) {
            randomSpawnForTheEnemies();
        }
        if (gameView.timer(random.nextInt(10000, 15000), 0, this)) {
            spawnGameObject(new FuelItem(gameView, this));
        }
        if (gameView.timer(random.nextInt(6000, 12000), 0, this)) {
            spawnGameObject(new GreyJet(gameView, this));
        }
        if (gameView.timer(random.nextInt(7000, 12000), 0, this)) {
            spawnGameObject(new Tank(gameView, this));
        }
    }

    private void randomSpawnForTheEnemies() {
        var ship = new Ship(gameView, this);
        var helicopter = new Helicopter(gameView, this);
        var balloon = new Balloon(gameView, this);
        GameObject[] array = new GameObject[]{ship, helicopter, balloon};
        int randomNumber = random.nextInt(3);
        spawnGameObject(array[randomNumber]);
    }

    @Override
    protected void gameLoop() {
        super.gameLoop();
        gameObjectManager.gameLoop();
        gamePlayManagement();
    }
}
