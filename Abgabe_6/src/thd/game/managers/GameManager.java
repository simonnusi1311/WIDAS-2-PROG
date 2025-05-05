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
        redFuelBar = new RedFuelBar(gameView, this);
        sceneryLeft = new SceneryLeft(gameView, this);
        sceneryRight = new SceneryRight(gameView, this);
        spawnGameObject(statusBar);
        spawnGameObject(lifeCounter);
        spawnGameObject(jetFighter);
        spawnGameObject(fuelGage);
        spawnGameObject(score);
        spawnGameObject(redFuelBar);
        spawnGameObject(sceneryLeft);
        spawnGameObject(sceneryRight);
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
