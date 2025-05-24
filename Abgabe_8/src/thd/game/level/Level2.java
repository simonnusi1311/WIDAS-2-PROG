package thd.game.level;

/**
 * The second level of the game.
 */
public class Level2 extends Level {

    /**
     * Creates the second level with its name, number, world layout,
     * and enemies.
     */
    public Level2() {
        name = "LevelTwo";
        number = 2;
        world = """
                L                                                     X            R                                                                           T\s
                                                                                                                                                                \s
                                                                                           S                                                                    \s
                G                                                                                                                                               \s
                                                                          H                                                                                     \s
                                                                                                                                                                \s
                                                          B                                                                                                     \s
                                                                                                                                                                \s
                                                                                                                                                                \s
                                                                            F                                                                                   \s
                                                                                                                                                                \s
                                                                                                                                                                \s
                                                                                              B                                                                 \s
                T                                                                                                                                               \s
                                                                                                                                                               G\s
                G                                                                       H                                                                       \s
                                                                                                                                                                \s
                                                                                                                                                                \s
                                                                                                                                                                \s
                                                                                                 B                                                              \s
                                                                                                                                                                \s
                                                S                                                                                                              G\s
                                                                                                                                                                \s
                                                                                                                                                                \s
                                                             B                                                                                                  \s
                                                                                              B                                                                 \s
                                                        S                                                                                                      T\s
                                                                                                                                                                \s
                                                                     H                                                                                          \s
                                                                                                                                                                \s
                G                                                                                                                                               \s
                                                                                                                                                                \s
                                                                                    B                                                                           \s
                                                                                                                                                                \s
                                                                                            H                                                                    \s
                                                        S                                                                                                       \s
                                                                                                                                                                \s
                                                                          F                                                                                     \s
                                                                                                                                                                \s
                                           H                                                                                                                    \s
                                                                                                                                                               T\s
                                                                        S                                                                                       \s
                                                                                                                                                                \s
                                                                                                                                                                \s
                                                        B                                                                                                       \s
                G                                                                                                                                               \s
                                                                                                                                                                \s
                                                        H                                                                                                       \s
                                                                                                                                                                \s
                                                                                           H                                                                    \s
                                                                                                                                                               G\s
                                                                             S                                                                                  \s
                                                                                                                                                                \s
                                                                                                                                                               \s
                                                                                                                                                                 \s
                                                                                                                                                                \s""";
        worldOffsetColumns = 0;
        worldOffsetLines = 44;

    }
}
