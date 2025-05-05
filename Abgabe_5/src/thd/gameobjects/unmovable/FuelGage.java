package thd.gameobjects.unmovable;

import thd.game.managers.GamePlayManager;
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

public class FuelGage extends GameObject {

    /**
     * Creates a fuel gage object and it starts full.
     *
     * @param gameView        The GameView object where the fuel gage will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public FuelGage(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        position.updateCoordinates((GameView.WIDTH / 2.0), GameView.HEIGHT - 60);
        size = 30;
        rotation = 0;
        width = 170;
        height = 45;
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
        gameView.addRectangleToCanvas(position.getX(), position.getY() + 8, width, height, 3, false, Color.BLACK);
        gameView.addTextToCanvas("E", position.getX() + 5, position.getY() + 19, size, true, Color.BLACK, 0, "font.ttf");
        gameView.addBlockImageToCanvas(FractionalLineBlockImage.FRACTIONAL_LINE_BLOCK_IMAGE, position.getX() + 61, position.getY() + 22, 4, 0);
        gameView.addTextToCanvas("1", position.getX() + 75, position.getY() + 16, 20, true, Color.BLACK, 0, "font.ttf");
        gameView.addTextToCanvas("2", position.getX() + 91, position.getY() + 30, 20, true, Color.BLACK, 0, "font.ttf");
        gameView.addTextToCanvas("F", position.getX() + 152, position.getY() + 19, size, true, Color.BLACK, 0, "font.ttf");
        gameView.addRectangleToCanvas(position.getX() + 10, position.getY() + 10, 5, 10, 1, true, Color.BLACK);
        gameView.addRectangleToCanvas(position.getX() + 156, position.getY() + 10, 5, 10, 1, true, Color.BLACK);
        gameView.addRectangleToCanvas(position.getX() + 85, position.getY() + 10, 3, 7, 1, true, Color.BLACK);
    }


}
