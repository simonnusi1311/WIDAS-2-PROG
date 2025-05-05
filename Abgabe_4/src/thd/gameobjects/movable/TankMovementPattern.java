package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;

class TankMovementPattern extends MovementPattern {
    private final Position patternForTankMovement;

    TankMovementPattern() {
        patternForTankMovement = new Position();
        movingRight = random.nextBoolean();
    }

    @Override
    protected Position startPosition() {
        if (movingRight) {
            patternForTankMovement.updateCoordinates(-46, 0);
        } else {
            patternForTankMovement.updateCoordinates(GameView.WIDTH, 0);
        }
        return patternForTankMovement;
    }

    @Override
    protected Position nextPosition() {
        return patternForTankMovement;
    }
}
