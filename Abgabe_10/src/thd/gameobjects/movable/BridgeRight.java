package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;

import java.awt.*;


/**
 * Shows the street on the right side of the bridge.
 * It separates the current level with the next section.
 *
 * @see GameView
 * @see Position
 */

public class BridgeRight extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {

    /**
     * Creates the Bridge street on the right side.
     *
     * @param gameView        The GameView object where the scenery will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public BridgeRight(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        position.updateCoordinates((GameView.WIDTH / 2.0) + 95, -74);
        speedInPixel = 2.2;
        rotation = 0;
        width = (GameView.WIDTH / 2.0) - 42;
        height = 65;
        size = 40;
        hitBoxOffsets(0, 0, -3, 0);
        distanceToBackground = 2;
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
        gameView.addRectangleToCanvas(position.getX(), position.getY(), width, height, 3, true, Color.GRAY.brighter());
        gameView.addRectangleToCanvas(position.getX(), position.getY() + 28, width, 3, 3, true, Color.YELLOW.darker());
        gameView.addRectangleToCanvas(position.getX(), position.getY() + 37, width, 3, 3, true, Color.YELLOW.darker());
    }

    @Override
    public boolean tryToActivate(JetFighter info) {
        return position.getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
