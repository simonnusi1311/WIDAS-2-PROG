package thd.game.managers;

import thd.game.utilities.GameView;

class GameManager extends LevelManager {

    GameManager(GameView gameView) {
        super(gameView);
        initializeLevel();
        jetFighter.addPathDecisionObjects(sceneryLeft);
        jetFighter.addPathDecisionObjects(sceneryRight);
        initializeGame();
    }

    private boolean endOfLevel() {
        return gameView.timer(1000000, 0, this);
    }

    private boolean endOfGame() {
        return lives == 0 || (!hasNextLevel() && endOfLevel());
    }

    private void gameManagement() {
        if (endOfGame()) {
            initializeGame();
        } else if (endOfLevel()) {
            switchToNextLevel();
            initializeLevel();
        }
    }

    @Override
    protected void gameLoop() {
        while (gameView.isVisible()) {
            super.gameLoop();
            gameManagement();
            gameView.plotCanvas();
        }
    }

    @Override
    protected void initializeLevel() {
        super.initializeLevel();
    }

    @Override
    protected void initializeGame() {
        super.initializeGame();
        initializeLevel();
    }
}
