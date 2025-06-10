package thd.gameobjects.unmovable;

import thd.game.managers.GamePlayManager;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.game.utilities.GameView;
import java.awt.*;

/**
 * Shows the Scenery on the right border of the game.
 * It is responsible for the game field and the environment outside the river.
 *
 * @see GameView
 * @see Position
 */

public class SceneryRight extends CollidingGameObject {

    /**
     * Creates the scenery with one border on the left.
     *
     * @param gameView        The GameView object where the scenery will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public SceneryRight(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        position.updateCoordinates(GameView.WIDTH - 220, 0);
        rotation = 0;
        width = 220;
        height = 654;
        size = 40;
        hitBoxOffsets(120, 0, 0, 0);
        distanceToBackground = 0;
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {

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
        gameView.addRectangleToCanvas(GameView.WIDTH - 100, position.getY(), width, height, 3, true, Color.GREEN.darker());
    }
}
