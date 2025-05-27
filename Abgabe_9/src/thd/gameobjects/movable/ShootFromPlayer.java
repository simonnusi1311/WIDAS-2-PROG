package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;


class ShootFromPlayer extends CollidingGameObject {

    private ShootAnimationState shootAnimationState;
    private boolean triggerAnimation;

    ShootFromPlayer(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        position.updateCoordinates(0, 0);
        speedInPixel = 4;
        size = 0.80;
        rotation = 0;
        width = 1;
        height = 5;
        hitBoxOffsets(0, 0, 0, 8);
        shootAnimationState = ShootAnimationState.SHOOT_ANIMATION_1;
        triggerAnimation = false;
    }

    private enum ShootAnimationState {
        SHOOT_ANIMATION_1("shoot_animation_1.png"),
        SHOOT_ANIMATION_2("shoot_animation_2.png"),
        SHOOT_ANIMATION_3("shoot_animation_3.png");

        private final String image;

        ShootAnimationState(String image) {
            this.image = image;
        }

        private ShootAnimationState next() {
            return values()[(ordinal() + 1) % values().length];
        }

        public String getImage() {
            return image;
        }
    }

    @Override
    public void updateStatus() {
        if (shootHitsUpperBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }

    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {

        if (other instanceof MovableSceneryLeft || other instanceof MovableSceneryRight || other instanceof BigIsland) {
            triggerAnimation = true;
            speedInPixel = 0;
        } else {
            gamePlayManager.destroyGameObject(this);
        }
    }

    private boolean shootHitsUpperBoundary() {
        return position.getY() <= 0;
    }

    @Override
    public void updatePosition() {
        position.up(speedInPixel);
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
        gameView.addBlockImageToCanvas(ShootFromPlayerBlockImages.SHOOT_FROM_PLAYER_BLOCK_IMAGES, position.getX(), position.getY(), 3, 0);
    }
}
