package thd.gameobjects.movable;

import thd.game.level.Difficulty;
import thd.game.level.Level;
import thd.game.managers.GamePlayManager;
import thd.gameobjects.base.*;
import thd.gameobjects.unmovable.SceneryLeft;
import thd.gameobjects.unmovable.SceneryRight;
import thd.game.utilities.GameView;

/**
 * Represents a satellite in the {@link GameView} windows.
 * This object is mainly decorative, but it does move downward through the screen,
 * outside the River in {@link SceneryLeft} or {@link SceneryRight}.
 *
 * @see GameView
 * @see Position
 */
public class Satellite extends GameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {
    private boolean spawnedLeft;

    /**
     * Creates a satellite object that appears at a defined start position on the
     * left or right side of the {@link GameView} field.
     *
     * @param gameView        The gaming window where the fuel-item will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public Satellite(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        if (Level.difficulty == Difficulty.EASY) {
            speedInPixel = 1.8;
        } else {
            speedInPixel = 2.2;
        }
        size = 0.08;
        rotation = 0;
        width = 30;
        height = 90;
        distanceToBackground = 2;
        spawnedLeft = false;
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
        if (spawnedLeft) {
            gameView.addImageToCanvas("satellite.png", position.getX(), position.getY(), size, 0);
        } else {
            gameView.addImageToCanvas("satellite_left.png", position.getX(), position.getY(), size, 0);
        }


    }

    /**
     * Determines whether the object is display on the left or right side,
     * based on the colum from the GameWorldManger.
     *
     * @param spawnedLeft true if the object spawns on the left side, false if right side.
     */
    public void initializeTheSpawnPointForSatellite(boolean spawnedLeft) {
        this.spawnedLeft = spawnedLeft;
    }


    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
