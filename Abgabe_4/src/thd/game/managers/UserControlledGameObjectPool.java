package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.FuelGage;
import thd.gameobjects.unmovable.LifeCounter;
import thd.gameobjects.unmovable.Score;
import thd.gameobjects.unmovable.StatusBar;

import java.awt.event.KeyEvent;

class UserControlledGameObjectPool {
    protected final GameView gameView;
    protected Helicopter helicopter;
    protected Ship ship;
    protected Score score;
    protected GreyJet greyJet;
    protected FuelItem fuelItem;
    protected Balloon balloon;
    protected JetFighter jetFighter;
    protected Tank tank;
    protected FuelGage fuelGage;
    protected LifeCounter lifeCounter;
    protected StatusBar statusBar;
    protected Bridge bridge;

    UserControlledGameObjectPool(GameView gameView) {
        this.gameView = gameView;
    }

    private void processKeyCode(int keyCode) {
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            jetFighter.right();
        } else if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            jetFighter.left();
        } else if (keyCode == KeyEvent.VK_SPACE) {
            jetFighter.shoot();
        }
    }

    protected void gameLoop() {
        jetFighter.stopShooting();
        Integer[] pressedKeys = gameView.keyCodesOfCurrentlyPressedKeys();
        for (int keyCode : pressedKeys) {
            processKeyCode(keyCode);
        }
    }

}
