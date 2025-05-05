package thd.gameobjects.unmovable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

import java.awt.*;

/**
 * Shows the life counter that the player has in River Raid.
 * The life counter is visible at the bottom-right center in {@link GameView}.
 *
 * @see GameView
 * @see Position
 */

public class LifeCounter extends GameObject {
    private int lifeCounter;

    /**
     * Creates a new life counter which starts at 3, which means that the player has
     * 3 trys before its game over.
     *
     * @param gameView        The GameView object where the life counter will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public LifeCounter(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        position.updateCoordinates((GameView.WIDTH / 2.0) + 260, GameView.HEIGHT - 60);
        size = 40;
        rotation = 0;
        width = 70;
        height = 670;
        lifeCounter = 3;
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
        gameView.addTextToCanvas(String.valueOf(lifeCounter), position.getX(), position.getY() + 9,
                size, true, Color.BLACK, rotation, "font.ttf");
        gameView.addImageToCanvas("jet_fighter.png", position.getX() + 30, position.getY() + 15, 0.65, 0);
    }


}
