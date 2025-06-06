package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;

import java.awt.*;

/**
 * Represents the left-side moving scenery border in the {@link GameView}.
 * This object simulates a scrolling ground element on the left edge of the screen.
 *
 * @see GameView
 * @see Position
 */

public class MovableSceneryLeft extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {

    /**
     * Creates a new instance of the left-side movable scenery object.
     *
     * @param gameView        The gaming window where the helicopter will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public MovableSceneryLeft(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        speedInPixel = 2.2;
        size = 0.80;
        rotation = 0;
        width = 800;
        height = 50;
        hitBoxOffsets(0, 0, -10, 0);
        distanceToBackground = 1;
    }

    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }
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
        gameView.addRectangleToCanvas(position.getX(), position.getY(), width, height, 3, true, Color.GREEN.darker());
        gameView.addImageToCanvas("grass_edge_right.png", position.getX() + 792, position.getY(), 1.42, 0);
    }

    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}

