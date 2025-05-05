package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

import java.awt.*;

/**
 * A green ball that targets a {@link RandomBall} in {@link GameView}.
 *
 * @see RandomBall
 * @see Position
 */

public class FollowerBall extends GameObject {
    private final RandomBall followMe;

    /**
     * Creates a new follower ball object with default (0, 0) coordinates, speed, size and other properties.
     * He targets the {@link RandomBall}.
     *
     * @param gameView The gaming window where the random ball will be displayed.
     * @param followMe The RandomBall which is the target.
     */

    public FollowerBall(GameView gameView, RandomBall followMe) {
        super(gameView);
        this.followMe = followMe;
        speedInPixel = 3;
        size = 50;
        width = 50;
        height = 50;
    }

    @Override
    public String toString() {
        return "FollowerBall: " + position;
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */

    @Override
    public void updatePosition() {
        targetPosition.updateCoordinates(followMe.getPosition());
        position.moveToPosition(targetPosition, speedInPixel);
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
        gameView.addOvalToCanvas(position.getX(), position.getY(), width, height, 2, true, Color.GREEN);
    }

}

