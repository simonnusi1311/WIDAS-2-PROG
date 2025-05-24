package thd.game.level;

import java.util.Random;

/**
 * The base class for the different game levels.
 */
public class Level {

    /**
     * Level name from the different levels.
     */
    public String name;
    /**
     * Level number from the different levels.
     */
    public int number;
    /**
     * String representation of a level.
     */
    public String world;
    /**
     * World Offset Column from a level.
     */
    public int worldOffsetColumns;
    /**
     * World Offset Lines from a level.
     */
    public int worldOffsetLines;

    protected String fillTheWorldStringWithObjects2() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        char[] randomEnemiesForSpawn = {'H', 'B', 'S'};
        boolean alreadyOneObjectInLine = false;
        for (int line = 0; line < 56; line++) {
            boolean spawnEnemyThisLine = line > 3 && line < 53 && random.nextDouble() > 0.45;
            int randomColumnForEnemy = spawnEnemyThisLine ? random.nextInt(25, 80) : -1;

            boolean placeTankThisLine = line == 1 || line == 54 || line == 50;
            boolean spawnLeftTank = random.nextBoolean();

            for (int column = 0; column < 112; column++) {
                char cell = '.';
                if ((line == 0 || line == 55 || line == 50) && column == 0) {
                    cell = 'L';
                } else if ((line == 0 || line == 55 || line == 50) && column == 65) {
                    cell = 'R';
                } else if ((line == 0 || line == 55 || line == 50) && column == 53) {
                    cell = 'X';
                } else if (placeTankThisLine && ((spawnLeftTank && column == 0) || (!spawnLeftTank && column == 110))) {
                    cell = 'T';
                } else if (spawnEnemyThisLine && column == randomColumnForEnemy) {
                    int spawnRandom = random.nextInt(3);
                    cell = randomEnemiesForSpawn[spawnRandom];
                } else if ((column == 0 || column == 110) && random.nextDouble() > 0.98) {
                    cell = 'G';
                } else if ((column == 0 || column == 110) && random.nextDouble() > 0.98
                        && line < 50 && line > 5) {
                    cell = 'T';
                } else if (column > 28 && column < 79 && random.nextDouble() > 0.999 && !alreadyOneObjectInLine
                        && line > 3 && line < 53) {
                    cell = 'F';
                    alreadyOneObjectInLine = true;
                } else if ((column < 15) ^ (column > 95) && random.nextDouble() > 0.999) {
                    cell = 'Y';
                } else if ((column < 15) ^ (column > 95) && random.nextDouble() > 0.999) {
                    cell = 'Z';
                }
                sb.append(cell);
            }
            alreadyOneObjectInLine = false;
            sb.append("\n");
        }
        return sb.toString();
    }

}
