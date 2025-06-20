package thd.game.managers;

import thd.game.level.Difficulty;
import thd.game.level.Level;
import thd.game.utilities.FileAccess;
import thd.game.utilities.GameView;
import thd.screens.GameInfo;
import thd.screens.Screens;

import java.awt.*;

class GameManager extends LevelManager {

    private boolean messageGameOverAlreadyDisplayed;
    private boolean messageGreatJobAlreadyDisplayed;
    private boolean endScreenAlreadyShown;

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

                if (!hasNextLevel()) {
                    gameView.stopAllSounds();
                    destroyAllGameObjects();
                    gameView.plotCanvas();
                    gameView.updateBackgroundColor(Color.BLACK);
                    Screens.showEndScreen(gameView, "Sie haben: " + score.showThePointsAtEndingScreen() + " Punkte erreicht!");
                    startNewGame();
                    return;
                }

                gameView.playSound("game_over.wav", false);
                overlay.showMessage("Game Over", 2);
            } else if (!overlay.isMessageShown() && !endScreenAlreadyShown) {
                gameView.stopAllSounds();
                gameView.updateBackgroundColor(Color.BLACK);
                Screens.showEndScreen(
                        gameView,
                        "Sie haben: " + score.showThePointsAtEndingScreen() + " Punkte erreicht!"
                );
                endScreenAlreadyShown = true;
            } else if (endScreenAlreadyShown && !overlay.isMessageShown()) {
                overlay.stopShowing();
                messageGameOverAlreadyDisplayed = false;
                endScreenAlreadyShown = false;

                destroyAllGameObjects();
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
        messageGameOverAlreadyDisplayed = false;
        jetFighter.resetJetFighter();
        statusBar.resetLevelSection();
        Level.difficulty = FileAccess.readDifficultyFromDisc();
        int id = gameView.playSound("start_music.wav", true);
        String chosenDifficulty = Screens.showStartScreen(gameView, GameInfo.TITLE, GameInfo.DESCRIPTION, Level.difficulty.name);
        gameView.stopSound(id);
        Level.difficulty = Difficulty.fromName(chosenDifficulty);
        FileAccess.writeDifficultyToDisc(Level.difficulty);
        if (Level.difficulty == Difficulty.EASY) {
            lives = 12;
        } else {
            lives = 10;
        }
        lifeCounter.setLifeCounter(lives);
        gameView.updateBackgroundColor(new Color(0, 100, 255));
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
