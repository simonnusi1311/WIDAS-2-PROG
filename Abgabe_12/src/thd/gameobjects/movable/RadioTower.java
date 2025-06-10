package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.gameobjects.base.*;
import thd.gameobjects.unmovable.SceneryLeft;
import thd.gameobjects.unmovable.SceneryRight;
import thd.game.utilities.GameView;

/**
 * Represents a radio tower in the {@link GameView} windows.
 * This object is mainly decorative, but it does move downward through the screen,
 * outside the River in {@link SceneryLeft} or {@link SceneryRight}.
 *
 * @see GameView
 * @see Position
 */
public class RadioTower extends GameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {

    /**
     * Creates a new radio tower object that appears at a defined start position on the
     * left or right side of the {@link GameView} field.
     *
     * @param gameView        The gaming window where the fuel-item will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public RadioTower(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        speedInPixel = 2.2;
        size = 0.09;
        rotation = 0;
        width = 30;
        height = 90;
        distanceToBackground = 2;
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
        gameView.addImageToCanvas("radio_tower.png", position.getX(), position.getY(), size, 0);

    }

    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
