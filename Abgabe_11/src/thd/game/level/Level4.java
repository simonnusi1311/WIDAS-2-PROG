package thd.game.level;

/**
 * The fourth level of the game.
 */
public class Level4 extends Level {
    /**
     * Creates the fourth level with its name, number, world layout,
     * and enemies.
     */
    public Level4() {
        name = "Level Four";
        number = 4;
        world = """
                .................................................C.................EA...............................................\s
                .......A........................................C....................E..............................................\s
                .............................................C.........................E............................................\s
                .......A...................................C.............................E..........................................\s
                ..........................................C..................U..........E...........................................\s
                ...........................................C...........................E............................................\s
                ........................................C.............................EA............................................\s
                .........................................C...........................E.............Y................................\s
                ................Y..........................C..........................E.............................................\s
                .............................................C.........................E............................................\s
                ........................Z...................C.........................E.............................................\s
                ..........................................C.........................E.........................Z.....................\s
                .................................D.........C........................E...............................................\s
                ............................................C......................E................................................\s
                L....................................................X.............R..W.........................T...................\s
                ............................................C... ...... ...........E................................................\s
                ...........................................C........... ............E...............................................\s
                .......................................C................ .............E..........Z..................................\s
                ................A....................C.................... H.........E..............................................\s
                ...................................C......................... ......E...............................................\s
                ................................C..................F........... ..E.................................................\s
                ......Z.......................C.................................E. ..........................Z......................\s
                .............. ............C...................................E..... .... .........................................\s
                G...........................C..................S.............E.............. .......................................\s
                .............................C.............................E................... ....................................\s
                ..............................C...........................E.........................................................\s
                ...............................C........................E......................... .................................\s
                ..............................C.............H..........E...........................A... ............................\s
                T......................W.....C..........................E.................................. ........................\s
                ............................C.............................E..................................... ...................\s
                ...........................C.................................E......................................................\s
                ............................C..................U...............E........................t...........................\s
                .......Z.....................C....................................E.................................................\s
                ..............................C......................................E..............................................\s
                ...............................C.........................H..............E..........................................G\s
                ................................C..........................................E........................................\s
                .................................C...........................................E......................................\s
                ......................t.........C.........................U..........S........E...............t.....................\s
                ...............................C...............................................E....................................\s
                ................................C...............................................E..................................G\s
                ...........Z......................C............................................E....................................\s
                ....................................C..........................H.................E..................................\s
                ......................................C............................................E................A...............\s
                ........................................C...........................................E...............................\s
                ......................T.............W......C...............B........................E...............................\s
                ..............................................C......................................E..........t...................\s
                ................................................C......................................E............................\s
                .......................Y..........................C......................................E..........................\s
                ...................................................C......................H................E........................\s
                ....................................................C.......................................E...................G...\s
                .....................................................C.......................................E......................\s
                ..Z..................................................C........................................E.....................\s
                .................................T...................WC..........H...........................E......................\s
                ........................................................C.......................S..............E....Y...............\s
                ................... . ....................................C......................................E..................\s
                ................................ ...........................C..........................B.........E..................\s
                .......................................Z...................C......................................E........t........\s
                ............t..............................................C.....................................E..................\s
                ...........................................................C....................................E...................\s
                .Y...........................................................C.................................E....................\s
                ........................................A.......................C..............U.............E......................\s
                .................................................................C..........................E..........Y............\s
                ..................................................................C......................E..........................\s
                ..............................t....................................C....................E...........................\s
                ....................................................................C...........S.......E...........................\s
                ...................................................................C...................E............................\s
                ................G....................................................C...................E..........................\s
                ......................................................................C...................E.........................\s
                ........................................................................C...........F........E.................t....\s
                ......................................................................C.........................E...................\s
                ....................................................................C..........................E..A.................\s
                ...................................................................C..........................E.....................\s
                .............................A......................................C.............F..........E......................\s
                .................................................................C.........................E............Z...........\s
                .............................................................C..........................E...........................\s
                ..........................................................C..........................E..............................\s
                ............Z..........................................C..........................E.................................\s
                .....................................Z...............C..........................E.........................Y.........\s
                .............Y.......................................C.......................E......................................\s
                ...................................................C.......................E........................................\s
                ...............................A.................C......................E...........................A...............\s
                ...............................................C......................E..........Y..................................\s
                .............................................C......................E..A............................................\s
                ............................................C......................E................................................\s
                                                                                                                            \s""";
        worldOffsetColumns = 0;
        worldOffsetLines = 70;
    }

    private void printWorldLineCount() {
        int lineCount = world.split("\n").length;
        System.out.println("Anzahl der Zeilen im Level 4 + " + lineCount);
    }

}
