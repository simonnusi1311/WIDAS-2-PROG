package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;

class FuelItemMovementPattern extends MovementPattern {
    private final Position patternForFuelItem;

    FuelItemMovementPattern() {
        patternForFuelItem = new Position();
    }

    @Override
    protected Position startPosition() {
        patternForFuelItem.updateCoordinates(random.nextDouble(225, GameView.WIDTH - 260), -20);
        return patternForFuelItem;
    }

    @Override
    protected Position nextPosition() {
        return patternForFuelItem;
    }
}
