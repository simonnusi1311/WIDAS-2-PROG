package thd.game.managers;

import thd.game.utilities.GameView;

import java.awt.*;

public class GameViewManager {
    private final GameView gameView;
    private GameManager gameManager;

    public GameViewManager() {
        gameView = new GameView();
        gameManager = new GameManager(gameView);
        gameView.updateWindowTitle("River Raid");
        gameView.updateStatusText("Simon Nuspahic - Java Programmierung SS 2025");
        gameView.updateWindowIcon("icon.png");
        gameView.updateBackgroundColor(Color.GRAY);
        startGameLoop();
    }

    private void initialize() {
        gameView.updateStatusText("Simon Nuspahic - Java Programmierung SS 2025");
    }

    private void startGameLoop() {
        gameManager.gameLoop();
    }
}