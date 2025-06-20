package thd.gameobjects.movable;

import thd.game.level.Difficulty;
import thd.game.level.Level;
import thd.game.managers.GamePlayManager;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;
import thd.game.utilities.GameView;

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
        if (Level.difficulty == Difficulty.EASY) {
            speedInPixel = 1.8;
        } else {
            speedInPixel = 2.2;
        }
        size = 0.50;
        rotation = 0;
        width = 40;
        height = 100;
        hitBoxOffsets(-25, -35, 13, -30);
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
        gameView.addImageToCanvas("small_island.png", position.getX() - 50, position.getY() - 50, 0.1, 0);
    }


    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
