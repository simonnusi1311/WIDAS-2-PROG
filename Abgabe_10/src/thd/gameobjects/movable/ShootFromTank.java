package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;

/**
 * The class which contains the white shoot from the tank.
 *
 * @see Tank
 */
class ShootFromTank extends CollidingGameObject implements ShiftableGameObject {

    private final Tank tank;
    private State currentState;
    private ShootAnimationState shootAnimationState;
    private boolean isCollidableWithScenery;

    ShootFromTank(GameView gameView, GamePlayManager gamePlayManager, Tank tank) {
        super(gameView, gamePlayManager);
        this.tank = tank;
        speedInPixel = 7;
        size = 0.80;
        rotation = 0;
        width = 9;
        height = 2;
        distanceToBackground = 2;
        hitBoxOffsets(0, 0, 1, 0);
        currentState = State.FLYING;
        shootAnimationState = ShootAnimationState.SHOOT_ANIMATION_1;
        isCollidableWithScenery = false;
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
        shootIsImmuneAgainstScenery();
        switch (currentState) {
            case FLYING -> {
                if (gameObjectHitsLeftBoundary() || gameObjectHitsRightBoundary()) {
                    currentState = State.EXPLODING;
                }

            }
            case EXPLODING -> {
                if (gameView.timer(100, 0, this)) {
                    shootAnimationState = shootAnimationState.next();
                    if (shootAnimationState == ShootAnimationState.SHOOT_ANIMATION_3) {
                        gamePlayManager.destroyGameObject(this);
                    }
                }
            }
        }
    }

    private void shootIsImmuneAgainstScenery() {
        if (!isCollidableWithScenery && gameView.timer(1000, 0, this)) {
            isCollidableWithScenery = true;
        }
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (other instanceof JetFighter) {
            gamePlayManager.destroyGameObject(this);
        }
        if (isCollidableWithScenery && (other instanceof MovableSceneryLeft || other instanceof MovableSceneryRight
                || other instanceof BigIsland || other instanceof SmallIsland)) {
            if (currentState == State.FLYING) {
                currentState = State.EXPLODING;
            }
        }
    }

    @Override
    public void updatePosition() {
        if (gamePlayManager.isJetInRespawn()) {
            position.down(1.3);
        }

        if (currentState == State.FLYING) {
            if (tank.getTankMovementPattern().movingRight) {
                position.right(speedInPixel);
            } else {
                position.left(speedInPixel);
            }
        }
    }

    @Override
    protected boolean gameObjectHitsLeftBoundary() {
        return position.getX() <= 0;
    }

    @Override
    protected boolean gameObjectHitsRightBoundary() {
        return position.getX() >= GameView.WIDTH;
    }

    void updateTheStartPositionFromShoot(double xCoordinate, double yCoordinate) {
        position.updateCoordinates(xCoordinate, yCoordinate);
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
        if (currentState == State.FLYING) {
            gameView.addBlockImageToCanvas(ShootFromTankBlockImages.SHOOT_FROM_TANK_BLOCK_IMAGES, position.getX(), position.getY(), 4, 0);
        } else if (currentState == State.EXPLODING) {
            gameView.addImageToCanvas(shootAnimationState.getImage(), position.getX(), position.getY(), size, 0);
        }

    }
}
