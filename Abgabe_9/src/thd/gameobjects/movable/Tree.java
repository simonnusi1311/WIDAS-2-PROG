package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.*;
import thd.gameobjects.unmovable.SceneryLeft;
import thd.gameobjects.unmovable.SceneryRight;

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
        speedInPixel = 1.3;
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
