package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.*;

class GameManager extends GamePlayManager {

    GameManager(GameView gameView) {
        super(gameView);
        score = new Score(gameView, this);
        jetFighter = new JetFighter(gameView, this);
        fuelGage = new FuelGage(gameView, this);
        lifeCounter = new LifeCounter(gameView, this);
        statusBar = new StatusBar(gameView, this);
        scenery = new Scenery(gameView, this);
        spawnGameObject(fuelGage);
        spawnGameObject(jetFighter);
        spawnGameObject(scenery);
        spawnGameObject(statusBar);
        spawnGameObject(score);
        spawnGameObject(fuelGage);
        spawnGameObject(lifeCounter);
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
