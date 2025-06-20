package thd.game.managers;

import thd.game.level.Difficulty;
import thd.game.level.Level;
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
    protected int points;
    protected int lives;
    private Tank activeTank;
    private boolean levelReady;

    protected GamePlayManager(GameView gameView) {
        super(gameView);
        gameObjectManager = new GameObjectManager();
        random = new Random();
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
            if (Level.difficulty == Difficulty.EASY) {
                moveWorldUp(1.8);
            } else {
                moveWorldUp(2.2);
            }
            gameView.stopAllSounds();
        }
    }

    /**
     * When the fuel from {@link JetFighter} is empty, it
     * immediately reduces the JetFighter's life to zero.
     */
    public void fuelIsEmpty() {
        if (lives > 0) {
            lifeLost();
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
     * Returns the current lives from the JetFighter.
     *
     * @return the lives that the {@link JetFighter} has.
     */
    public int getLives() {
        return lives;
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
     * Counter for the new level section if bridge gets destroyed.
     */
    public void bridgeDestroyed() {
        levelCounter();
        if (statusBar.getLevelSection() == 13) {
            gameView.playSound("finale.wav", false);
        }
    }

    /**
     * This method is responsible for the next level.
     */
    public void finishedLevel() {
        levelReady = true;
    }

    protected boolean changeLevelSection() {
        if (levelReady) {
            levelReady = false;
            return true;
        }
        return false;
    }

    /**
     * Increase the level section in {@link StatusBar} by one.
     */
    private void levelCounter() {
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
        gameObjectManager.add(gameObject);
    }

    /**
     * Checks whether the jet fighter is currently in the respawn phase.
     *
     * @return true if the jet fighter is in an invincible state.
     */
    public boolean isJetInRespawn() {
        return jetFighter.isInvincible();
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
    }

    @Override
    protected void gameLoop() {
        super.gameLoop();
        gameObjectManager.gameLoop();
        gamePlayManagement();
    }
}
