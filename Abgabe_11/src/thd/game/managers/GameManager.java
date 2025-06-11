package thd.game.managers;

import thd.game.level.Difficulty;
import thd.game.level.Level;
import thd.game.utilities.FileAccess;
import thd.game.utilities.GameView;

class GameManager extends LevelManager {

    private boolean messageGameOverAlreadyDisplayed;
    private boolean messageGreatJobAlreadyDisplayed;

    GameManager(GameView gameView) {
        super(gameView);
        initializeLevel();
        jetFighter.addPathDecisionObjects(sceneryLeft);
        jetFighter.addPathDecisionObjects(sceneryRight);
        startNewGame();
    }

    private boolean endOfLevel() {
        return changeLevelSection();
    }

    private boolean endOfGame() {
        return lives == 0 || (!hasNextLevel() && endOfLevel());
    }

    private void gameManagement() {

        if (endOfGame()) {
            if (!messageGameOverAlreadyDisplayed) {
                messageGameOverAlreadyDisplayed = true;
                overlay.showMessage("Game Over", 2);
            }
            if (gameView.timer(0, 0, this)) {
                moveWorldUp(2.2);
            }

            if (!overlay.isMessageShown()) {
                overlay.stopShowing();
                messageGameOverAlreadyDisplayed = false;
                startNewGame();
            }
        } else if (endOfLevel()) {
            if (!messageGreatJobAlreadyDisplayed) {
                messageGreatJobAlreadyDisplayed = true;
                overlay.showMessage("Great Job!", 2);
            }
            switchToNextLevel();
            initializeLevel();
        }
    }

    private void startNewGame() {
        Level.difficulty = FileAccess.readDifficultyFromDisc();
        Level.difficulty = Difficulty.EASY;
        FileAccess.writeDifficultyToDisc(Level.difficulty);
        initializeGame();
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
        overlay.showMessage(level.name, 2);
    }

    @Override
    protected void initializeGame() {
        super.initializeGame();
        initializeLevel();
    }
}
