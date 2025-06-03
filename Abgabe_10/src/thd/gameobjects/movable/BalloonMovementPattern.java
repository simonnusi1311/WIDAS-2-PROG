package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;

class BalloonMovementPattern extends MovementPattern {
    private final Position patternForBalloon;

    BalloonMovementPattern() {
        patternForBalloon = new Position();
        movingRight = random.nextBoolean();
    }

    @Override
    protected Position startPosition() {
        patternForBalloon.updateCoordinates(random.nextDouble(225, GameView.WIDTH - 260), -20);
        return patternForBalloon;
    }

    @Override
    protected Position nextPosition() {
        return patternForBalloon;
    }
}
