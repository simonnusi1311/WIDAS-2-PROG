package thd.game.managers;

import thd.game.level.*;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;

import java.util.List;

class LevelManager extends GameWorldManager {
    private List<Level> levels;

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
        levels = List.of(
                new Level1(), new Level2(), new Level3(), new Level4(), new Level5(), new Level6(),
                new Level7(), new Level8(), new Level9(), new Level10(), new Level11(), new Level12()
        );
        level = levels.get(0);
        lives = switch (Level.difficulty) {
            case EASY -> 12;
            case STANDARD -> 10;
        };
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

    private void initializeGameObjects() {
        for (CollidingGameObject collidingGameObject : getPathDecisionObjects()) {
            jetFighter.addPathDecisionObjects(collidingGameObject);
        }
    }
}
