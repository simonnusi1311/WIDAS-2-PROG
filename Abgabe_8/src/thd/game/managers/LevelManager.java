package thd.game.managers;

import thd.game.level.*;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;


import java.util.List;

public class LevelManager extends GameWorldManager {
    private List<Level> levels;
    private static final int LIVES = 3;

    protected LevelManager(GameView gameView) {
        super(gameView);
        initializeGame();
    }

    @Override
    protected void initializeLevel() {
        super.initializeLevel();
        initializeGameObjects();
    }

    protected void initializeGame() {
        levels = List.of(new Level1(), new Level2(), new Level3());
        level = levels.get(0);
        lives = LIVES;
        points = 0;
    }

    protected boolean hasNextLevel() {
        int currentIndexForLevel = levels.indexOf(level);
        return currentIndexForLevel >= 0 && currentIndexForLevel < levels.size() - 1;
    }

    protected void switchToNextLevel() {
        if (hasNextLevel()) {
            int currentIndexForLevel = levels.indexOf(level);
            level = levels.get(currentIndexForLevel + 1);
            initializeLevel();
        } else {
            throw new NoMoreLevelsAvailableException("There are no more Levels available.");
        }
    }

    //Kopieren Sie diesen Text als Kommentar zur Erinnerung für später in diese Methode.
    private void initializeGameObjects() {
        for (CollidingGameObject collidingGameObject : getPathDecisionObjects()) {
            jetFighter.addPathDecisionObjects(collidingGameObject);
        }
    }
}
