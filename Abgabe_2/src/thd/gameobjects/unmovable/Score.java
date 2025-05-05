package thd.gameobjects.unmovable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.Position;

import java.awt.*;

/**
 * Shows the number of points that the player gets from the enemies.
 * The score is visible at the bottom center in {@link GameView}.
 */

public class Score {
    private final GameView gameView;
    private final Position position;
    private final double size;
    private final double rotation;
    private final double width;
    private final double height;
    private final int scorePoints;


    /**
     * Creates a new score display.
     *
     * @param gameView The GameView object where the score display is rendered.
     */

    public Score(GameView gameView) {
        this.gameView = gameView;
        size = 30;
        rotation = 0;
        width = 70;
        height = 670;
        position = new Position(GameView.WIDTH - width - 5, -12);
        scorePoints = 1500;
    }

    @Override
    public String toString() {
        return "Score: " + position;
    }

    /**
     * Adds the gaming object to the game canvas in {@link GameView}
     * by placing an image or shape (oval, rectangle, etc.) at the respective position.
     *
     * @see GameView
     * @see Position
     */
    public void addToCanvas() {
        gameView.addTextToCanvas(String.valueOf(scorePoints), position.getX(), position.getY(),
                size, true, Color.WHITE, rotation);
    }


}
