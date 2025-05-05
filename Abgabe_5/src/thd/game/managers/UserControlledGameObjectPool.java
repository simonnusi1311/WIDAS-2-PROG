package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.*;

import java.awt.event.KeyEvent;

class UserControlledGameObjectPool {
    protected final GameView gameView;
    protected Score score;
    protected JetFighter jetFighter;
    protected FuelGage fuelGage;
    protected LifeCounter lifeCounter;
    protected StatusBar statusBar;
    protected Scenery scenery;

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
        Integer[] pressedKeys = gameView.keyCodesOfCurrentlyPressedKeys();
        for (int keyCode : pressedKeys) {
            processKeyCode(keyCode);
        }
    }

}
