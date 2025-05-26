package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;

// Diese Klasse muss nicht mit Javadoc kommentiert werden.
public class Jimmy extends GameObject {

    private enum State {RUNNING, JUMPING, HOVERING}

    private State currentState;
    private String blockImage;
    private RunningState runningState;
    private JumpingState jumpingState;

    public Jimmy(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        currentState = State.RUNNING;
        size = 5;
        height = 21 * size;
        width = 15 * size;
        speedInPixel = 5;
        distanceToBackground = 10000;
        resetPosition();
        runningState = RunningState.RUNNING_1;
        jumpingState = JumpingState.JUMPING_1;
    }

    private enum RunningState {
        RUNNING_1(JimmyBlockImages.RUNNING_1), RUNNING_2(JimmyBlockImages.RUNNING_2),
        RUNNING_3(JimmyBlockImages.RUNNING_3), RUNNING_4(JimmyBlockImages.RUNNING_4),
        RUNNING_5(JimmyBlockImages.RUNNING_5);

        private final String display;

        RunningState(String display) {
            this.display = display;
        }

        private RunningState switchToNextRunningState() {
            int nextState = (ordinal() + 1) % State.values().length;
            return RunningState.values()[nextState];
        }
    }

    private enum JumpingState {
        JUMPING_1(JimmyBlockImages.STANDARD, 0), JUMPING_2(JimmyBlockImages.JUMPING, 50),
        JUMPING_3(JimmyBlockImages.JUMPING, 30), JUMPING_4(JimmyBlockImages.JUMPING, 0),
        JUMPING_5(JimmyBlockImages.JUMPING, 0), JUMPING_6(JimmyBlockImages.JUMPING, -30),
        JUMPING_7(JimmyBlockImages.JUMPING, -50), JUMPING_8(JimmyBlockImages.STANDARD, 0);

        private final String display;
        private final int up;

        JumpingState(String display, int up) {
            this.display = display;
            this.up = up;
        }

        private JumpingState switchToNextJumpingState() {
            int nextState = (ordinal() + 1) % JumpingState.values().length;
            return JumpingState.values()[nextState];
        }
    }

    private void resetPosition() {
        position.updateCoordinates(-width, GameView.HEIGHT - height);
    }

    @Override
    public void updateStatus() {
        switch (currentState) {
            case RUNNING -> {
                blockImage = runningState.display;
                position.right(speedInPixel);
                if (gameView.timer(80, 0, this)) {
                    runningState = runningState.switchToNextRunningState();
                }
            }
            case JUMPING -> {
                blockImage = jumpingState.display;
                position.right(speedInPixel);
                if (gameView.timer(80, 0, this)) {
                    jumpingState = jumpingState.switchToNextJumpingState();
                    position.up(jumpingState.up);
                }
            }
            case HOVERING -> {
                blockImage = JimmyBlockImages.STANDARD;
                position.right(speedInPixel);
            }
        }
        if (position.getX() > GameView.WIDTH) {
            resetPosition();
            switchToNextState();
            resetStates();
        }
    }

    private void resetStates() {
        runningState = RunningState.RUNNING_1;
        jumpingState = JumpingState.JUMPING_1;
    }

    private void switchToNextState() {
        int nextState = (currentState.ordinal() + 1) % State.values().length;
        currentState = State.values()[nextState];
    }

    @Override
    public void addToCanvas() {
        gameView.addBlockImageToCanvas(blockImage, position.getX(), position.getY(), size, rotation);
    }
}
