package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;

class GreyJetMovementPattern extends MovementPattern {
    private final Position patternForGreyJet;

    GreyJetMovementPattern() {
        patternForGreyJet = new Position();
        movingRight = random.nextBoolean();
    }

    @Override
    protected Position startPosition() {
        if (movingRight) {
            patternForGreyJet.updateCoordinates(-40, 0);
        } else {
            patternForGreyJet.updateCoordinates(GameView.WIDTH, 0);
        }
        return patternForGreyJet;
    }

    @Override
    protected Position nextPosition() {
        patternForGreyJet.updateCoordinates(0, 0);
        return patternForGreyJet;
    }
}
