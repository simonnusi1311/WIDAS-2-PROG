package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;

class GreyJetMovementPattern extends MovementPattern {
    private final Position patternForGreyJet;
    private final double yCoordinate;

    GreyJetMovementPattern() {
        patternForGreyJet = new Position();
        movingRight = random.nextBoolean();
        yCoordinate = 0;
    }

    @Override
    protected Position startPosition() {
        if (movingRight) {
            patternForGreyJet.updateCoordinates(0, yCoordinate);
        } else {
            patternForGreyJet.updateCoordinates(GameView.WIDTH, yCoordinate);
        }
        return patternForGreyJet;
    }

    @Override
    protected Position nextPosition() {
        patternForGreyJet.updateCoordinates(0, yCoordinate);
        return patternForGreyJet;
    }
}
