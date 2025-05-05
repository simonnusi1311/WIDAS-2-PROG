package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

import java.awt.*;

/**
 * Represents a bridge in the {@link GameView} window.
 * The bridge is a movable object and is a border to the
 * next level section. {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class Bridge extends GameObject {
    private final BridgeMovementPattern bridgeMovementPattern;

    /**
     * Creates a new bridge in game view.
     *
     * @param gameView The gaming window where the balloon will be displayed.
     */

    public Bridge(GameView gameView) {
        super(gameView);
        bridgeMovementPattern = new BridgeMovementPattern();
        position.updateCoordinates(bridgeMovementPattern.startPosition());
        speedInPixel = 1;
        size = 0.50;
        rotation = 0;
        width = 0;
        height = 0;
    }

    @Override
    protected boolean readyToSpawn() {
        return gameView.timer(12000, 200000, this);
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    @Override
    public void updatePosition() {
        if (readyToSpawn()) {
            position.down(speedInPixel);
        }
        if (gameObjectHitsLowerBoundary()) {
            position.updateCoordinates(bridgeMovementPattern.nextPosition());
        }
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
        gameView.addImageToCanvas("bridge.png", position.getX(), position.getY(), size, 0);
        gameView.addTextToCanvas("0002", position.getX() + 26, position.getY() + 24, 35, true, Color.BLACK, 0, "font.ttf");
    }


}
