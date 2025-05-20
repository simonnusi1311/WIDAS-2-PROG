package thd.gameobjects.unmovable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.movable.JetFighter;

import java.awt.*;

/**
 * Shows the number of points that the player gets when he destroys an enemy.
 * The score is visible at the bottom-left center in {@link GameView}.
 *
 * @see GameView
 * @see Position
 */

public class Score extends GameObject {
    private int scorePoints;

    /**
     * Creates a new score display which starts at zero.
     *
     * @param gameView        The GameView object where the score display will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public Score(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        position.updateCoordinates((GameView.WIDTH / 3.0) - 25, GameView.HEIGHT - 51);
        size = 40;
        rotation = 0;
        width = 70;
        height = 670;
        distanceToBackground = 5;
    }

    /**
     * Method to increase the scorePoints if {@link JetFighter} destroys
     * an enemy.
     *
     * @param scorePoints the number of points that which is visible at the bottom.
     */
    public void setScorePoints(int scorePoints) {
        this.scorePoints = scorePoints;
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
        gameView.addTextToCanvas(String.valueOf(scorePoints), position.getX(), position.getY(),
                size, true, Color.BLACK, rotation, "font.ttf");
    }


}
