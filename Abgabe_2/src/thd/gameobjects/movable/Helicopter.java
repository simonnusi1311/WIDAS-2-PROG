package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.Position;

import java.awt.*;

public class Helicopter {
    private final GameView gameView;
    private final Position position;
    private final double speedInPixel;
    private final double size;
    private double rotation;
    private final double width;
    private final double height;

    public Helicopter(GameView gameView) {
        this.gameView = gameView;
        this.position = new Position(0, GameView.HEIGHT / 2.0);
        this.speedInPixel = 5;
        this.size = 30;
        this.rotation = 0;
        this.width = 0;
        this.height = 0;
    }

    @Override
    public String toString() {
        return "Flugzeug: " + position;
    }

    public void updatePosition() {
        position.right(speedInPixel);
        this.rotation++;
    }

    public void addToCanvas() {
        gameView.addImageToCanvas("helicopter.png", position.getX(), position.getY(), 0.60, 0);
        //        gameView.addTextToCanvas("Objekt 1", position.getX(), position.getY(),
        //                size, true, Color.YELLOW, rotation);
    }


}
