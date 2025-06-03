package thd.gameobjects.base;

import java.util.HashMap;
import java.util.List;

/**
 * Stores vertical movement patterns.
 */
class VerticalMovementPattern {

    private final HashMap<String, List<Position>> movementPatterns;


    VerticalMovementPattern() {
        movementPatterns = new HashMap<>();
        movementPatterns.put("down", List.of(new Position(0, 1)));
    }

    /**
     * Return the movement pattern for the given name.
     *
     * @param patternName of the pattern.
     * @return List of positions for the pattern.
     */
    List<Position> loadPattern(String patternName) {
        return movementPatterns.getOrDefault(patternName, List.of());
    }


}
