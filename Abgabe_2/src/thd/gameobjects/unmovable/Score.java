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
    private int scorePoints;


    /**
     * Creates a new score display.
     *
     * @param gameView The GameView object where die score display is rendered.
     */

    public Score(GameView gameView) {
        this.gameView = gameView;
        this.size = 30;
        this.rotation = 0;
        this.width = 146;
        this.height = -11;
        this.position = new Position(GameView.WIDTH - width, height);
        scorePoints = 0;
    }

    @Override
    public String toString() {
        return "Score: " + position;
    }

    /**
     * Adds a text (String) at the specified position in the game.
     * The text will be displayed in the respective color at the x and the y position in {@link GameView}.
     */
    public void addToCanvas() {
        gameView.addTextToCanvas(String.valueOf(scorePoints), position.getX(), position.getY(),
                size, true, Color.WHITE, rotation);
    }


}
