package thd.game.utilities;

import thd.game.level.Difficulty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Contains Path Methods for SaveFile Access.
 */
public class FileAccess {
    private static final Path SAVEGAME_DIRECTORY = Path.of(System.getProperty("user.home")).resolve("game");
    private static final String SAVEGAME_FILENAME = "simon_nuspahic_game.txt";
    private static final Path SAVEGAME_FILE = SAVEGAME_DIRECTORY.resolve(SAVEGAME_FILENAME);

    /**
     * Writes the Difficulty local to the Disk.
     *
     * @param difficulty the Difficulty.
     */
    public static void writeDifficultyToDisc(Difficulty difficulty) {
        try {
            if (!Files.exists(SAVEGAME_DIRECTORY)) {
                Files.createDirectories(SAVEGAME_DIRECTORY);
            }
            Files.writeString(SAVEGAME_FILE, difficulty.toString() + '\n', StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Reads Difficulty from the disk.
     * <p>
     * Will return Standard if Exception gets thrown.
     *
     * @return the Difficulty.
     */
    public static Difficulty readDifficultyFromDisc() {
        try {
            String content = Files.readString(SAVEGAME_FILE, StandardCharsets.UTF_8);
            String difficultyLine = content.split("\n")[0].trim();
            for (Difficulty difficulty : Difficulty.values()) {
                if (difficulty.toString().equals(difficultyLine)) {
                    return difficulty;
                }
            }
        } catch (IOException e) {
            return Difficulty.STANDARD;
        }
        return Difficulty.STANDARD;
    }

}
