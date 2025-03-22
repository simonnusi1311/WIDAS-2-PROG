package game;

import java.awt.*;

public class Score {
    private GameView gameView;
    private Position position;
    private double size;
    private double rotation;
    private double width;

    public Score(GameView gameView) {
        this.gameView = gameView;
        this.size = 33;
        this.rotation = 0;
        this.width = 150;
        this.position = new Position(GameView.WIDTH - width, -11);
    }

    @Override
    public String toString() {
        return "Score: " + position;
    }

    public void addToCanvas() {
        gameView.addTextToCanvas("Objekt 3", position.getX(), position.getY(),
                size, true, Color.YELLOW, rotation);
    }


}
