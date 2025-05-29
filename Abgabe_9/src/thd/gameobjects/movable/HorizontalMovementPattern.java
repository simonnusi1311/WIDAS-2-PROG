package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HorizontalMovementPattern extends MovementPattern {

    private final HashMap<String, List<Position>> movementPatterns;
    private List<Position> currentPattern;
    private int currentIndex;
    private String currentDirection;

    HorizontalMovementPattern() {
        movementPatterns = createPatterns();
        currentDirection = "right";
        currentPattern = movementPatterns.get(currentDirection);
        currentIndex = 0;
    }

    private HashMap<String, List<Position>> createPatterns() {
        HashMap<String, List<Position>> patterns = new HashMap<>();

        List<Position> rightPattern = new ArrayList<>();
        List<Position> leftPattern = new ArrayList<>();

        double yCoordinateForPattern = GameView.HEIGHT * 0.2;
        int iterationForXCoordinates = 50;

        for (int i = 0; i < iterationForXCoordinates; i++) {
            double xCoordinateForRightPattern = i * (GameView.WIDTH / 50.0);
            double xCoordinateForLeftPattern = GameView.WIDTH - xCoordinateForRightPattern;
            rightPattern.add(new Position(xCoordinateForRightPattern, yCoordinateForPattern));
            leftPattern.add(new Position(xCoordinateForLeftPattern, yCoordinateForPattern));
        }
        patterns.put("right", rightPattern);
        patterns.put("left", leftPattern);
        return patterns;
    }

    public void changeDirection() {
        if (currentDirection.equals("right")) {
            currentDirection = "left";
        } else {
            currentDirection = "right";
        }
        currentPattern = movementPatterns.get(currentDirection);
        currentIndex = 0;
    }

    @Override
    protected Position nextPosition() {
        if (currentIndex >= currentPattern.size()) {
            currentIndex = 0;
        }
        return currentPattern.get(currentIndex++);
    }

    @Override
    protected Position startPosition() {
        currentIndex = 0;
        return nextPosition();
    }
}
