package thd.game.level;

public class Level1 extends Level {

    public Level1() {
        name = "LevelOne";
        number = 1;
        world = fillTheWorldStringWithObjects();
        worldOffsetColumns = 0;
        worldOffsetLines = 44;
        output();
    }

    public void output() {
        String[] output = world.split("\n");

        for (int row = 0; row < output.length; row++) {
            String line = output[row];
            for (int col = 0; col < line.length(); col++) {
                char c = line.charAt(col);
                System.out.println("Zeile: " + row + ", Spalte " + col + ": " + c);
            }
        }
    }
}
