package thd.game.level;

import java.util.Random;

/**
 * The base class for the different game levels.
 */
public class Level {

    public String name;
    public int number;
    public String world;
    public int worldOffsetColumns;
    public int worldOffsetLines;

    public Level() {
        fillTheWorldStringWithObjects();
    }

    protected String fillTheWorldStringWithObjects() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        char[] randomEnemiesForSpawn = {'H', 'B', 'S'};
        boolean alreadyOneObjectInLine = false;

        for (int line = 0; line < 56; line++) {
            for (int column = 0; column < 145; column++) {
                char cell = '.';

                if ((line == 0 || line == 55 || line == 50) && column == 0) {
                    cell = 'L';
                } else if ((line == 0 || line == 55 || line == 50) && column == 65) {
                    cell = 'R';
                } else if ((line == 0 || line == 55 || line == 50) && column == 53) {
                    cell = 'X';
                } else if (column > 28 && column < 79 && random.nextDouble() > 0.99 && !alreadyOneObjectInLine &&
                        line > 3 && line < 53) {
                    cell = randomEnemiesForSpawn[random.nextInt(randomEnemiesForSpawn.length)];
                    alreadyOneObjectInLine = true;
                } else if ((column == 0 || column == 143) && random.nextDouble() > 0.97) {
                    cell = 'G';
                } else if ((column == 0 || column == 143) && random.nextDouble() > 0.97) {
                    cell = 'T';
                } else if (column > 28 && column < 79 && random.nextDouble() > 0.999 && !alreadyOneObjectInLine) {
                    cell = 'F';
                    alreadyOneObjectInLine = true;
                }

                sb.append(cell);
            }

            alreadyOneObjectInLine = false;
            sb.append("\n");
        }

        System.out.println(sb);
        return sb.toString();
    }

}
