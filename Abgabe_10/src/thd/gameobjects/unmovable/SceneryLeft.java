package thd.gameobjects.unmovable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;

import java.awt.*;

/**
 * Shows the Scenery on the left border of the game.
 * It is responsible for the game field and the environment outside the river.
 *
 * @see GameView
 * @see Position
 */

public class SceneryLeft extends CollidingGameObject {

    /**
     * Creates the scenery with one border on the left.
     *
     * @param gameView        The GameView object where the scenery will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public SceneryLeft(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        position.updateCoordinates(0, 0);
        rotation = 0;
        width = 100;
        height = 654;
        size = 40;
        hitBoxOffsets(0, 0, -3, 0);
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
        gameView.addRectangleToCanvas(position.getX(), position.getY(), width, height, 3, true, Color.GREEN.darker());
    }

}
