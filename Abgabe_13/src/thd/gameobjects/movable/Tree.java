package thd.gameobjects.movable;

import thd.game.level.Difficulty;
import thd.game.level.Level;
import thd.game.managers.GamePlayManager;
import thd.gameobjects.base.*;
import thd.gameobjects.unmovable.SceneryLeft;
import thd.gameobjects.unmovable.SceneryRight;
import thd.game.utilities.GameView;

import java.util.Random;

/**
 * Represents a tree in the {@link GameView} windows.
 * This object is mainly decorative, but it does move downward through the screen,
 * outside the River in {@link SceneryLeft} or {@link SceneryRight}.
 *
 * @see GameView
 * @see Position
 */
public class Tree extends GameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {

    private Random random;
    private int randomNumberForSpawn;

    /**
     * Creates a new tree object that appears at a defined start position on the
     * left or right side of the {@link GameView} field.
     *
     * @param gameView        The gaming window where the fuel-item will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public Tree(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        if (Level.difficulty == Difficulty.EASY) {
            speedInPixel = 1.8;
        } else {
            speedInPixel = 2.2;
        }
        size = 0.15;
        rotation = 0;
        width = 30;
        height = 90;
        distanceToBackground = 2;
        random = new Random();
        randomNumberForSpawn = random.nextInt(3);
    }


    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            gamePlayManager.destroyGameObject(this);
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
        spawnARandomTree();
        gameView.addImageToCanvas("grass_1.png", position.getX() + 110, position.getY() + 100, 1, 0);
        gameView.addImageToCanvas("grass_2.png", position.getX() + 40, position.getY() + 70, 1, 0);
        gameView.addImageToCanvas("grass_3.png", position.getX() + 30, position.getY() + 90, 1, 0);
    }

    private void spawnARandomTree() {
        switch (randomNumberForSpawn) {
            case 1:
                gameView.addImageToCanvas("tree_1.png", position.getX(), position.getY(), size, 0);
                break;
            case 2:
                gameView.addImageToCanvas("tree_2.png", position.getX(), position.getY(), size, 0);
                break;
            case 3:
                gameView.addImageToCanvas("tree_3.png", position.getX(), position.getY(), size, 0);
                break;
            default:
                gameView.addImageToCanvas("tree_1.png", position.getX(), position.getY(), size, 0);
        }
    }

    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
