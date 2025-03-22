package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.Position;

import java.awt.*;

public class Ship {
    private final GameView gameView;
    private final Position position;
    private final double speedInPixel;
    private final double size;
    private final double rotation;
    private final double width;
    private final double height;

    public Ship(GameView gameView) {
        this.gameView = gameView;
        this.position = new Position(1100, 650);
        this.speedInPixel = 2;
        this.size = 30;
        this.rotation = 0;
        this.width = 150;
        this.height = 33;
    }

    @Override
    public String toString() {
        return "Flugzeug: " + position;
    }

    public void updatePosition() {
        position.left(speedInPixel);
    }

    public void addToCanvas() {
        gameView.addImageToCanvas("ship.png", position.getX(), position.getY(), 0.60, 0);
        //        gameView.addRectangleToCanvas(position.getX(), position.getY(), this.width, this.height, 5, false, Color.WHITE);
        //        gameView.addRectangleToCanvas(position.getX()+4, position.getY()+3, this.width-4, this.height-3, 0, true, Color.GREEN);
        //        gameView.addTextToCanvas("Objekt 2", position.getX() + 3, position.getY() - 4,
        //                size, true, Color.BLUE, rotation);
    }


}
