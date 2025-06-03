package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;

class HelicopterMovementPattern extends MovementPattern {
    private final Position patternForHelicopter;

    HelicopterMovementPattern() {
        patternForHelicopter = new Position();
        movingRight = random.nextBoolean();
    }

    @Override
    protected Position startPosition() {
        patternForHelicopter.updateCoordinates(random.nextDouble(225, GameView.WIDTH - 260), -20);
        return patternForHelicopter;
    }

    @Override
    protected Position nextPosition() {
        return patternForHelicopter;
    }
}
