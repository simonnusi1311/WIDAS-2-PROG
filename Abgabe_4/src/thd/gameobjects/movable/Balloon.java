package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * Represents an enemy balloon in the {@link GameView} window.
 * The balloon is a movable object that has different properties,
 * such as speed and size. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class Balloon extends GameObject {
    private final BalloonMovementPattern balloonMovementPattern;

    /**
     * Creates a new balloon object with random position, speed, size and other properties.
     *
     * @param gameView The gaming window where the balloon will be displayed.
     */

    public Balloon(GameView gameView) {
        super(gameView);
        balloonMovementPattern = new BalloonMovementPattern();
        position.updateCoordinates(balloonMovementPattern.startPosition());
        speedInPixel = 1;
        size = 0.80;
        rotation = 0;
        width = 0;
        height = 0;
        randomNumberForTheSpawn = (int) (Math.random() * 5000);
    }

    @Override
    public String toString() {
        return "Balloon: " + position;
    }

    @Override
    protected boolean gameObjectHitsLeftBoundary() {
        return position.getX() <= 320;
    }

    @Override
    protected boolean gameObjectHitsRightBoundary() {
        return position.getX() >= 960;
    }

    @Override
    protected boolean readyToSpawn() {
        return gameView.timer(randomNumberForTheSpawn, 200000, this);
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    @Override
    public void updatePosition() {
        if (readyToSpawn()) {
            position.down(speedInPixel);
            if (balloonMovementPattern.movingRight) {
                position.right(speedInPixel + 1);
            }
            if (!balloonMovementPattern.movingRight) {
                position.left(speedInPixel + 1);
            }
            if (gameObjectHitsRightBoundary()) {
                balloonMovementPattern.movingRight = false;
            } else if (gameObjectHitsLeftBoundary()) {
                balloonMovementPattern.movingRight = true;
            }
        }
        if (gameObjectHitsLowerBoundary()) {
            position.updateCoordinates(balloonMovementPattern.nextPosition());
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
        gameView.addImageToCanvas("balloon.png", position.getX(), position.getY(), size, 0);
    }
}
