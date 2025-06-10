package thd.gameobjects.unmovable;

import thd.game.managers.GamePlayManager;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.movable.Bridge;
import thd.game.utilities.GameView;
import java.awt.*;

/**
 * Shows status bar from the game and which level section {@link Bridge}.
 * The status bar is visible at the bottom right center in {@link GameView}.
 * It also contains the abbreviation P1, which means PlayerOne.
 *
 * @see GameView
 * @see Position
 */

public class StatusBar extends GameObject {
    private int levelSection;

    /**
     * Creates a status bar object, and it starts with level one.
     *
     * @param gameView        The GameView object where the fuel gage will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public StatusBar(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        position.updateCoordinates(0, GameView.HEIGHT - 65);
        rotation = 0;
        width = GameView.WIDTH;
        height = 65;
        size = 40;
        levelSection = 1;
        distanceToBackground = 4;
    }

    /**
     * Increase the level counter by one, when the player destroys the a {@link Bridge}.
     */
    public void increaseLevelCounterByOne() {
        levelSection++;
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
        gameView.addRectangleToCanvas(position.getX(), position.getY() + 2, width, 5, 3, true, Color.BLACK);
        gameView.addRectangleToCanvas(position.getX(), position.getY() + 10, width, height, 3, true, Color.LIGHT_GRAY);
        gameView.addTextToCanvas("P1", position.getX() + 100, position.getY() + 14, size, true, Color.BLACK, 0, "font.ttf");
        gameView.addTextToCanvas(String.valueOf(levelSection), position.getX() + 1100, position.getY() + 14, size, true, Color.BLACK, 0, "font.ttf");
    }


}
