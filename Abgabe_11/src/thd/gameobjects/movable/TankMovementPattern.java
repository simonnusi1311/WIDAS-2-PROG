package thd.gameobjects.movable;

import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;
import thd.game.utilities.GameView;

class TankMovementPattern extends MovementPattern {
    private final Position patternForTankMovement;

    TankMovementPattern() {
        patternForTankMovement = new Position();
        movingRight = random.nextBoolean();
    }

    @Override
    protected Position startPosition() {
        if (movingRight) {
            patternForTankMovement.updateCoordinates(0, -20);
        } else {
            patternForTankMovement.updateCoordinates(GameView.WIDTH - 20, -20);
        }
        return patternForTankMovement;
    }

    @Override
    protected Position nextPosition() {
        return patternForTankMovement;
    }
}
