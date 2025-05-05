package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.unmovable.ShootFromPlayerBlockImages;

import java.awt.*;

/**
 * Represents the main playable jet fighter in the {@link GameView} window.
 * The jet fighter can move horizontally and its position is defined by the
 * x and y coordinates from {@link Position}
 * <p>
 * The jet fighter also can shoot {@link ShootFromPlayerBlockImages} on the enemies.
 *
 * @see GameView
 * @see Position
 */

public class JetFighter extends GameObject {
    private boolean shotInProgress;

    /**
     * Creates a new jet fighter object with position, speed, size and other properties.
     *
     * @param gameView The gaming window where the jet fighter will be displayed.
     */

    public JetFighter(GameView gameView) {
        super(gameView);
        position.updateCoordinates(GameView.WIDTH / 2.0, 600);
        speedInPixel = 1;
        size = 0.80;
        rotation = 0;
        width = 0;
        height = 0;
        shotInProgress = false;
    }

    /**
     * The jet moves the left by the given number of pixels.
     *
     * @see Position
     */
    public void left() {
        position.left(3);
    }

    /**
     * The jet moves the right by the given number of pixels.
     *
     * @see Position
     */
    public void right() {
        position.right(3);
    }

    /**
     * The jet shoots.
     */
    public void shoot() {
        shotInProgress = true;
    }

    /**
     * The jet stop shooting.
     */
    public void stopShooting() {
        shotInProgress = false;
    }

    @Override
    public String toString() {
        return "JetFighter: " + position;
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    @Override
    public void updatePosition() {
    }

    @Override
    public void updateStatus() {
        if (gameView.timer(5000, 0, this)) {
            size++;
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
        if (shotInProgress) {
            gameView.addTextToCanvas("X", position.getX() + 9, position.getY(), 40, true, Color.BLACK, 0, "font.ttf");
        } else {
            gameView.addImageToCanvas("jet_fighter.png", position.getX(), position.getY(), size, 0);
        }

    }


}
