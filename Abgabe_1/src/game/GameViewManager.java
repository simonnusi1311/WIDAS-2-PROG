package game;

public class GameViewManager {
    private final GameView gameView;
    private Helicopter helicopter;
    private Ship ship;
    private Score score;

    public GameViewManager() {
        gameView = new GameView();
        helicopter = new Helicopter(gameView);
        ship = new Ship(gameView);
        score = new Score(gameView);
        startGameLoop();
    }

    private void startGameLoop() {
        // Der Game-Loop
        while (gameView.isVisible()) {
            helicopter.updatePosition();
            ship.updatePosition();
            helicopter.addToCanvas();
            ship.addToCanvas();
            score.addToCanvas();
            gameView.plotCanvas();
        }
    }
}