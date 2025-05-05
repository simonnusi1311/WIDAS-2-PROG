package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;

class BridgeMovementPattern extends MovementPattern {
    private final Position patternForBridge;

    BridgeMovementPattern() {
        patternForBridge = new Position();
        movingRight = random.nextBoolean();
    }

    @Override
    protected Position startPosition() {
        patternForBridge.updateCoordinates((GameView.WIDTH / 2.0) - 50, -85);
        return patternForBridge;
    }

    @Override
    protected Position nextPosition() {
        return patternForBridge;
    }
}
