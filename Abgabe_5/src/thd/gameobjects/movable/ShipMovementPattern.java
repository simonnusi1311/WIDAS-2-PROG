package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;

class ShipMovementPattern extends MovementPattern {
    private final Position patternForShipMovement;

    ShipMovementPattern() {
        patternForShipMovement = new Position();
        movingRight = random.nextBoolean();
    }

    @Override
    protected Position startPosition() {
        patternForShipMovement.updateCoordinates(random.nextDouble(225, GameView.WIDTH - 260), 0);
        return patternForShipMovement;
    }

    @Override
    protected Position nextPosition() {
        return patternForShipMovement;
    }
}
