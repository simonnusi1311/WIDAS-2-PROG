package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
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
    private int counterForLevel;


    /**
     * Creates a new bridge in game view.
     *
     * @param gameView        The gaming window where the Bridge will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public Bridge(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        bridgeMovementPattern = new BridgeMovementPattern();
        position.updateCoordinates(bridgeMovementPattern.startPosition());
        speedInPixel = 1.3;
        size = 0.50;
        rotation = 0;
        width = 0;
        height = 0;
        counterForLevel = 1;
    }

    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            counterForLevel++;
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
        gameView.addImageToCanvas("bridge.png", position.getX(), position.getY(), size, 0);
        gameView.addTextToCanvas(String.format("%04d", counterForLevel), position.getX() + 26, position.getY() + 24, 35, true, Color.BLACK, 0, "font.ttf");
    }


}
