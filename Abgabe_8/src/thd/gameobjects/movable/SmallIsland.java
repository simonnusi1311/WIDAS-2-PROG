package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;

import java.awt.*;

/**
 * Represents a SmallIsland in the {@link GameView} window.
 * this island is a smaller obstacle in the center of the Game.
 *
 * @see GameView
 * @see Position
 */

public class SmallIsland extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {

    /**
     * Creates a new big island in game view.
     *
     * @param gameView        The gaming window where the Bridge will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public SmallIsland(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        speedInPixel = 1.3;
        size = 0.50;
        rotation = 0;
        width = 40;
        height = 100;
        hitBoxOffsets(-20, -45, 0, 0);
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
        gameView.addOvalToCanvas(position.getX(), position.getY(), width, height, 4, true, Color.GREEN.darker());
    }


    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
