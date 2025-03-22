package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.movable.Helicopter;
import thd.gameobjects.movable.Ship;
import thd.gameobjects.unmovable.Score;

class GameManager {
    private Helicopter helicopter;
    private Ship ship;
    private Score score;
    private final GameView gameView;

    GameManager(GameView gameView) {
        this.gameView = gameView;
        helicopter = new Helicopter(gameView);
        ship = new Ship(gameView);
        score = new Score(gameView);
    }

    void gameLoop() {
        // Der Game-Loop
        while (gameView.isVisible()) {
            helicopter.updatePosition();
            ship.updatePosition();
            helicopter.addToCanvas();
            ship.addToCanvas();
            score.addToCanvas();
            gameView.plotCanvas();
        }
    }


}
