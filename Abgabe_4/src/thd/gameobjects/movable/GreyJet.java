package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * Represents an enemy jet in grey in the {@link GameView} window.
 * The jet is a movable object that has different properties,
 * such as speed and size. The position is defined by the
 * x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class GreyJet extends GameObject {
    private final GreyJetMovementPattern greyJetMovementPattern;

    /**
     * Creates a new grey jet object with position, speed and size.
     * The jet appears random on the left or right upper center in {@link GameView}.
     *
     * @param gameView The gaming window where the grey jet will be displayed.
     */

    public GreyJet(GameView gameView) {
        super(gameView);
        greyJetMovementPattern = new GreyJetMovementPattern();
        position.updateCoordinates(greyJetMovementPattern.startPosition());
        speedInPixel = 4;
        size = 0.80;
        rotation = 0;
        width = 0;
        height = 0;
        randomNumberForTheSpawn = (int) (Math.random() * 7000) + 1000;
    }

    @Override
    public String toString() {
        return "Flugzeug: " + position;
    }

    @Override
    protected boolean gameObjectHitsLeftBoundary() {
        return position.getX() <= -10;
    }

    @Override
    protected boolean gameObjectHitsRightBoundary() {
        return position.getX() >= GameView.WIDTH;
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
            if (greyJetMovementPattern.movingRight) {
                position.right(speedInPixel);
            } else {
                position.left(speedInPixel);
            }
            if (gameObjectHitsRightBoundary()) {
                position.updateCoordinates(0, position.getY());
            } else if (gameObjectHitsLeftBoundary()) {
                position.updateCoordinates(GameView.WIDTH, position.getY());
            }
            position.down(1);
        }
        if (gameObjectHitsLowerBoundary()) {
            position.updateCoordinates(greyJetMovementPattern.nextPosition());
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
        if (greyJetMovementPattern.movingRight) {
            gameView.addImageToCanvas("grey_jet.png", position.getX(), position.getY(), size, 0);
        } else {
            gameView.addImageToCanvas("grey_jet_left.png", position.getX(), position.getY(), size, 0);
        }
    }
}
