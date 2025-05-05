package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

import java.awt.*;

/**
 * Represents a yellow/red filled ball which appears random in {@link GameView}.
 * He targets the white oval object until he has the same {@link Position}.
 *
 * @see GameView
 * @see Position
 */

public class RandomBall extends GameObject {
    private final QuadraticMovementPattern quadraticMovementPattern;

    /**
     * Creates a new random ball object with random position, speed, size and other properties.
     *
     * @param gameView The gaming window where the random ball will be displayed.
     */

    public RandomBall(GameView gameView) {
        super(gameView);
        quadraticMovementPattern = new QuadraticMovementPattern();
        position.updateCoordinates(new RandomMovementPattern().startPosition());
        targetPosition.updateCoordinates(quadraticMovementPattern.nextPosition());
        speedInPixel = 4;
        size = 50;
        rotation = 0;
        width = 50;
        height = 50;
    }

    @Override
    public String toString() {
        return "RandomBall: " + position;
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */

    @Override
    public void updatePosition() {
        boolean pauseForMovement = true;
        if (gameView.timer(3000, 0, this)) {
            speedInPixel++;
        }
        if (!gameView.timer(1000, 4000, this)) {
            pauseForMovement = false;
        }
        if (pauseForMovement) {
            position.moveToPosition(targetPosition, speedInPixel);
        }
        if (position.similarTo(targetPosition)) {
            targetPosition.updateCoordinates(quadraticMovementPattern.nextPosition());
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
        gameView.addOvalToCanvas(position.getX(), position.getY(), width, height, 2, true, Color.YELLOW);
        if (gameView.gameTimeInMilliseconds() >= 5000) {
            gameView.addOvalToCanvas(position.getX(), position.getY(), width, height, 2, true, Color.RED);
        }
        gameView.addOvalToCanvas(targetPosition.getX(), targetPosition.getY(), width, height, 2, false, Color.WHITE);

    }

}

