package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;

import java.util.List;

import java.awt.*;

/**
 * Represents a BigIsland in the {@link GameView} window.
 * this island is a bigger obstacle in the center of the Game.
 *
 * @see GameView
 * @see Position
 */

public class BigIsland extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<JetFighter> {

    private final IslandBottomHitBox islandBottomHitBox;
    private final IslandBottomHitBoxTwo islandBottomHitBoxTwo;
    private final IslandTopHitBox islandTopHitBox;
    private final IslandTopHitBoxTwo islandTopHitBoxTwo;

    /**
     * Creates a new big island in game view.
     *
     * @param gameView        The gaming window where the Bridge will be displayed.
     * @param gamePlayManager The main gameplay logic.
     */

    public BigIsland(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        speedInPixel = 1.3;
        size = 0.50;
        rotation = 0;
        width = 180;
        height = 1500;
        hitBoxOffsets(-72, -550, -30, -400);
        distanceToBackground = 1;
        islandBottomHitBox = new IslandBottomHitBox(gameView, gamePlayManager);
        islandBottomHitBoxTwo = new IslandBottomHitBoxTwo(gameView, gamePlayManager);
        islandTopHitBox = new IslandTopHitBox(gameView, gamePlayManager);
        islandTopHitBoxTwo = new IslandTopHitBoxTwo(gameView, gamePlayManager);
        gamePlayManager.spawnGameObject(islandBottomHitBox);
        gamePlayManager.spawnGameObject(islandBottomHitBoxTwo);
        gamePlayManager.spawnGameObject(islandTopHitBox);
        gamePlayManager.spawnGameObject(islandTopHitBoxTwo);
    }

    @Override
    public void updateStatus() {
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
    }

    /**
     * Returns all special hitboxes of the island.
     * These hitboxes are used for collision and path decision handling.
     *
     * @return A list of {@link CollidingGameObject} representing all hitbox objects.
     */
    public List<CollidingGameObject> collectAllHitBoxes() {
        return List.of(islandBottomHitBox, islandBottomHitBoxTwo, islandTopHitBox);
    }

    /**
     * Updates the position of the gaming object.
     *
     * @see Position
     */
    @Override
    public void updatePosition() {
        position.down(speedInPixel);
        islandBottomHitBox.getPosition().updateCoordinates(position.getX() + 6, position.getY() + 700);
        islandBottomHitBoxTwo.getPosition().updateCoordinates(position.getX() + 6, position.getY() + 700);
        islandTopHitBox.getPosition().updateCoordinates(position.getX() + 6, position.getY() - 500);
        islandTopHitBoxTwo.getPosition().updateCoordinates(position.getX() + 6, position.getY() - 680);
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
        gameView.addOvalToCanvas(position.getX(), position.getY(), width, height, 4, true, Color.GREEN.darker());
    }


    @Override
    public boolean tryToActivate(JetFighter info) {
        return getPosition().getY() < info.getPosition().getY() + ACTIVATION_DISTANCE;
    }
}
