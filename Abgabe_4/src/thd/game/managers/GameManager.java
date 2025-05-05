package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.FuelGage;
import thd.gameobjects.unmovable.LifeCounter;
import thd.gameobjects.unmovable.Score;
import thd.gameobjects.unmovable.StatusBar;

class GameManager extends UserControlledGameObjectPool {

    private final GameObjectManager gameObjectManager;

    GameManager(GameView gameView) {
        super(gameView);
        this.gameObjectManager = new GameObjectManager();
        helicopter = new Helicopter(gameView);
        ship = new Ship(gameView);
        score = new Score(gameView);
        greyJet = new GreyJet(gameView);
        fuelItem = new FuelItem(gameView);
        balloon = new Balloon(gameView);
        jetFighter = new JetFighter(gameView);
        tank = new Tank(gameView);
        fuelGage = new FuelGage(gameView);
        lifeCounter = new LifeCounter(gameView);
        statusBar = new StatusBar(gameView);
        bridge = new Bridge(gameView);

        gameObjectManager.add(helicopter);
        gameObjectManager.add(ship);
        gameObjectManager.add(greyJet);
        gameObjectManager.add(fuelItem);
        gameObjectManager.add(balloon);
        gameObjectManager.add(jetFighter);
        gameObjectManager.add(tank);
        gameObjectManager.add(bridge);
        gameObjectManager.add(statusBar);
        gameObjectManager.add(score);
        gameObjectManager.add(fuelGage);
        gameObjectManager.add(lifeCounter);
    }

    @Override
    protected void gameLoop() {
        super.gameLoop();
        gameObjectManager.gameLoop();
    }
}
