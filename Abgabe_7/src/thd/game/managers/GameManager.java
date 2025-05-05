package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.*;

class GameManager extends GameWorldManager {

    GameManager(GameView gameView) {
        super(gameView);
        jetFighter.addPathDecisionObjects(sceneryLeft);
        jetFighter.addPathDecisionObjects(sceneryRight);
    }

    private void gameManagement() {

    }

    @Override
    protected void gameLoop() {
        while (gameView.isVisible()) {
            super.gameLoop();
            gameManagement();
            gameView.plotCanvas();
        }
    }
}
