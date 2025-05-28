package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the right border in the {@link GameView} window.
 * The border is a movable object that has different properties,
 * such as speed and size. The position is defined by the
 * x and y coordinates from {@link Position}.
 *
 * @see GameView
 * @see Position
 */

public class MovableSceneryRight extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {

    /**
     * Creates a new right border Scenery.
     *
     * @param gameView        The gaming window where the helicopter will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public MovableSceneryRight(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        speedInPixel = 1.3;
        size = 0.80;
        rotation = 0;
        width = 800;
        height = 50;
        hitBoxOffsets(0, 0, 0, 0);
        distanceToBackground = 1;
    }

    @Override
    public void updateStatus() {
        if (gameObjectHitsLowerBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {

    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    @Override
    public void updatePosition() {
        position.down(speedInPixel);
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
        gameView.addRectangleToCanvas(position.getX(), position.getY(), width, height, 3, true, Color.GREEN.darker());
        addGrasToRectangle();
    }

    private void addGrasToRectangle() {
        ArrayList<String> grassImages = new ArrayList<>(List.of("grass_1.png", "grass_2.png", "grass_3.png"));
        int numberOfGrassImages = 7;
        double spacing = width / (double) numberOfGrassImages;
        for (int i = 0; i < numberOfGrassImages; i++) {
            String image = grassImages.get(i % grassImages.size());
            double xCoordinate = position.getX() + (i * spacing);
            gameView.addImageToCanvas(image, xCoordinate, position.getY(), 1.5, 0);
        }
    }

    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}

