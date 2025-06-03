package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;


class ShootFromPlayer extends CollidingGameObject {

    private ShootAnimationState shootAnimationState;
    private State currentState;

    ShootFromPlayer(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        position.updateCoordinates(0, 0);
        speedInPixel = 4;
        size = 0.80;
        rotation = 0;
        width = 1;
        height = 5;
        distanceToBackground = 3;
        hitBoxOffsets(0, 0, 0, 8);
        currentState = State.FLYING;
        shootAnimationState = ShootAnimationState.SHOOT_ANIMATION_1;
    }

    private enum State {
        FLYING, EXPLODING
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

        private String getImage() {
            return image;
        }
    }

    @Override
    public void updateStatus() {
        if (shootHitsUpperBoundary()) {
            gamePlayManager.destroyGameObject(this);
        }


        switch (currentState) {
            case FLYING -> {
                if (shootHitsUpperBoundary()) {
                    gamePlayManager.destroyGameObject(this);
                }
            }
            case EXPLODING -> {
                width = 0;
                height = 0;
                if (gameView.timer(100, 0, this)) {
                    if (shootAnimationState == ShootAnimationState.SHOOT_ANIMATION_3) {
                        gamePlayManager.destroyGameObject(this);
                    } else {
                        shootAnimationState = shootAnimationState.next();
                    }
                }
            }
        }


    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (other instanceof MovableSceneryLeft || other instanceof MovableSceneryRight || other instanceof BigIsland
                || other instanceof IslandBottomHitBox || other instanceof IslandBottomHitBoxTwo) {
            speedInPixel = 0;
            currentState = State.EXPLODING;
        } else {
            gamePlayManager.destroyGameObject(this);
        }
    }

    private boolean shootHitsUpperBoundary() {
        return position.getY() <= 0;
    }

    @Override
    public void updatePosition() {
        if (currentState == State.EXPLODING) {
            position.down(1.3);
        } else {
            position.up(speedInPixel);
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
        if (currentState == State.EXPLODING) {
            gameView.addImageToCanvas(shootAnimationState.getImage(), position.getX() - 30, position.getY() - 30, size, 0);
        } else {
            gameView.addBlockImageToCanvas(ShootFromPlayerBlockImages.SHOOT_FROM_PLAYER_BLOCK_IMAGES, position.getX(), position.getY(), 3, 0);
        }

    }
}
