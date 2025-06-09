package thd.game.managers;

import thd.game.utilities.GameView;

import java.awt.*;

/**
 * Manages the game view and its interactions between the different gaming objects.
 * This class initializes the game view window in {@link GameView} with different
 * properties.
 *
 * @see GameView
 */

public class GameViewManager {
    private final GameView gameView;
    private final GameManager gameManager;

    /**
     * Creates a new instance of {@link GameViewManager}
     * and starts the game loop.
     */
    public GameViewManager() {
        gameView = new GameView();
        gameManager = new GameManager(gameView);
        initialize();
        gameView.showStatistic(true);
        startGameLoop();
    }

    private void initialize() {
        gameView.updateWindowTitle("River Raid");
        gameView.updateStatusText("Simon Nuspahic - Java Programmierung SS 2025");
        gameView.updateWindowIcon("icon.png");
        gameView.updateBackgroundColor(new Color(0, 100, 255));
    }

    private void startGameLoop() {
        gameManager.gameLoop();
    }
}