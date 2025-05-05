package game;

import java.awt.*;

public class Ship {
    private GameView gameView;
    private Position position;
    private double speedInPixel;
    private double size;
    private double rotation;
    private double width;
    private double height;

    public Ship(GameView gameView) {
        this.gameView = gameView;
        position = new Position(1100, 650);
        speedInPixel = 2;
        size = 30;
        rotation = 0;
        width = 150;
        height = 33;
    }

    @Override
    public String toString() {
        return "Flugzeug: " + position;
    }

    public void updatePosition() {
        position.left(speedInPixel);
    }

    public void addToCanvas() {
        gameView.addRectangleToCanvas(position.getX(), position.getY(), width, height, 5, false, Color.WHITE);
        gameView.addRectangleToCanvas(position.getX() + 4, position.getY() + 3, width - 4, height - 3, 0, true, Color.GREEN);
        gameView.addTextToCanvas("Objekt 2", position.getX() + 2, position.getY() - 4,
                size, true, Color.BLUE, rotation);
    }
}
