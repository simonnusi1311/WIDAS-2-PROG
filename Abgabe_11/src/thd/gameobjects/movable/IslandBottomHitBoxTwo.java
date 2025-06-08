package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;

import java.awt.*;

/**
 * Represents a BigIsland in the {@link GameView} window.
 * this island is a bigger obstacle in the center of the Game.
 *
 * @see GameView
 * @see Position
 */

class IslandBottomHitBoxTwo extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {

    /**
     * Creates a new big island in game view.
     *
     * @param gameView        The gaming window where the Bridge will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    IslandBottomHitBoxTwo(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        speedInPixel = 2.2;
        size = 0.50;
        rotation = 0;
        width = 180;
        height = 1500;
        hitBoxOffsets(-32, -30, -130, -1430);
        distanceToBackground = 1;
    }

    @Override
    public void updateStatus() {
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    @Override
    public void updatePosition() {
        //position.down(speedInPixel);
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
        gameView.addOvalToCanvas(position.getX(), position.getY(), 50, 50, 4, true, Color.GREEN.darker());
    }


    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
