package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.Score;

class GameManager {
    private Ship ship;
    private Score score;
    private final GameView gameView;
    private RandomBall randomBall;
    private FollowerBall followerBall;
    private GreyJet greyJet;

    GameManager(GameView gameView) {
        this.gameView = gameView;
        ship = new Ship(gameView);
        score = new Score(gameView);
        randomBall = new RandomBall(gameView);
        followerBall = new FollowerBall(gameView, randomBall);
        greyJet = new GreyJet(gameView);
    }

    void gameLoop() {
        ship.updatePosition();
        ship.addToCanvas();
        score.addToCanvas();
        randomBall.updatePosition();
        randomBall.addToCanvas();
        followerBall.updatePosition();
        followerBall.addToCanvas();
        greyJet.updatePosition();
        greyJet.addToCanvas();
    }


}
