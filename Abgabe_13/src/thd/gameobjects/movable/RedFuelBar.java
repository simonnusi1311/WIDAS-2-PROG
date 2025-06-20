package thd.gameobjects.movable;

import thd.game.level.Difficulty;
import thd.game.level.Level;
import thd.game.managers.GamePlayManager;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.unmovable.FuelGage;
import thd.gameobjects.base.Position;
import thd.game.utilities.GameView;

import java.awt.*;

/**
 * Represents a red rectangle in the {@link GameView} window.
 * The rectangle is a movable object in the {@link FuelGage}
 * and is responsible for the fuel gage of the {@link JetFighter}.
 * Its position is defined by the x and y coordinates from {@link Position}
 *
 * @see GameView
 * @see Position
 */

public class RedFuelBar extends GameObject {
    private boolean jetHitsFuelItem;
    private boolean alarmSoundIsPlaying;
    private int id;
    private double pixelForBoostToRight;

    /**
     * Creates a new red rectangle object in the {@link FuelGage}
     * which is responsible for displaying the fuel status.
     *
     * @param gameView        The gaming window where the rectangle will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public RedFuelBar(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        position.updateCoordinates((GameView.WIDTH / 2.0) + 155, GameView.HEIGHT - 60);
        if (Level.difficulty == Difficulty.EASY) {
            speedInPixel = 0.02;
            pixelForBoostToRight = 0.65;
        } else {
            speedInPixel = 0.05;
            pixelForBoostToRight = 0.60;
        }
        width = 8;
        height = 25;
        distanceToBackground = 6;
    }

    /**
     * Reset the RedFuelBar after the restart.
     */
    void resetRedFuelBar() {
        position.updateCoordinates((GameView.WIDTH / 2.0) + 155, GameView.HEIGHT - 60);
        if (Level.difficulty == Difficulty.EASY) {
            speedInPixel = 0.02;
            pixelForBoostToRight = 0.65;
        } else {
            speedInPixel = 0.05;
            pixelForBoostToRight = 0.60;
        }
        jetHitsFuelItem = false;
    }

    /**
     * Sets whether the jet has hit a fuel item.
     *
     * @param jetHitsFuelItem true if jet hits item, false if not
     */
    public void setJetHitsFuelItem(boolean jetHitsFuelItem) {
        this.jetHitsFuelItem = jetHitsFuelItem;
    }

    @Override
    protected boolean gameObjectHitsLeftBoundary() {
        return position.getX() <= (GameView.WIDTH / 2.0) + 8;
    }

    @Override
    protected boolean gameObjectHitsRightBoundary() {
        return position.getX() >= (GameView.WIDTH / 2.0) + 155;
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    @Override
    public void updatePosition() {
        stopMovingIfHitsLeftBoundary();
        stopMovingIfHitsRightBoundary();
        if (jetHitsFuelItem) {
            position.right(speedInPixel + pixelForBoostToRight);
        } else if (!gamePlayManager.isJetInRespawn()) {
            position.left(speedInPixel);
        }
    }

    @Override
    public void updateStatus() {
        double limit = (GameView.WIDTH / 2.0) + 50;

        if (gamePlayManager.isJetInRespawn()) {
            if (alarmSoundIsPlaying) {
                gameView.stopSound(id);
            }
            alarmSoundIsPlaying = false; // ← immer zurücksetzen!
            return;
        }

        if (position.getX() <= limit) {
            if (!alarmSoundIsPlaying) {
                id = gameView.playSound("warning.wav", true);
                alarmSoundIsPlaying = true;
            }
        } else {
            if (alarmSoundIsPlaying) {
                gameView.stopSound(id);
                alarmSoundIsPlaying = false;
            }
        }
    }

    private void stopMovingIfHitsLeftBoundary() {
        if (gameObjectHitsLeftBoundary()) {
            speedInPixel = 0;
            gamePlayManager.fuelIsEmpty();
        }
    }

    private void stopMovingIfHitsRightBoundary() {
        if (gameObjectHitsRightBoundary()) {
            jetHitsFuelItem = false;
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
        gameView.addRectangleToCanvas(position.getX(), position.getY() + 10, width, height, 1, true, Color.RED.darker());
    }
}
