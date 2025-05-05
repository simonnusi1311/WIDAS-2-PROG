package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.movable.GreyJet;
import thd.gameobjects.movable.Ship;
import thd.gameobjects.unmovable.Score;

class GameManager {
    private GreyJet greyJet;
    private Ship ship;
    private Score score;
    private final GameView gameView;

    GameManager(GameView gameView) {
        this.gameView = gameView;
        greyJet = new GreyJet(gameView);
        ship = new Ship(gameView);
        score = new Score(gameView);
    }

    void gameLoop() {
        // Der Game-Loop
        greyJet.updatePosition();
        ship.updatePosition();
        greyJet.addToCanvas();
        ship.addToCanvas();
        score.addToCanvas();
    }
}
