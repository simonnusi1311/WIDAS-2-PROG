package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;

class RadioTowerMovementPattern extends MovementPattern {
    private final Position patternForFuelItem;

    RadioTowerMovementPattern() {
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
