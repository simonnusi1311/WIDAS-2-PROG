package thd.gameobjects.movable;

import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;

class FuelItemMovementPattern extends MovementPattern {
    private final Position patternForFuelItem;

    FuelItemMovementPattern() {
        patternForFuelItem = new Position();
    }

    @Override
    protected Position startPosition() {
        patternForFuelItem.updateCoordinates(random.nextDouble(320, 960), -97);
        return patternForFuelItem;
    }

    @Override
    protected Position nextPosition() {
        return patternForFuelItem;
    }
}
