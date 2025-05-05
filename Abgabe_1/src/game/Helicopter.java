package game;

import java.awt.*;

public class Helicopter {
    private GameView gameView;
    private Position position;
    private double speedInPixel;
    private double size;
    private double rotation;

    public Helicopter(GameView gameView) {
        this.gameView = gameView;
        position = new Position(0, GameView.HEIGHT / 2.0);
        speedInPixel = 5;
        size = 30;
        rotation = 0;
    }

    @Override
    public String toString() {
        return "Flugzeug: " + position;
    }

    public void updatePosition() {
        position.right(speedInPixel);
        rotation++;
    }

    public void addToCanvas() {
        gameView.addTextToCanvas("Objekt 1", position.getX(), position.getY(),
                size, true, Color.YELLOW, rotation);
    }


}
