package thd.gameobjects.unmovable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.movable.JetFighter;

import java.awt.*;

/**
 * Shows the fuel gage from the playable {@link JetFighter}.
 * The fuel gage is visible at the bottom center in {@link GameView}.
 *
 * @see GameView
 * @see Position
 */

public class StatusBar extends GameObject {

    /**
     * Creates a fuel gage object and it starts full.
     *
     * @param gameView The GameView object where the fuel gage will be displayed.
     */

    public StatusBar(GameView gameView) {
        super(gameView);
        position.updateCoordinates(0, GameView.HEIGHT - 65);
        rotation = 0;
        width = GameView.WIDTH;
        height = 65;
        size = 40;
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
        gameView.addTextToCanvas("1", position.getX() + 1100, position.getY() + 14, size, true, Color.BLACK, 0, "font.ttf");
    }


}
