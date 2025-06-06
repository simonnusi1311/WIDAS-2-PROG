package thd.game.utilities;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

/**
 * Diese Klasse kapselt die Bibliothek SWING und stellt einfache Methoden zur Verfügung, um ein Fenster zu erstellen und
 * darauf zu zeichnen. Es kann Ton ausgegeben werden und Tastatur- und Mausereignisse können abgefragt werden. Das
 * Fenster erscheint, sobald ein neues Objekt von GameView erzeugt wurde.
 * <p>
 * Es wird eine Leinwand mit einer Auflösung von {@value WIDTH} * {@value HEIGHT} Pixeln erzeugt<br> (Breite =
 * {@value WIDTH} Pixel, Höhe = {@value HEIGHT} Pixel).
 * <br><br><pre><code>
 *   (0,0) . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ({@value WIDTH},0)<br>
 *         . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .<br>
 *         . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .<br>
 *         . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .<br>
 * (0,{@value HEIGHT}) . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ({@value WIDTH},{@value HEIGHT})
 * <br></code></pre>
 *
 * <p>
 * GameView stellt eine Leinwand (Canvas) zur Verfügung, auf der Sie malen können. Die Leinwand können sie dann mit der
 * Methode {@link #plotCanvas()} anzeigen lassen.
 * <pre>{@code
 *
 * package game;
 *
 * import java.awt.*;
 *
 * public class GameViewManager {
 *
 *     private final GameView gameView;
 *
 *     public GameViewManager() {
 *         gameView = new GameView();
 *         gameView.updateWindowTitle("UFO-Fight");
 *         startGameLoop();
 *     }
 *
 *     private void startGameLoop() {
 *         while (gameView.isVisible()) {
 *             gameView.addTextToCanvas("Hallo GameView!", 0, 0, 16, false, Color.YELLOW, 0);
 *             gameView.plotCanvas();
 *         }
 *     }
 * }
 * }</pre>
 * <p>
 * Alternativ gibt es auch einen Terminal-Modus, der eine einfache Textausgabe ermöglicht, siehe
 * {@link #plotTerminal(String, String)}.
 * <pre>{@code
 *     void testTerminalMode() {
 *         GameView gameView = new GameView();
 *         gameView.plotTerminal("Hallo Welt", "S");
 *     }
 * }</pre>
 *
 * @author Andreas Berl
 */
public final class GameView {

    /** Breite der Leinwand in Pixeln. */
    public static final int WIDTH = 1280;
    /** Höhe der Leinwand in Pixeln. */
    public static final int HEIGHT = 720;

    private static int instances = 0;
    private final Canvas canvas;
    private final Mouse mouse;
    private final Keyboard keyboard;
    private final Sound sound;
    private final SwingAdapter swingAdapter;
    private final Statistic statistic;
    private final GameLoop gameLoop;
    private final Timer timer;
    private final Terminal terminal;
    private final TestEnvironment testEnvironment;

    /**
     * Es wird ein Fenster mit einer Auflösung von {@value WIDTH} * {@value HEIGHT} Pixeln erzeugt<br>. Das Fenster
     * erscheint, sobald ein neues Objekt von GameView erzeugt wurde.
     */
    public GameView() {
        this(false);
    }

    private GameView(boolean testEnvironmentOnly) {
        instances++;
        statistic = new Statistic();
        swingAdapter = new SwingAdapter(statistic);
        mouse = new Mouse(swingAdapter);
        keyboard = new Keyboard();
        sound = new Sound();
        canvas = new Canvas();
        timer = new Timer();
        gameLoop = new GameLoop();
        swingAdapter.registerListeners(mouse, keyboard, sound);
        swingAdapter.initialize(testEnvironmentOnly);
        terminal = new Terminal();
        testEnvironment = new TestEnvironment();
    }

    /**
     * Erzeugt ein Test-Objekt von GameView. Das Fenster wird nicht sichtbar. Es wird nur für automatisierte Tests
     * verwendet.
     *
     * @return Ein Test-Objekt von GameView.
     */
    public static GameView createGameViewTestEnvironment() {
        return new GameView(true);
    }

    /**
     * Diese Methode zeigt an, ob das Fenster sichtbar ist oder nicht. Nach dem Schließen des Fensters liefert es
     * false.
     *
     * @return true, falls das Fenster sichtbar ist.
     */
    public boolean isVisible() {
        return swingAdapter.frame.isVisible();
    }

    /**
     * Zeigt den aktuellen Zustand der Leinwand im Fenster an. Die Leinwand wird anschließend wieder gelöscht. Ein
     * Beispiel für die Benutzung dieser Methode ist in der Klassenbeschreibung {@link GameView} zu finden. Zwischen
     * zwei Aufrufen dieser Methode wird automatisch eine Pause eingefügt. Das führt zu einer Darstellung von höchstens
     * 60 Bildern pro Sekunde.
     */
    public void plotCanvas() {
        gameLoop.plotCanvas();
    }

    /**
     * Die Ausgabe erfolgt im Terminal-Modus. Der übergebene Text wird direkt im Fenster ausgegeben, vorhergehende
     * Inhalte werden gelöscht. Die Anzeige erfolgt oben links. Die Auflösung des Fensters muss übergeben werden ("XL",
     * "L", "M" oder "S"). Ein Beispiel für die Benutzung dieser Methode ist in der Klassenbeschreibung {@link GameView}
     * zu finden. Zwischen zwei Aufrufen dieser Methode wird automatisch eine Pause eingefügt. Das führt zu einer
     * Darstellung von höchstens 60 Bildern pro Sekunde.
     *
     * @param text       Der anzuzeigende Text.
     * @param resolution Die Auflösung, mit der der Text angezeigt werden soll. Es gibt vier Wahlmöglichkeiten:
     *                   <ul>
     *                   <li><code>"S" → 80 Zeilen x 45 Spalten</code></li>
     *                   <li><code>"M" → 128 Zeilen x 64 Spalten</code></li>
     *                   <li><code>"L" → 160 Zeilen x 90 Spalten</code></li>
     *                   <li><code>"XL" → 240 Zeilen x 135 Spalten</code></li>
     *                   </ul>
     */
    public void plotTerminal(String text, String resolution) {
        terminal.plotTerminal(text, resolution);
    }

    /**
     * Zeigt eine Statistik in der linken oberen Ecke des Bildschirms. Diese Statistik kann jederzeit mit dem
     * Tastaturkürzel <b>STRG+R</b> ein- und ausgeschaltet werden.
     * <br><br>
     * <b>Loops/Sekunde</b>: Zeigt an, wie oft der Loop pro Sekunde ausgeführt wird. Dieser Wert sollte knapp unter 60
     * liegen. Falls der Wert kleiner ist, schafft Ihr Rechner die Berechnungen nicht<br><br>
     * <b>Bilder/Sekunde:</b> Zeigt an, wie viele Frames pro Sekunde auf das Fenster gezeichnet werden. Dieser Wert
     * sollte knapp unter 60 liegen. Falls der Wert kleiner ist, wurden einzelne Frames nicht gezeichnet, weil die
     * Grafikkarte zu langsam war.<br><br>
     * <b>GameView:</b> Zeigt an, wie viel Zeit GameView pro Frame braucht um ein Bild aufzubereiten.<br><br>
     * <b>Grafikkarte:</b> Zeigt an, wie viel Zeit die Grafikdarstellung pro Bild braucht um es aufs Fenster zu
     * zeichnen.<br><br>
     * <b>Spiel-Logik:</b> Zeigt an, wie viel Zeit die von Ihnen implementierte Spiel-Logik benötigt, um ein Bild zu
     * berechnen. Es handelt sich dabei um den Code, der in der Methode <code>gameLoop()</code> ablauft. Dieser Wert
     * sollte etwa
     * <b>1</b> betragen.<br><br>
     * <b>Sichtbar:</b> Zeigt an, wie viel Spielobjekte gerade zu sehen sind. Dieser Wert sollte <b>kleiner 200</b>
     * sein.<br><br>
     * <b>Unsichtbar:</b> Zeigt an, wie viel Spielobjekte gerade zu nicht sehen sind, aber trotzdem berechnet werden.
     * Dieser Wert sollte bei <b>0</b> liegen.<br><br>
     * <b>Größe:</b> Zeigt an, wie viel Speicher für Bilder benötigt wird. Dieser Wert sollte unter <b>500 MB</b>
     * liegen.<br><br>
     * <b>Überläufe:</b> Zeigt an, wie oft der Bildspeicher schon gelöscht werden musste, weil zu viel Speicher
     * benötigt wurde. Dieser Wert sollte bei <b>0</b> liegen.<br><br>
     *
     * @param show True, falls die Statistik angezeigt werden soll.
     */
    public void showStatistic(boolean show) {
        statistic.showStatistics = show;
    }

    /**
     * Setzt den Fenstertitel.
     *
     * @param title Der Fenstertitel
     */
    public void updateWindowTitle(String title) {
        swingAdapter.setTitle(title + " - " + Version.getStandardTitle());
    }

    /**
     * Legt ein Symbol für die Titelleiste fest. Das Symbolfile muss in einem Verzeichnis "src/resources" liegen. Bitte
     * den Namen des Files ohne Verzeichnisnamen angeben, z.B.<code>setWindowIcon("Symbol.png")</code>.
     *
     * @param iconFileName Der Dateiname des Symbols.
     */
    public void updateWindowIcon(String iconFileName) {
        swingAdapter.setWindowIcon(iconFileName);
    }

    /**
     * Text, der in der Statuszeile angezeigt wird.
     *
     * @param statusText Text der Statuszeile.
     */
    public void updateStatusText(String statusText) {
        swingAdapter.setStatusText(statusText);
    }

    /**
     * Legt die Hintergrundfarbe fest. Die Farbe bleibt so lange gleich, bis sie erneut geändert wird.
     *
     * @param backgroundColor Die Hintergrundfarbe.
     */
    public void updateBackgroundColor(Color backgroundColor) {
        canvas.setBackgroundColor(backgroundColor);
    }

    /**
     * Fügt eine neue Farbe zur Farbtabelle für Block-Grafiken hinzu oder überschreibt eine vorhandene Farbe mit neuen
     * Werten.<br>
     * <br>
     * Die bereits vordefinierte Farbtabelle:
     * <ul>
     * <li>'R' = Color.RED</li>
     * <li>'r' = Color.RED.brighter()</li>
     * <li>'G' = Color.GREEN</li>
     * <li>'g' = Color.GREEN.brighter()</li>
     * <li>'B' = Color.BLUE</li>
     * <li>'b' = Color.BLUE.brighter()</li>
     * <li>'Y' = Color.YELLOW</li>
     * <li>'y' = Color.YELLOW.brighter()</li>
     * <li>'P' = Color.PINK</li>
     * <li>'p' = Color.PINK.brighter()</li>
     * <li>'C' = Color.CYAN</li>
     * <li>'c' = Color.CYAN.brighter()</li>
     * <li>'M' = Color.MAGENTA</li>
     * <li>'m' = Color.MAGENTA.brighter()</li>
     * <li>'O' = Color.ORANGE</li>
     * <li>'o' = Color.ORANGE.brighter()</li>
     * <li>'W' = Color.WHITE</li>
     * <li>'L' = Color.BLACK</li>
     * </ul>
     *
     * @param character Buchstabe, der der Farbe zugeordnet ist.
     * @param color     Die Farbe, die dem Buchstaben zugeordnet ist.
     */
    public void updateColorForBlockImage(char character, Color color) {
        swingAdapter.setColorForBlockImage(character, color);
    }

    /**
     * Diese Methode kann bunte Block-Grafiken anzeigen. Dazu muss ein farbcodierter <code>String</code> übergeben
     * werden, der dann auf die Leinwand (Canvas) übertragen wird, ohne die bisherigen Inhalte zu löschen. Die im
     * <code>String</code> enthaltenen Buchstaben werden als Farben interpretiert. Jeder Buchstabe repräsentiert einen
     * Block mit der Größe blockSize * blockSize. Beispiel: Ein rotes Dreieck mit grüner Füllung.
     * <br>
     * <pre>{@code
     * String dreieck =
     * "   R   \n" +
     * "  RGR  \n" +
     * " RGGGR \n" +
     * "RRRRRRR\n";
     * }</pre>
     * Um die Farbcodes zu interpretieren, wird eine Farbpalette ausgewertet. Die Farben der Farbpalette lassen sich
     * über die Methode {@link #updateColorForBlockImage(char, Color)} anpassen.
     * <p>
     * Es sind nur Zeichen erlaubt, die in der Farbpalette vorkommen, das Leerzeichen (Space) und Zeilenumbrüche. Das
     * Leerzeichen ist transparent, man kann den Hintergrund sehen. Zusätzlich werden Koordinaten ausgewertet: (0, 0)
     * ist links oben {@link #GameView()}. Negative Koordinaten können verwendet werden, um Grafiken teilweise
     * anzuzeigen.
     * <p>
     * Die Größe der Blöcke muss mit dem Parameter <code>blockSize</code> festgelegt werden. Beispielsweise bedeutet
     * <code>blockSize = 10</code>, dass ein Block die Fläche von 10 mal 10 Pixeln belegt.
     * <p>
     * Die Grafik kann mit einer Rotation dargestellt werden, dabei wird um den Mittelpunkt der Grafik rotiert. Eine
     * Rotation um 0° stellt das Bild ohne Rotation dar, bei 180° steht das Bild auf dem Kopf.
     *
     * @param blockImage Das Bild als farbcodierter String.
     * @param x          x-Koordinate, bei welcher der Text angezeigt werden soll. 0 ist links.
     * @param y          y-Koordinate, bei welcher der Text angezeigt werden soll. 0 ist oben.
     * @param blockSize  Die Größe eines einzelnen Farbblocks.
     * @param rotation   Die Rotation des Bildes in Grad um den Mittelpunkt.
     * @see #updateColorForBlockImage(char, Color)
     */
    public void addBlockImageToCanvas(String blockImage, double x, double y, double blockSize, double rotation) {
        BufferedImage image = swingAdapter.createImageFromColorString(blockImage, blockSize);
        addImageToCanvasIfVisible(image, x, y, rotation);
    }

    /**
     * Schreibt den übergebenen Text auf die Leinwand (Canvas), ohne die bisherigen Inhalte zu löschen. Zusätzlich
     * werden Koordinaten ausgewertet: (0, 0) entspricht links oben {@link #GameView()}. Negative Koordinaten können
     * verwendet werden, um Texte teilweise anzuzeigen. Leerzeichen sind durchsichtig (Objekte im Hintergrund sind zu
     * sehen). Die Schrift kann mit einer Rotation dargestellt werden, dabei wird um den Mittelpunkt der Grafik rotiert.
     * Eine Rotation um 0° stellt die Schrift ohne Rotation dar, bei 180° steht die Schrift auf dem Kopf. Es wird eine
     * Standard-Schriftart der Familie "Monospaced" verwendet. Die Darstellung kann sich auf unterschiedlichen
     * Betriebssystemen unterscheiden. Es gibt eine weitere Methode
     * {@link #addTextToCanvas(String, double, double, double, boolean, Color, double, String)}, um die Schriftart
     * festzulegen.
     *
     * @param text     Der anzuzeigende Text.
     * @param x        x-Koordinate, bei welcher der Text angezeigt werden soll. 0 ist links.
     * @param y        y-Koordinate, bei welcher der Text angezeigt werden soll. 0 ist oben.
     * @param fontSize Die Schriftgröße, mindestens 5.
     * @param bold     Die Schriftart. Bei true wird die Schrift fettgedruckt.
     * @param color    Die Farbe, in der der Text angezeigt werden soll.
     * @param rotation Die Rotation der Schrift in Grad um den Mittelpunkt.
     */
    public void addTextToCanvas(String text, double x, double y, double fontSize, boolean bold, Color color, double rotation) {
        addTextToCanvas(text, x, y, fontSize, bold, color, rotation, "standardfont");
    }

    /**
     * Diese Methode hat dieselbe Funktionalität wie die Methode
     * {@link #addTextToCanvas(String, double, double, double, boolean, Color, double)}. Darüber hinaus kann eine
     * Schriftart als .ttf-Font-Datei angegeben werden. Die Schrift wird mit dem angegebenen Font ausgegeben. Dieser
     * Font wird auf allen Betriebssystemen gleich dargestellt.
     *
     * @param text     Der anzuzeigende Text.
     * @param x        x-Koordinate, bei welcher der Text angezeigt werden soll. 0 ist links.
     * @param y        y-Koordinate, bei welcher der Text angezeigt werden soll. 0 ist oben.
     * @param fontSize Die Schriftgröße, mindestens 5.
     * @param bold     Die Schriftart. Bei true wird die Schrift fettgedruckt.
     * @param color    Die Farbe, in der der Text angezeigt werden soll.
     * @param rotation Die Rotation der Schrift in Grad um den Mittelpunkt.
     * @param fontName Name des zu verwendenden Fonts. Das Font-File muss in einem Verzeichnis "src/resources" liegen
     *                 und auf ".ttf" enden.
     */
    public void addTextToCanvas(String text, double x, double y, double fontSize, boolean bold, Color color, double rotation, String fontName) {
        BufferedImage image = swingAdapter.createImageFromText(text, fontSize, color, bold, fontName);
        addImageToCanvasIfVisible(image, x, y, rotation);
    }

    /**
     * Erzeugt eine Grafik aus einer Datei. Die Datei muss im Verzeichnis "src/resources" liegen. Bitte den Namen der
     * Datei ohne Verzeichnisnamen angeben, z.B.<code>"raumschiff.png"</code>. Der Dateiname darf nur aus
     * Kleinbuchstaben bestehen.
     * <p>
     * Die Grafik wird auf die Leinwand (Canvas) übertragen, ohne die bisherigen Inhalte zu löschen.
     * <p>
     * Koordinaten werden ausgewertet: (0, 0) ist links oben {@link #GameView()}. Negative Koordinaten können verwendet
     * werden, um Grafiken teilweise anzuzeigen.
     * <p>
     * Die Größe der Grafik mit dem Parameter <code>scaleFactor</code> festgelegt werden. Beispielsweise bedeutet
     * <code>scaleFactor = 1</code>, dass das Bild in Originalgröße angezeigt wird.
     * <p>
     * Die Grafik kann mit einer Rotation dargestellt werden, dabei wird um den Mittelpunkt der Grafik rotiert. Eine
     * Rotation um 0° stellt das Bild ohne Rotation dar, bei 180° steht das Bild auf dem Kopf.
     *
     * @param imageFile        Der Dateiname des Bildes.
     * @param x                x-Koordinate, bei welcher das Bild angezeigt werden soll. 0 ist links.
     * @param y                y-Koordinate, bei welcher das Bild angezeigt werden soll. 0 ist oben.
     * @param imageScaleFactor Skalierungsfaktor des Bildes.
     * @param rotation         Die Rotation des Bildes in Grad um den Mittelpunkt.
     */
    public void addImageToCanvas(String imageFile, double x, double y, double imageScaleFactor, double rotation) {
        BufferedImage image = swingAdapter.createImageFromFile(imageFile, imageScaleFactor);
        addImageToCanvasIfVisible(image, x, y, rotation);
    }

    private void addImageToCanvasIfVisible(BufferedImage image, double x, double y, double rotation) {
        int xInt = (int) Math.ceil(
                x * swingAdapter.paintingPanel.windowsScaleFactor * swingAdapter.paintingPanel.panelScaleFactor);
        int yInt = (int) Math.ceil(
                y * swingAdapter.paintingPanel.windowsScaleFactor * swingAdapter.paintingPanel.panelScaleFactor);
        int width = image.getWidth();
        int height = image.getHeight();
        int diagonale = (int) Math.ceil(Math.sqrt(width * width + height * height));
        if (swingAdapter.paintingPanel.scaledBounds.intersects(new java.awt.Rectangle(xInt, yInt, diagonale, diagonale))) {
            canvas.addImageToCanvas(image, xInt, yInt, rotation);
        } else {
            statistic.invisiblePrintObjects++;
        }
    }

    /**
     * Diese Methode kann ein farbiges Oval auf die Leinwand (Canvas) zeichnen, ohne die bisherigen Inhalte zu löschen.
     * <p>
     * Die Koordinaten werden wie folgt ausgewertet: (0, 0) ist links oben {@link #GameView()}. Negative Koordinaten
     * können verwendet werden, um Ovale teilweise anzuzeigen.
     *
     * @param xCenter    x-Koordinate des Mittelpunkts des Ovals. 0 ist links.
     * @param yCenter    y-Koordinate des Mittelpunkts des Ovals. 0 ist oben.
     * @param width      Breite des Ovals.
     * @param height     Höhe des Ovals.
     * @param lineWeight Die Linienstärke des Ovals.
     * @param filled     Legt fest, ob das Oval gefüllt werden soll oder nicht.
     * @param color      Die Farbe des Ovals.
     */
    public void addOvalToCanvas(double xCenter, double yCenter, double width, double height, double lineWeight, boolean filled, Color color) {
        if (lineWeight < 0 || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height have to be positive numbers and lineWeight can't be negative.");
        }
        int xInt = (int) Math.ceil(xCenter - width / 2);
        int yInt = (int) Math.ceil(yCenter - height / 2);
        int widthInt = (int) Math.ceil(width);
        int heightInt = (int) Math.ceil(height);
        if (rectangleIntersectsGameViewBounds(xInt, yInt, widthInt, heightInt, lineWeight)) {
            canvas.addOvalToCanvas((int) Math.ceil(xCenter), (int) Math.ceil(yCenter), widthInt, heightInt, (int) Math.ceil(lineWeight), filled, color);
        }
    }

    private boolean rectangleIntersectsGameViewBounds(int x, int y, int width, int height, double lineWeight) {
        int halfLineWeight = (int) Math.round(lineWeight / 2);
        java.awt.Rectangle rect = new java.awt.Rectangle(
                x - halfLineWeight, y - halfLineWeight, width + halfLineWeight, height + halfLineWeight);
        boolean intersects = rect.intersects(swingAdapter.paintingPanel.bounds);
        if (!intersects) {
            statistic.invisiblePrintObjects++;
        }
        return intersects;
    }

    /**
     * Diese Methode kann ein farbiges Rechteck auf die Leinwand (Canvas) zeichnen, ohne die bisherigen Inhalte zu
     * löschen.
     * <p>
     * Die Koordinaten werden wie folgt ausgewertet: (0, 0) ist links oben {@link #GameView()}. Negative Koordinaten
     * können verwendet werden, um Rechtecke teilweise anzuzeigen.
     *
     * @param x          x-Koordinate der linken oberen Ecke des Rechtecks. 0 ist links.
     * @param y          y-Koordinate der linken oberen Ecke des Rechtecks. 0 ist oben.
     * @param width      Breite des Rechtecks.
     * @param height     Höhe des Rechtecks.
     * @param lineWeight Die Linienstärke.
     * @param filled     Legt fest, ob das Rechteck gefüllt werden soll oder nicht.
     * @param color      Die Farbe des Rechtecks.
     */
    public void addRectangleToCanvas(double x, double y, double width, double height, double lineWeight, boolean filled, Color color) {
        if (lineWeight < 0 || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height have to be positive numbers and lineWeight can't be negative.");
        }
        int xInt = (int) Math.ceil(x);
        int yInt = (int) Math.ceil(y);
        int widthInt = (int) Math.ceil(width);
        int heightInt = (int) Math.ceil(height);
        if (rectangleIntersectsGameViewBounds(xInt, yInt, widthInt, heightInt, lineWeight)) {
            canvas.addRectangleToCanvas(xInt, yInt, widthInt, heightInt, (int) Math.round(lineWeight), filled, color);
        }
    }

    /**
     * Diese Methode kann eine farbige Linie auf die Leinwand (Canvas) zeichnen, ohne die bisherigen Inhalte zu
     * löschen.
     * <p>
     * Die Koordinaten werden wie folgt ausgewertet: (0, 0) ist links oben {@link #GameView()}. Negative Koordinaten
     * können verwendet werden, um Linien teilweise anzuzeigen.
     *
     * @param xStart     x-Koordinate des Startpunkts der Linie. 0 ist links.
     * @param yStart     y-Koordinate des Startpunkts der Linie. 0 ist oben.
     * @param xEnd       x-Koordinate des Endpunkts der Linie. 0 ist links.
     * @param yEnd       y-Koordinate des Endpunkts der Linie. 0 ist oben.
     * @param lineWeight Die Linienstärke.
     * @param color      Die Farbe der Linie.
     */
    public void addLineToCanvas(double xStart, double yStart, double xEnd, double yEnd, double lineWeight, Color color) {
        if (lineWeight < 0) {
            throw new IllegalArgumentException(Tools.NEGATIVE_LINEWEIGHT);
        }
        int xStartInt = (int) Math.round(xStart);
        int yStartInt = (int) Math.round(yStart);
        int xEndInt = (int) Math.round(xEnd);
        int yEndInt = (int) Math.round(yEnd);
        int[] xs = new int[]{xStartInt, xEndInt};
        int[] ys = new int[]{yStartInt, yEndInt};
        if (lineIntersectsGameViewBounds(xs, ys, lineWeight)) {
            canvas.addLineToCanvas(xStartInt, yStartInt, xEndInt, yEndInt, (int) Math.round(lineWeight), color);
        }
    }

    private boolean lineIntersectsGameViewBounds(int[] xs, int[] ys, double lineWeight) {
        IntSummaryStatistics statX = Arrays.stream(xs).summaryStatistics();
        IntSummaryStatistics statY = Arrays.stream(ys).summaryStatistics();
        return rectangleIntersectsGameViewBounds(statX.getMin(), statY.getMin(),
                statX.getMax() - statX.getMin(), statY.getMax() - statY.getMin(), lineWeight);
    }

    /**
     * Diese Methode kann eine farbige Poly-Linie (eine Linie zwischen mehreren Punkten) auf die Leinwand (Canvas)
     * zeichnen, ohne die bisherigen Inhalte zu löschen. Dazu müssen alle Punkte der Poly-Linie angegeben werden.
     * <p>
     * Die Koordinaten werden wie folgt ausgewertet: (0, 0) ist links oben {@link #GameView()}. Negative Koordinaten
     * können verwendet werden, um die Linien teilweise anzuzeigen.
     *
     * @param xCoordinates Die x-Koordinaten der Punkte der Poly-Linie.
     * @param yCoordinates Die y-Koordinaten der Punkte der Poly-Linie.
     * @param lineWeight   Die Linienstärke.
     * @param color        Die Farbe der Poly-Linie.
     */
    public void addPolyLineToCanvas(double[] xCoordinates, double[] yCoordinates, double lineWeight, Color color) {
        if (lineWeight < 0) {
            throw new IllegalArgumentException(Tools.NEGATIVE_LINEWEIGHT);
        }
        int[] xs = convertDoubleToIntArray(xCoordinates);
        int[] ys = convertDoubleToIntArray(yCoordinates);
        if (lineIntersectsGameViewBounds(xs, ys, lineWeight)) {
            canvas.addPolyLineToCanvas(xs, ys, (int) Math.round(lineWeight), color);
        }
    }

    /**
     * Diese Methode kann ein farbiges Polygon auf die Leinwand (Canvas) zeichnen, ohne die bisherigen Inhalte zu
     * löschen. Dazu müssen alle Punkte des Polygons angegeben werden. Der Letzte angegebene Punkt wird mit dem ersten
     * Punkt des Polygons verbunden.
     * <p>
     * Die Koordinaten werden wie folgt ausgewertet: (0, 0) ist links oben {@link #GameView()}. Negative Koordinaten
     * können verwendet werden, um das Polygon teilweise anzuzeigen.
     *
     * @param xCoordinates Die x-Koordinaten der Punkte des Polygons.
     * @param yCoordinates Die y-Koordinaten der Punkte des Polygons.
     * @param lineWeight   Die Linienstärke.
     * @param filled       Legt fest, ob das Polygon gefüllt werden soll oder nicht.
     * @param color        Die Farbe des Polygons.
     */
    public void addPolygonToCanvas(double[] xCoordinates, double[] yCoordinates, double lineWeight, boolean filled, Color color) {
        if (lineWeight < 0) {
            throw new IllegalArgumentException(Tools.NEGATIVE_LINEWEIGHT);
        }
        int[] xs = convertDoubleToIntArray(xCoordinates);
        int[] ys = convertDoubleToIntArray(yCoordinates);
        if (lineIntersectsGameViewBounds(xs, ys, lineWeight)) {
            canvas.addPolygonToCanvas(xs, ys, (int) Math.round(lineWeight), filled, color);
        }
    }

    private int[] convertDoubleToIntArray(double[] original) {
        int[] converted = new int[original.length];
        for (int i = 0; i < converted.length; i++) {
            converted[i] = (int) Math.round(original[i]);
        }
        return converted;
    }

    /**
     * Legt fest, ob die Maus im Fenster benutzt werden soll. Falls sie nicht benutzt wird, wird der Cursor der Maus auf
     * die Default-Ansicht zurückgesetzt und die Maus wird ausgeblendet. Falls sie benutzt wird, werden Maus-Ereignisse
     * erzeugt, die verwendet werden können. Die Standardeinstellung ist false.
     *
     * @param useMouse Legt fest, ob die Maus im Fenster benutzt werden soll.
     */
    public void useMouse(boolean useMouse) {
        mouse.useMouse(useMouse);
    }

    /**
     * Liest den Inhalt einer Textdatei als String ein. Die Datei muss im Verzeichnis "src/resources" liegen. Bitte den
     * Namen der Datei ohne Verzeichnisnamen angeben, z.B.<code>"raumschiff.txt"</code>. Der Dateiname darf nur aus
     * Kleinbuchstaben bestehen.
     * <p>
     * Der Inhalt der Datei wird als ein einziger String zurückgegeben. Wenn die Datei nicht existiert oder nicht
     * gelesen werden kann, wird eine Ausnahme geworfen.
     * <p>
     * Achtung der Festplattenzugriff ist langsam, sie sollten diese Methode am besten nur ein einziges Mal aufrufen.
     *
     * @param fileName Der Name der Datei, die gelesen werden soll. Die Textdatei muss sich im Verzeichnis "resources"
     *                 befinden.
     * @return Ein String, der den Inhalt der Textdatei darstellt. Gibt "Datei nicht gefunden" zurück, wenn die Datei im
     * Ressourcenverzeichnis nicht existiert.
     * @throws IllegalArgumentException Falls die Datei nicht gelesen werden kann oder andere Fehler auftreten.
     */
    public String readTextFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream("resources/" + fileName),
                () -> "Die Textdatei \"" + fileName + "\" konnte nicht gefunden werden!");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new UncheckedIOException("Fehler beim Lesen der Testdatei: " + fileName, e);
        }
    }

    /**
     * Gibt zurück, ob die Maus eingeschaltet ist.
     *
     * @return true, falls die Maus eingeschaltet ist.
     */
    public boolean mouseIsEnabled() {
        return mouse.useMouse;
    }

    /**
     * Legt ein neues Symbol für den Maus-Cursor fest. Die Bild-Datei muss im Verzeichnis "src/resources" liegen. Bitte
     * den Namen der Datei ohne Verzeichnisnamen angeben, z.B. <code>changeMouseCursor("cursor.png", false)</code>.
     *
     * @param cursorImageFileName Name der Bild-Datei. Die Bild-Datei muss in einem Verzeichnis "src/resources" liegen.
     * @param centered            Gibt an, ob der Hotspot des Cursors in der Mitte des Symbols oder oben links liegen
     *                            soll.
     */
    public void updateMouseCursor(String cursorImageFileName, boolean centered) {
        mouse.setMouseCursor(cursorImageFileName, centered);
    }

    /**
     * Der Maus-Cursor wird auf das Standard-Icon zurückgesetzt.
     */
    public void resetMouseCursor() {
        mouse.setStandardMouseCursor();
    }

    /**
     * Falls die Maus mit {@link #useMouse(boolean)} aktiviert wurde, liefert diese Methode alle Mausereignisse, die
     * seit dem letzten Aufruf dieser Methode aufgelaufen sind als Array zurück. Es werden maximal die neuesten 25
     * Ereignisse zurückgegeben, alte Ereignisse werden gelöscht. Diese Methode ist geeignet um die Texteingaben vom
     * Benutzer zu realisieren.
     * <p>
     * Das Array enthält Ereignisse vom Typ {@link MouseEvent}. Das Ereignis enthält Koordinaten auf der Leinwand
     * (Canvas) und die Information, ob die Maus gedrückt, losgelassen, geklickt oder nur bewegt wurde. Um
     * festzustellen, wie die Maus betätigt wurde, kann der Typ des Ereignisses mit {@link MouseEvent#getID()} abgefragt
     * werden. Folgende Konstanten werden weitergeleitet:
     * <br>
     * <code>MouseEvent.MOUSE_PRESSED</code> <br>
     * <code>MouseEvent.MOUSE_RELEASED</code> <br>
     * <code>MouseEvent.MOUSE_CLICKED</code> <br>
     * <code>MouseEvent.MOUSE_MOVED</code> <br>
     * <br>
     * Die Fensterkoordinaten können mit den Methoden<br> {@link MouseEvent#getX()} = X-Koordinate<br>
     * {@link MouseEvent#getY()} = Y-Koordinate<br> abgerufen werden, um X und Y-Koordinate des Ereignisses zu
     * bestimmen.<br>
     * <br>
     * Beispiel zur Erkennung einer gedrückten Maustaste:<br>
     *
     * <pre>{@code
     * package demo;
     *
     * import java.awt.event.MouseEvent;
     *
     * public class MouseEventTest {
     *   private final GameView gameView;
     *
     *    public MouseEventTest() {
     *     gameView = new GameView();
     *     gameView.updateWindowTitle("UFO-Fight");
     *     gameView.useMouse(true);
     *     startGameLoop();
     *   }
     *
     *   private void startGameLoop() {
     *     int x = 0;
     *     int y = 0;
     *     while (gameView.isVisible()) {
     *       MouseEvent[] mouseEvents = gameView.mouseEvents();
     *       for (MouseEvent mouseEvent : mouseEvents) {
     *         if (mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) {
     *           x = mouseEvent.getX();
     *           y = mouseEvent.getY();
     *         }
     *       }
     *       gameView.addTextToCanvas("X=" + x + " Y=" + y, x, y, 12, false, Color.WHITE, 0);
     *       gameView.plotCanvas();
     *     }
     *   }
     * }
     * }</pre>
     * Mit {@link MouseEvent#getButton()} lässt sich ermitteln, welche Maustaste betätigt wurde (links, rechts oder
     * die Mitte).
     *
     * @return Alle Mausereignisse seit dem letzten Aufruf dieser Methode.
     * @see MouseEvent
     */
    public MouseEvent[] mouseEvents() {
        return mouse.pollMouseEvents();
    }

    /**
     * Diese Methode liefert alle gerade im Moment gedrückten Tasten als <code>KeyCode</code> der Klasse
     * {@link KeyEvent} als Array zurück. Es handelt sich dabei um Ganzzahlen vom Typ <code>Integer</code>. Die Tasten
     * sind in der Reihenfolge enthalten, in der sie gedrückt wurden. Diese Methode ist geeignet um die Steuerung von
     * Spielfiguren zu realisieren.
     * <p>
     * Ein Abgleich der KeyCodes kann über Konstanten der Klasse {@link KeyEvent} erfolgen. Beispielsweise kann die
     * Leertaste mithilfe der Konstante {@link KeyEvent#VK_SPACE} abgeglichen werden.
     * <pre>{@code
     * package demo;
     *
     * import java.awt.event.KeyEvent;
     *
     * public class PressedKeys {
     *   private final GameView gameView;
     *
     *   public PressedKeys() {
     *     gameView = new GameView();
     *     gameView. setWindowTitle("UFO-Fight");
     *     startGameLoop();
     *   }
     *
     *   private void startGameLoop() {
     *     while (gameView.isVisible()) {
     *       Integer[] pressedKeys = gameView.keyCodesOfCurrentlyPressedKeys();
     *       String result = "Keine Taste gedrückt";
     *       for (int keyCode : pressedKeys) {
     *         if (keyCode == KeyEvent.VK_UP) {
     *           result = "UP\n";
     *         } else if (keyCode == KeyEvent.VK_DOWN) {
     *           result = "Down\n";
     *         } else if (keyCode == KeyEvent.VK_LEFT) {
     *           result = "Left\n";
     *         } else if (keyCode == KeyEvent.VK_RIGHT) {
     *           result = "Right\n";
     *         } else if (keyCode == KeyEvent.VK_SPACE) {
     *           result = "Space\n";
     *         }
     *       }
     *       gameView.addTextToCanvas(result, 0, 0, 12, false, Color.WHITE, 0);
     *       gameView.plotCanvas();
     *     }
     *   }
     * }
     * }</pre>
     *
     * @return Alle gerade gedrückten Tasten als <code>KeyCode</code> in einem Array.
     * @see KeyEvent
     */
    public Integer[] keyCodesOfCurrentlyPressedKeys() {
        return keyboard.keyCodesOfCurrentlyPressedKeys();
    }

    /**
     * Zeigt an, ob die übergebene Taste gerade gedrückt wird.
     * <p>
     *
     * @param keyCode Ein KeyCode von <code>java.awt.event.KeyEvent</code>, z.B. <code>KeyEvent.VK_A</code> für den
     *                Buchstaben A.
     * @return True, falls diese Taste gerade gedrückt wird.
     */
    public boolean keyCurrentlyPressed(int keyCode) {
        return keyboard.currentlyPressedKeys.contains(keyCode);
    }

    /**
     * Löscht alle bisher aufgelaufenen Tastenereignisse.
     */
    public void clearKeyEvents() {
        keyboard.clearKeyEvents();
    }

    /**
     * Liefert alle Tastenereignisse (Taste gedrückt und wieder losgelassen), die seit dem letzten Aufruf dieser Methode
     * aufgelaufen sind als Array zurück. Es werden maximal die neuesten 20 Ereignisse zurückgegeben, alte Ereignisse
     * werden gelöscht.
     * <p>
     * Sichtbare Zeichen lassen sich mit der Methode {@link KeyEvent#getKeyChar()} direkt auslesen.
     *
     * <pre>{@code
     * package demo;
     *
     * import java.awt.event.KeyEvent;
     *
     * public class KeyTypedTest {
     *   private final GameView gameView;
     *
     *   public KeyTypedTest() {
     *     gameView = new GameView();
     *     gameView. setWindowTitle("UFO-Fight");
     *     startGameLoop();
     *   }
     *
     *   private void startGameLoop() {
     *     while (gameView.isVisible()) {
     *       KeyEvent[] typedKeys = gameView.typedKeys();
     *       for (KeyEvent typedKey : typedKeys) {
     *         gameView.addTextToCanvas("Taste: " + typedKey.getKeyChar(), 0, 0, 12, false, Color.WHITE, 0);
     *         gameView.plotCanvas();
     *       }
     *     }
     *   }
     * }
     * }</pre>
     *
     * @return die letzten 20 <code>KeyEvent</code> Ereignisse für gedrückte Tasten seit dem letzten Aufruf dieser
     * Methode.
     * @see KeyEvent
     * @see #keyCodesOfCurrentlyPressedKeys()
     */
    public KeyEvent[] typedKeys() {
        return keyboard.typedKeys();
    }

    /**
     * Spielt einen Sound ab (eine .wav-Datei). Die Sound-Datei muss in einem Verzeichnis "src/resources" liegen. Bitte
     * den Namen der Datei ohne Verzeichnisnamen angeben, z.B. <code>playSound("boom.wav", false)</code>. Der Dateiname
     * darf nur aus Kleinbuchstaben bestehen.
     * <p>
     * Der Sound beendet sich selbst, sobald er fertig abgespielt wurde. Der Parameter "replay" kann genutzt werden, um
     * den Sound endlos zu wiederholen. Mit der Methode {@link #stopSound(int)} kann ein Sound frühzeitig beendet
     * werden. Mit der Methode {@link #stopAllSounds()} können alle laufenden Sounds beendet werden. Achten Sie auf
     * Groß- und Kleinschreibung beim Soundfile!
     *
     * @param soundFile Name der Sound-Datei. Die Sound-Datei muss in einem Verzeichnis "src/resources" liegen und auf
     *                  ".wav" enden.
     * @param replay    Legt fest, ob der Sound endlos wiederholt werden soll.
     * @return Die eindeutige Identifikationsnummer des Soundfiles wird zurückgegeben. Diese Nummer kann genutzt werden
     * um mit der Methode {@link #stopSound(int)} das Abspielen des Sounds zu beenden.
     */
    public int playSound(String soundFile, boolean replay) {
        return this.sound.playSound(soundFile, replay);
    }

    /**
     * Stoppt den Sound mit der angegebenen Nummer. Falls der Sound schon gestoppt wurde, passiert nichts.
     *
     * @param id Die eindeutige Identifikationsnummer des Soundfiles, das gestoppt werden soll.
     */
    public void stopSound(int id) {
        sound.stopSound(id);
    }

    /**
     * Stoppt alle gerade spielenden Sounds.
     */
    public void stopAllSounds() {
        sound.stopAllSounds();
    }

    /**
     * Liefert die Zeit in Millisekunden, die seit dem Start von GameView verstrichen sind.
     *
     * @return Zeit in Millisekunden.
     */
    public int gameTimeInMilliseconds() {
        return timer.gameTimeInMilliseconds();
    }

    /**
     * Ein Timer liefert einen zeitgesteuerten booleschen Wert. Der Ablauf ist wie folgt:
     * <ol>
     * <li>Der Timer is inaktiv.</li>
     * <li>Der Timer wird gestartet, sobald er zum ersten Mal aufgerufen wird.</li>
     * <li>Der Timer liefert für den angegebenen Zeitraum false.</li>
     * <li>Der Timer liefert für den angegebenen Zeitraum true.</li>
     * <li>Der Timer wird auf inaktiv zurückgesetzt. Beim nächsten Aufruf wird er wieder neu gestartet.</li>
     * </ol>
     * <p>
     * Beispielhafte Anwendung eines Timers der für 600 Millisekunden false und 300 Millisekunden true liefert:
     * <pre>{@code
     * if (gameView.timer(600, 300, this)) {
     *       showShot = true;
     * }
     * }</pre>
     * Wenn der Timer jeweils nur ein einziges Mal true bzw. false liefern soll, anstatt über einen Zeitraum hinweg, dann muss
     * der entsprechende Parameter auf 0 gesetzt werden. Sind beide Parameter auf 0 gesetzt, liefert der Timer
     * abwechselnd false und true.
     * <p>
     * Ein Timer muss durch das zugehörige Objekt eindeutig identifizierbar gemacht werden.
     *
     * @param millisecondsFalse Anzahl der Millisekunden, in denen der Timer false liefert.
     * @param millisecondsTrue  Anzahl der Millisekunden, in denen der Timer true liefert.
     * @param id                Das aufrufende Objekt, zu dem dieser Timer gehört (<code>this</code>). Dient zur
     *                          eindeutigen Identifikation des Timers.
     * @return Abwechselnd false und true, in Abhängigkeit von den übergebenen Parametern.
     */
    public boolean timer(int millisecondsFalse, int millisecondsTrue, Object id) {
        return timer.timer(millisecondsFalse, millisecondsTrue, id);
    }

    /**
     * Alle laufenden Timer des übergebenen Objekts werden abgebrochen und auf ihren Ausgangszustand zurückgesetzt.
     *
     * @param id Das Objekt, dessen Timer zurückgesetzt werden sollen.
     */
    public void resetTimers(Object id) {
        timer.resetTimers(id);
    }

    /**
     * Alle laufenden Timer werden abgebrochen und auf ihren Ausgangszustand zurückgesetzt.
     */
    public void resetAllTimers() {
        timer.resetAllTimers();
    }

    /**
     * Diese Methode liefert eine Umgebung, die für die Erstellung von Tests genutzt werden kann.
     *
     * @return Eine Testumgebung.
     */
    public TestEnvironment getTestEnvironment() {
        return testEnvironment;
    }

    private abstract static class PrintObject {
        protected final int x;
        protected final int y;
        protected final Color color;
        PrintType type;

        protected PrintObject(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

        protected enum PrintType {
            OVAL, RECTANGLE, IMAGE_OBJECT, POLYGON, POLYLINE, LINE
        }
    }

    private static class Oval extends PrintObject {
        private final int width;
        private final int height;
        private final int lineWeight;
        private final boolean filled;

        private Oval(int xCenter, int yCenter, int width, int height, int lineWeight, boolean filled, Color color) {
            super(xCenter, yCenter, color);
            this.type = PrintType.OVAL;
            this.width = width;
            this.height = height;
            this.lineWeight = lineWeight;
            this.filled = filled;
        }
    }

    private static class Rectangle extends PrintObject {
        private final int width;
        private final int height;
        private final int lineWeight;
        private final boolean filled;

        private Rectangle(int x, int y, int width, int height, int lineWeight, boolean filled, Color color) {
            super(x, y, color);
            this.type = PrintType.RECTANGLE;
            this.width = width;
            this.height = height;
            this.lineWeight = lineWeight;
            this.filled = filled;
        }
    }

    private static class Line extends PrintObject {
        private final int xEnd;
        private final int yEnd;
        private final int lineWeight;

        private Line(int xStart, int yStart, int xEnd, int yEnd, int lineWeight, Color color) {
            super(xStart, yStart, color);
            this.type = PrintType.LINE;
            this.xEnd = xEnd;
            this.yEnd = yEnd;
            this.lineWeight = lineWeight;
        }
    }

    private static class Polygon extends PrintObject {
        private final int[] xCoordinates;
        private final int[] yCoordinates;
        private final int lineWeight;
        private final boolean filled;

        private Polygon(int[] xCoordinates, int[] yCoordinates, int lineWeight, boolean filled, Color color) {
            super(xCoordinates[0], yCoordinates[0], color);
            this.type = PrintType.POLYGON;
            this.xCoordinates = xCoordinates;
            this.yCoordinates = yCoordinates;
            this.lineWeight = lineWeight;
            this.filled = filled;
        }
    }

    private static class PolyLine extends PrintObject {
        private final int[] xCoordinates;
        private final int[] yCoordinates;
        private final int lineWeight;

        private PolyLine(int[] xCoordinates, int[] yCoordinates, int lineWeight, Color color) {
            super(xCoordinates[0], yCoordinates[0], color);
            this.type = PrintType.POLYLINE;
            this.xCoordinates = xCoordinates;
            this.yCoordinates = yCoordinates;
            this.lineWeight = lineWeight;
        }
    }

    private static class ImageObject extends PrintObject {
        private final BufferedImage image;
        private final double rotation;

        private ImageObject(int x, int y, BufferedImage image, double rotation) {
            super(x, y, Color.BLACK);
            this.type = PrintType.IMAGE_OBJECT;
            this.image = image;
            this.rotation = rotation;
        }
    }

    private static class Canvas {
        private Color backgroundColor;
        private ArrayList<PrintObject> printObjects;

        private Canvas() {
            this.backgroundColor = Color.black;
            this.printObjects = new ArrayList<>(1500);
        }

        private void setBackgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        private void addImageToCanvas(BufferedImage image, int x, int y, double rotation) {
            printObjects.add(new ImageObject(x, y, image, rotation));
        }

        private void addRectangleToCanvas(int x, int y, int width, int height, int lineWeight, boolean filled, Color color) {
            printObjects.add(new Rectangle(x, y, width, height, lineWeight, filled, color));
        }

        private void addLineToCanvas(int xStart, int yStart, int xEnd, int yEnd, int lineWeight, Color color) {
            printObjects.add(new Line(xStart, yStart, xEnd, yEnd, lineWeight, color));
        }

        private void addOvalToCanvas(int xCenter, int yCenter, int width, int height, int lineWeight, boolean filled, Color color) {
            printObjects.add(new Oval(xCenter, yCenter, width, height, lineWeight, filled, color));
        }

        private void addPolygonToCanvas(int[] xCoordinates, int[] yCoordinates, int lineWeight, boolean filled, Color color) {
            if (xCoordinates.length != yCoordinates.length) {
                throw new InputMismatchException("Die Anzahl der X- und Y-Koordinaten ist nicht gleich!");
            }
            printObjects.add(new Polygon(xCoordinates, yCoordinates, lineWeight, filled, color));
        }

        private void addPolyLineToCanvas(int[] xCoordinates, int[] yCoordinates, int lineWeight, Color color) {
            if (xCoordinates.length != yCoordinates.length) {
                throw new InputMismatchException("Die Anzahl der X- und Y-Koordinaten ist nicht gleich!");
            }
            printObjects.add(new PolyLine(xCoordinates, yCoordinates, lineWeight, color));
        }
    }

    private static class Frame extends JFrame {
        private final JPanel statusBar;
        private Mouse mouse;
        private Keyboard keyboard;
        private JLabel statusLabelLinks;

        private Frame(PaintingPanel paintingPanel) {

            statusBar = createStatusBar();

            JPanel center = new JPanel(new GridBagLayout());
            center.setBackground(Color.BLACK);
            center.add(paintingPanel);


            // Struktur
            paintingPanel.setPreferredSize(new Dimension(GameView.WIDTH, GameView.HEIGHT));
            JPanel paintingPanelAndStatusBar = new JPanel(new BorderLayout(0, 0));
            paintingPanelAndStatusBar.setBackground(Color.BLACK);
            paintingPanelAndStatusBar.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)), BorderLayout.NORTH);
            paintingPanelAndStatusBar.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)), BorderLayout.EAST);
            paintingPanelAndStatusBar.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)), BorderLayout.WEST);
            paintingPanelAndStatusBar.add(center, BorderLayout.CENTER);
            paintingPanelAndStatusBar.add(statusBar, BorderLayout.SOUTH);
            add(paintingPanelAndStatusBar);

            // Eigenschaften
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setTitle(Version.getStandardTitle());
            paintingPanel.setFocusable(false);
            setResizable(true);


            // Listeners
            KeyListener keyListener = new KeyListener() {

                @Override
                public void keyTyped(KeyEvent keyEvent) {
                    all(keyEvent);
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {
                    all(keyEvent);
                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    all(keyEvent);
                }

                private void all(KeyEvent keyEvent) {
                    if (keyboard != null) {
                        keyboard.update(keyEvent);
                    }
                }
            };
            addKeyListener(keyListener);

            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent mouseEvent) {
                    all(mouseEvent);
                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                    all(mouseEvent);
                }

                @Override
                public void mouseMoved(MouseEvent mouseEvent) {
                    all(mouseEvent);
                }

                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    all(mouseEvent);
                }

                private void all(MouseEvent mouseEvent) {
                    if (mouse != null) {
                        mouse.update(mouseEvent);
                    }
                }
            };
            paintingPanel.addMouseMotionListener(mouseAdapter);
            paintingPanel.addMouseListener(mouseAdapter);

            final javax.swing.Timer packTimer = new javax.swing.Timer(500, actionEvent -> {
                if (getExtendedState() != MAXIMIZED_BOTH) {
                    Point location = getLocation();
                    pack();
                    setLocation(location);
                }
            });
            packTimer.setRepeats(false);

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    double scalingFactor = Math.min(
                            paintingPanel.getParent().getWidth() * 1d / GameView.WIDTH,
                            paintingPanel.getParent().getHeight() * 1d / GameView.HEIGHT);
                    int newWidth = (int) Math.round(GameView.WIDTH * scalingFactor);
                    int newHeight = (int) Math.round(GameView.HEIGHT * scalingFactor);
                    paintingPanel.setPreferredSize(new Dimension(newWidth, newHeight));
                    paintingPanel.setMinimumSize(new Dimension(newWidth, newHeight));
                    paintingPanel.setSize(new Dimension(newWidth, newHeight));
                    paintingPanel.setMaximumSize(new Dimension(newWidth, newHeight));
                    if (packTimer.isRunning()) {
                        packTimer.restart();
                    } else {
                        packTimer.start();
                    }
                    paintingPanel.updateScaleFactor();
                    revalidate();
                }
            });

            // Location und Ausgeben
            int newWidth = (int) (GameView.WIDTH * 1.3);
            int newHeight = (int) (GameView.HEIGHT * 1.3);
            paintingPanel.setPreferredSize(new Dimension(newWidth, newHeight));

            pack();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (getHeight() > screenSize.height || getWidth() > screenSize.width) {
                newWidth = GameView.WIDTH * 8 / 10;
                newHeight = GameView.HEIGHT * 8 / 10;
                paintingPanel.setPreferredSize(new Dimension(newWidth, newHeight));
                pack();
            }
            setLocationRelativeTo(null);
            if (instances > 0) {
                int offset = (instances - 1) * 25;
                int x = getLocation().x + offset;
                int y = getLocation().y + offset;
                setLocation(x, y);
            }
        }

        private JPanel createStatusBar() {
            JPanel bar = new JPanel(new BorderLayout());
            bar.setBorder(BorderFactory.createRaisedBevelBorder());
            bar.setBackground(Color.WHITE);
            bar.setForeground(Color.BLACK);

            statusLabelLinks = new JLabel();
            statusLabelLinks.setBackground(Color.WHITE);
            statusLabelLinks.setForeground(Color.BLACK);
            statusLabelLinks.setHorizontalAlignment(JLabel.LEFT);

            JLabel statusLabelRechts = new JLabel(Version.getStatusSignature());
            statusLabelRechts.setBackground(Color.WHITE);
            statusLabelRechts.setForeground(Color.BLACK);
            statusLabelRechts.setHorizontalAlignment(JLabel.RIGHT);

            bar.add(statusLabelLinks, BorderLayout.WEST);
            bar.add(statusLabelRechts, BorderLayout.EAST);

            return bar;
        }

        private void registerListeners(Mouse mouse, Keyboard keyboard) {
            // Klassen
            this.mouse = mouse;
            this.keyboard = keyboard;
        }

        private JLabel getStatusLabelLinks() {
            return statusLabelLinks;
        }

        private JPanel getStatusBar() {
            return statusBar;
        }
    }

    private static class Mouse implements ActionListener {
        private static final int MOUSE_EVENT_BUFFER_SIZE = 25;
        private final SwingAdapter swingAdapter;
        private final javax.swing.Timer invisibleMouseTimer;
        private final ArrayBlockingQueue<MouseEvent> mousePointerEvents;
        private boolean invisibleMouseCursor;
        private boolean invisibleMouseCursorMoved;
        private boolean useMouse;

        private Mouse(SwingAdapter swingAdapter) {
            this.swingAdapter = swingAdapter;
            this.invisibleMouseCursor = false;
            this.invisibleMouseCursorMoved = true;
            this.mousePointerEvents = new ArrayBlockingQueue<>(MOUSE_EVENT_BUFFER_SIZE, true);
            this.invisibleMouseTimer = new javax.swing.Timer(500, this);
            setMouseInvisible();
        }

        private void setMouseInvisible() {
            useMouse = false;
            setInvisibleMouseCursor();
            startInvisibleMouseTimer();
        }

        private void setInvisibleMouseCursor() {
            invisibleMouseCursor = true;
            swingAdapter.setInvisibleMouseCursor();
        }

        private void startInvisibleMouseTimer() {
            if (!invisibleMouseTimer.isRunning()) {
                invisibleMouseTimer.start();
            }
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (invisibleMouseCursorMoved) {
                if (invisibleMouseCursor) {
                    setStandardMouseCursor();
                }
                invisibleMouseCursorMoved = false;
            } else {
                if (!invisibleMouseCursor) {
                    setInvisibleMouseCursor();
                }
            }
        }

        private void useMouse(boolean enableMouse) {
            if (enableMouse == this.useMouse) {
                return;
            }
            if (enableMouse) {
                this.useMouse = true;
                setStandardMouseCursor();
                invisibleMouseTimer.stop();
            } else {
                setMouseInvisible();
            }
        }

        private void setStandardMouseCursor() {
            this.invisibleMouseCursor = false;
            swingAdapter.setStandardMouseCursor();
        }

        private void setMouseCursor(String cursorImageFileName, boolean centered) {
            this.invisibleMouseCursor = false;
            swingAdapter.setMouseCursor(cursorImageFileName, centered);
        }

        private void update(MouseEvent mouseEvent) {
            if (useMouse) {
                int mouseEventY = GameView.HEIGHT * mouseEvent.getY() / swingAdapter.getTextDisplaySize().height;
                int mouseEventX = GameView.WIDTH * mouseEvent.getX() / swingAdapter.getTextDisplaySize().width;
                MouseEvent fixedMouseEvent = new MouseEvent(mouseEvent.getComponent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiersEx(), mouseEventX, mouseEventY, mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), mouseEvent.getButton());
                if (mousePointerEvents.size() == MOUSE_EVENT_BUFFER_SIZE) {
                    mousePointerEvents.remove();
                }
                mousePointerEvents.add(fixedMouseEvent);
            } else {
                invisibleMouseCursorMoved = true;
            }
        }

        private MouseEvent[] pollMouseEvents() {
            synchronized (mousePointerEvents) {
                MouseEvent[] events = mousePointerEvents.toArray(new MouseEvent[0]);
                mousePointerEvents.clear();
                return events;
            }
        }
    }

    private static class Sound {
        private final ConcurrentHashMap<Integer, Clip> clips;
        private final ConcurrentHashMap<String, byte[]> storedSoundBytes;
        private final ExecutorService executor;

        private Sound() {
            clips = new ConcurrentHashMap<>();
            storedSoundBytes = new ConcurrentHashMap<>();
            executor = Executors.newCachedThreadPool();
        }

        private void loadSoundBytes(String soundFile) {
            storedSoundBytes.computeIfAbsent(soundFile, key -> {
                try (InputStream stream = GameView.class.getResourceAsStream(Tools.RESOURCE_PREFIX + key)) {
                    return Objects.requireNonNull(stream).readAllBytes();
                } catch (IOException e) {
                    throw new UncheckedIOException("Soundfile \"" + key + "\" konnte nicht gelesen werden!", e);
                }
            });
        }

        private Clip createClipFromBytes(byte[] soundBytes) {
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(soundBytes))) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                return clip;
            } catch (IOException e) {
                throw new UncheckedIOException("Fehler beim Lesen der Sounddatei", e);
            } catch (LineUnavailableException | UnsupportedAudioFileException e) {
                throw new IllegalStateException("Sound kann nicht geladen werden!", e);
            }
        }

        private int playSound(String soundFile, boolean loop) {
            loadSoundBytes(soundFile);
            int id = soundFile.hashCode();
            executor.submit(() -> {
                Clip clip = createClipFromBytes(storedSoundBytes.get(soundFile));
                addLineListener(id, clip);
                playClip(id, clip, loop);
            });

            return id;
        }

        private void addLineListener(int id, Clip clip) {
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clips.remove(id);
                    executor.submit(() -> {
                        clip.flush();
                        clip.close();
                    });
                }
            });
        }

        private void playClip(int id, Clip clip, boolean loop) {
            clips.put(id, clip);
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
        }

        private void stopSound(int id) {
            Clip clip = clips.remove(id);
            if (clip != null) {
                clip.stop();
                executor.submit(() -> {
                    clip.flush();
                    clip.close();
                });
            }
        }

        private void stopAllSounds() {
            clips.keySet().forEach(this::stopSound);
        }
    }

    private static class SwingAdapter {
        private static final int IMAGE_MAP_LIMIT_IN_MB = 1000;
        private final PaintingPanel paintingPanel;
        private final Frame frame;
        private final ConcurrentHashMap<String, Font> storedFonts;
        private final HashMap<Integer, BufferedImage> imageMap;
        private Sound sound;
        private Mouse mouse;
        private Font activeFont;
        private HashMap<Character, Color> colorMap;
        private double sizeOfImageMapInMB;
        private int imageMapRefreshCounter;
        private volatile boolean blockUntilFontIsLoaded;

        private SwingAdapter(Statistic statistic) {
            paintingPanel = new PaintingPanel(statistic);
            frame = new Frame(paintingPanel);
            activeFont = new Font("Monospaced", Font.PLAIN, 15);
            initColorMap();
            imageMap = new HashMap<>();
            storedFonts = new ConcurrentHashMap<>();
            storedFonts.put("standardfont", activeFont);
        }

        private void initialize(boolean testEnvironmentOnly) {
            showWindowAndCreateBufferStrategy(testEnvironmentOnly);
        }

        private void showWindowAndCreateBufferStrategy(boolean testEnvironmentOnly) {
            if (!testEnvironmentOnly) {
                frame.setVisible(true);
            }
            paintingPanel.createBufferStrategy(2);
            paintingPanel.canvasBufferStrategy = paintingPanel.getBufferStrategy();
            Graphics2D graphics2D = (Graphics2D) paintingPanel.canvasBufferStrategy.getDrawGraphics();
            AffineTransform unScaledTransform = graphics2D.getTransform();
            paintingPanel.windowsScaleFactor = unScaledTransform.getScaleX();
            paintingPanel.updateScaleFactor();
        }

        private void setColorForBlockImage(char character, Color color) {
            colorMap.put(character, color);
        }

        private void registerListeners(Mouse mouse, Keyboard keyboard, Sound sound) {
            frame.registerListeners(mouse, keyboard);
            this.sound = sound;
            this.mouse = mouse;
        }

        private void initColorMap() {
            colorMap = new HashMap<>();
            colorMap.put('R', Color.RED);
            colorMap.put('r', Color.RED.darker());
            colorMap.put('G', Color.GREEN);
            colorMap.put('g', Color.GREEN.darker());
            colorMap.put('B', Color.BLUE);
            colorMap.put('b', Color.BLUE.darker());
            colorMap.put('Y', Color.YELLOW);
            colorMap.put('y', Color.YELLOW.darker());
            colorMap.put('P', Color.PINK);
            colorMap.put('p', Color.PINK.darker());
            colorMap.put('C', Color.CYAN);
            colorMap.put('c', Color.CYAN.darker());
            colorMap.put('M', Color.MAGENTA);
            colorMap.put('m', Color.MAGENTA.darker());
            colorMap.put('O', Color.ORANGE);
            colorMap.put('o', Color.ORANGE.darker());
            colorMap.put('W', Color.WHITE);
            colorMap.put('L', Color.BLACK);
        }

        // Anzeige
        private void setStatusText(String statusText) {
            SwingUtilities.invokeLater(() -> {
                frame.getStatusLabelLinks().setText(statusText);
                int minWidth = frame.getStatusBar().getPreferredSize().width + 50;
                frame.setMinimumSize(new Dimension(minWidth, minWidth / WIDTH * HEIGHT));
            });
        }

        private BufferedImage createImage(int width, int height, double scale) {
            return new BufferedImage((int) Math.ceil(width * scale), (int) Math.ceil(
                    height * scale), BufferedImage.TYPE_INT_ARGB_PRE);
        }

        private Graphics2D createGraphics2D(BufferedImage image, double scale) {
            Graphics2D graphics2D = image.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.scale(scale, scale);
            return graphics2D;
        }

        private BufferedImage createImageFromFile(String imageFileName, double imageScaleFactor) {
            int hash = Objects.hash(imageFileName, (int) Math.round(
                    imageScaleFactor * 100), paintingPanel.scaleFactorHash);
            BufferedImage image = imageMap.get(hash);
            if (image == null) {
                if (imageScaleFactor <= 0) {
                    throw new IllegalArgumentException("scaleFactor has to be a positive number.");
                }
                BufferedImage imageFromDisk;
                URL resourceUrl = Objects.requireNonNull(
                        GameView.class.getResource(Tools.RESOURCE_PREFIX + imageFileName),
                        () -> "ImageFile \"" + imageFileName + "\" konnte nicht gefunden werden!"
                );
                try {
                    imageFromDisk = ImageIO.read(resourceUrl);
                    Objects.requireNonNull(imageFromDisk,
                            () -> "ImageFile \"" + imageFileName
                                  + "\" konnte nicht geladen werden oder ist kein gültiges Bildformat!");
                } catch (IOException e) {
                    throw new UncheckedIOException("Fehler beim Lesen der Bilddatei: " + imageFileName, e);
                }
                double scale = paintingPanel.windowsScaleFactor * paintingPanel.panelScaleFactor * imageScaleFactor;
                int width = imageFromDisk.getWidth();
                int height = imageFromDisk.getHeight();
                image = createImage(width, height, scale);
                scale = image.getWidth() / (1d * width);
                Graphics2D graphics2D = createGraphics2D(image, scale);
                graphics2D.drawImage(imageFromDisk, 0, 0, null);
                graphics2D.dispose();
                addImageToMapOrClearMap(hash, image);
            }
            return image;
        }

        private BufferedImage createImageFromColorString(String colorString, double blockSize) {
            int roundedBlockSize = (int) Math.round(blockSize);
            int hash = Objects.hash(colorString, roundedBlockSize, paintingPanel.scaleFactorHash);
            BufferedImage image = imageMap.get(hash);
            if (image == null) {
                if (blockSize < 0.5) {
                    throw new IllegalArgumentException("blockSize has to be at least 0.5.");
                }
                String[] lines = colorString.split("\\R");
                double scale = paintingPanel.windowsScaleFactor * paintingPanel.panelScaleFactor;
                int width = Arrays.stream(lines).mapToInt(String::length).max().orElse(1) * roundedBlockSize;
                int height = lines.length * roundedBlockSize;
                image = createImage(width, height, scale);
                scale = image.getWidth() / (1d * width);
                double offsetToPreventRoundingErrors = 0.000000001;
                Graphics2D graphics2D = createGraphics2D(image, scale + offsetToPreventRoundingErrors);
                for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
                    char[] blocks = lines[lineIndex].toCharArray();
                    for (int columnIndex = 0; columnIndex < blocks.length; columnIndex++) {
                        Color color = colorMap.get(blocks[columnIndex]);
                        if (color != null) {
                            graphics2D.setColor(color);
                            graphics2D.fillRect(
                                    columnIndex * roundedBlockSize,
                                    lineIndex * roundedBlockSize, roundedBlockSize, roundedBlockSize);
                        }
                    }
                }
                graphics2D.dispose();
                addImageToMapOrClearMap(hash, image);
            }
            return image;
        }

        private BufferedImage createImageFromText(String text, double fontSize, Color color, boolean bold, String fontName) {
            int roundedFontSize = (int) Math.round(fontSize) + 1;
            int hash = Objects.hash(text, roundedFontSize, color, bold, fontName, paintingPanel.scaleFactorHash);
            BufferedImage image = imageMap.get(hash);
            if (image == null) {
                if (fontSize < 5) {
                    throw new IllegalArgumentException("fontSize has to be at least 5.");
                }
                if (text == null || text.isEmpty()) {
                    throw new IllegalArgumentException("Text can't be null or empty.");
                }
                String[] lines = text.split("\\R");
                Font font = storedFonts.get(fontName);
                if (font != null) {
                    activeFont = font;
                } else {
                    if (!blockUntilFontIsLoaded) {
                        new Thread(() -> loadFont(fontName)).start();
                    }
                    return new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB_PRE);
                }
                Font imageFont = this.activeFont.deriveFont((float) roundedFontSize);
                if (bold) {
                    imageFont = imageFont.deriveFont(Font.BOLD);
                }
                FontMetrics fontMetrics = paintingPanel.getFontMetrics(imageFont);
                double scale = paintingPanel.windowsScaleFactor * paintingPanel.panelScaleFactor;
                int width = (int) Math.round(
                        Arrays.stream(lines).mapToInt(fontMetrics::stringWidth).max().orElse(1) * 1.1);
                int height = fontMetrics.getHeight() * lines.length;
                image = createImage(width, height, scale);
                scale = image.getWidth() / (1d * width);
                Graphics2D graphics2D = createGraphics2D(image, scale);
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
                graphics2D.setFont(imageFont);
                graphics2D.setColor(color);
                for (int i = 0; i < lines.length; i++) {
                    graphics2D.drawString(lines[i], 0, roundedFontSize + fontMetrics.getHeight() * i);
                }
                graphics2D.dispose();
                addImageToMapOrClearMap(hash, image);
            }
            return image;
        }

        private void addImageToMapOrClearMap(int hash, BufferedImage image) {
            if (sizeOfImageMapInMB > IMAGE_MAP_LIMIT_IN_MB || paintingPanel.scaleFactorChanged) {
                imageMap.clear();
                sizeOfImageMapInMB = 0;
                if (!paintingPanel.scaleFactorChanged) {
                    imageMapRefreshCounter++;
                }
                paintingPanel.scaleFactorChanged = false;
            }
            imageMap.put(hash, image);
            sizeOfImageMapInMB += image.getHeight() * image.getWidth() * 0.000004;
        }

        // Fenster-Dekorationen
        private void setTitle(String title) {
            frame.setTitle(title);
        }

        private void setWindowIcon(String iconFileName) {
            Image fensterSymbol = new ImageIcon(
                    Objects.requireNonNull(
                            GameView.class.getResource(Tools.RESOURCE_PREFIX + iconFileName),
                            () -> "Symbolfile \"" + iconFileName + "\" konnte nicht gefunden werden!"
                    )
            ).getImage();
            frame.setIconImage(fensterSymbol);
        }

        private void loadFont(String fontName) {
            blockUntilFontIsLoaded = true;
            Font font;
            InputStream is = GameView.class.getResourceAsStream(Tools.RESOURCE_PREFIX + fontName);
            if (is == null) {
                throw new IllegalArgumentException(
                        "File does not exist or wrong filename! Only .ttf-Fonts are supported: " + fontName);
            }
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, is);
            } catch (FontFormatException | IOException e) {
                throw new IllegalArgumentException("Font could not be loaded: " + fontName, e);
            }
            font = font.deriveFont((float) 15);
            activeFont = font;
            storedFonts.put(fontName, font);
            blockUntilFontIsLoaded = false;
        }

        // Maus Cursor
        private void setMouseCursor(String cursorImageFileName, boolean centered) {
            URL resourceUrl = GameView.class.getResource(Tools.RESOURCE_PREFIX + cursorImageFileName);
            Objects.requireNonNull(resourceUrl, () -> "Cursor-Datei konnte nicht gefunden werden: "
                                                      + Tools.RESOURCE_PREFIX + cursorImageFileName);
            Image im = new ImageIcon(resourceUrl).getImage();
            SwingUtilities.invokeLater(() -> paintingPanel.setCursor(createCursor(im, centered)));
        }

        private Cursor createCursor(Image im, boolean centered) {
            Toolkit toolkit = paintingPanel.getToolkit();
            Dimension cursorSize = Toolkit.getDefaultToolkit().getBestCursorSize(64, 64);
            Point cursorHotSpot = new Point(0, 0);
            if (centered) {
                cursorHotSpot = new Point(cursorSize.width / 2, cursorSize.height / 2);
            }
            return toolkit.createCustomCursor(im, cursorHotSpot, "Cross");
        }

        private void setStandardMouseCursor() {
            SwingUtilities.invokeLater(() -> paintingPanel.setCursor(Cursor.getDefaultCursor()));
        }

        private void setInvisibleMouseCursor() {
            Image im = new ImageIcon("").getImage();
            SwingUtilities.invokeLater(() -> paintingPanel.setCursor(createCursor(im, false)));
        }

        // Beenden
        private void closeGameView() {
            sound.stopAllSounds();
            mouse.invisibleMouseTimer.stop();
            frame.dispose();
        }

        private Dimension getTextDisplaySize() {
            return paintingPanel.getSize();
        }

        public void paintImage(ArrayList<PrintObject> printObjects, Color backgroundColor) {
            paintingPanel.paintImage(printObjects, backgroundColor);
        }
    }

    private static class PaintingPanel extends java.awt.Canvas {
        private final AffineTransform identity;
        private final Statistic statistic;
        private BufferStrategy canvasBufferStrategy;
        private double windowsScaleFactor;
        private double panelScaleFactor;
        private int scaleFactorHash;
        private java.awt.Rectangle bounds;
        private java.awt.Rectangle scaledBounds;
        private boolean scaleFactorChanged;
        private AffineTransform scaledTransform;

        private PaintingPanel(Statistic statistic) {
            this.statistic = statistic;
            setIgnoreRepaint(true);
            setSize(GameView.WIDTH, GameView.HEIGHT);
            identity = new AffineTransform();
        }

        private void updateScaleFactor() {
            panelScaleFactor = Math.min(getWidth() * 1d / GameView.WIDTH, getHeight() * 1d / GameView.HEIGHT);
            scaleFactorHash = (int) (Math.round(panelScaleFactor * 1000));
            bounds = new java.awt.Rectangle(0, 0, GameView.WIDTH, GameView.HEIGHT);
            scaledBounds = new java.awt.Rectangle(0, 0, (int) Math.ceil(
                    GameView.WIDTH * windowsScaleFactor * panelScaleFactor), (int) Math.ceil(
                    GameView.HEIGHT * windowsScaleFactor * panelScaleFactor));
            scaledTransform = new AffineTransform();
            scaledTransform.scale(panelScaleFactor * windowsScaleFactor, panelScaleFactor * windowsScaleFactor);
            scaleFactorChanged = true;
        }

        private void paintImage(ArrayList<PrintObject> printObjects, Color backgroundColor) {
            do {
                do {
                    Graphics2D graphics2D = (Graphics2D) canvasBufferStrategy.getDrawGraphics();
                    graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
                    graphics2D.setTransform(scaledTransform);
                    statistic.drawImageTic();
                    draw(graphics2D, printObjects, backgroundColor);
                    statistic.drawImageToc();
                    graphics2D.dispose();
                } while (canvasBufferStrategy.contentsRestored());
                statistic.paintImageTic();
                canvasBufferStrategy.show();
                statistic.paintImageToc();
            } while (canvasBufferStrategy.contentsLost());
        }

        private void draw(Graphics2D graphics2D, ArrayList<PrintObject> printObjects, Color backgroundColor) {
            graphics2D.setColor(backgroundColor);
            graphics2D.fillRect(0, 0, GameView.WIDTH, GameView.HEIGHT);
            for (PrintObject p : printObjects) {
                graphics2D.setColor(p.color);
                switch (p.type) {
                    case OVAL -> {
                        Oval oval = (Oval) p;
                        int x = oval.x - oval.width / 2;
                        int y = oval.y - oval.height / 2;
                        if (oval.filled) {
                            graphics2D.fillOval(x, y, oval.width + oval.lineWeight, oval.height + oval.lineWeight);
                        } else {
                            graphics2D.setStroke(new BasicStroke(oval.lineWeight));
                            graphics2D.drawOval(
                                    x + oval.lineWeight / 2, y + oval.lineWeight / 2, oval.width, oval.height);
                        }
                    }
                    case LINE -> {
                        Line line = (Line) p;
                        graphics2D.setStroke(new BasicStroke(line.lineWeight));
                        graphics2D.drawLine(line.x, line.y, line.xEnd, line.yEnd);
                    }
                    case RECTANGLE -> {
                        Rectangle rectangle = (Rectangle) p;
                        if (rectangle.filled) {
                            graphics2D.fillRect(rectangle.x, rectangle.y,
                                    rectangle.width + rectangle.lineWeight, rectangle.height + rectangle.lineWeight);
                        } else {
                            graphics2D.setStroke(new BasicStroke(rectangle.lineWeight));
                            graphics2D.drawRect(
                                    rectangle.x + rectangle.lineWeight / 2,
                                    rectangle.y + rectangle.lineWeight / 2, rectangle.width, rectangle.height);
                        }
                    }
                    case POLYGON -> {
                        Polygon polygon = (Polygon) p;
                        if (polygon.filled) {
                            graphics2D.fillPolygon(polygon.xCoordinates, polygon.yCoordinates, polygon.xCoordinates.length);
                        } else {
                            graphics2D.setStroke(new BasicStroke(polygon.lineWeight));
                            graphics2D.drawPolygon(polygon.xCoordinates, polygon.yCoordinates, polygon.xCoordinates.length);
                        }
                    }
                    case POLYLINE -> {
                        PolyLine polyLine = (PolyLine) p;
                        graphics2D.setStroke(new BasicStroke(polyLine.lineWeight));
                        graphics2D.drawPolyline(polyLine.xCoordinates, polyLine.yCoordinates, polyLine.xCoordinates.length);
                    }
                    case IMAGE_OBJECT -> {
                        ImageObject imageObject = (ImageObject) p;
                        graphics2D.setTransform(identity);
                        if (imageObject.rotation != 0) {
                            AffineTransform rotationTransform = graphics2D.getTransform();
                            graphics2D.translate(imageObject.x, imageObject.y);
                            rotationTransform.rotate(Math.toRadians(imageObject.rotation),
                                    imageObject.image.getWidth() / 2.0, imageObject.image.getHeight() / 2.0);
                            graphics2D.drawImage(imageObject.image, rotationTransform, null);
                        } else {
                            graphics2D.drawImage(imageObject.image, imageObject.x, imageObject.y, null);
                        }
                        graphics2D.setTransform(scaledTransform);
                    }
                }
            }
        }
    }

    private static class Tools {
        public static final String RESOURCE_PREFIX = "/resources/";
        public static final String NEGATIVE_LINEWEIGHT = "lineWeight can't be negative.";

        private static void sleep(long milliseconds) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class Timer {
        private final long startTimeInMilliseconds;
        private final HashMap<Integer, Long> timers;

        private Timer() {
            startTimeInMilliseconds = System.currentTimeMillis();
            timers = new HashMap<>(200);
        }

        private int gameTimeInMilliseconds() {
            return (int) (System.currentTimeMillis() - startTimeInMilliseconds);
        }

        private boolean timer(int millisecondsFalse, int millisecondsTrue, Object id) {
            int hash = Thread.currentThread().getStackTrace()[3].getLineNumber() + System.identityHashCode(id);
            Long dueTime = timers.get(hash);
            if (dueTime == null) {
                timers.put(hash, System.currentTimeMillis() + millisecondsFalse);
                return false;
            } else {
                long currentTime = System.currentTimeMillis();
                if (currentTime >= dueTime) {
                    if (currentTime >= dueTime + millisecondsTrue) {
                        timers.remove(hash);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }

        private void resetTimers(Object id) {
            int objectHash = System.identityHashCode(id);
            for (int i = 0; i < 2000; i++) {
                timers.remove(objectHash + i);
            }
        }

        private void resetAllTimers() {
            timers.clear();
        }
    }

    private static class Version {
        private static final int MAJOR = 2;
        private static final int MINOR = 5;
        private static final int UPDATE = 0;

        private static final String VERSION = MAJOR + "." + MINOR + "." + UPDATE;

        private static final String STANDARD_TITLE = "GameView";
        private static final String SIGNATURE = "Prof. Dr. Andreas Berl - TH Deggendorf";

        private static String getStatusSignature() {
            return "   " + SIGNATURE + " ";
        }

        private static String getStandardTitle() {
            return STANDARD_TITLE + " " + VERSION;
        }

        private static boolean isSmallerThan(String versionString) {
            String[] split = versionString.split("\\.");
            int major = Integer.parseInt(split[0]);
            int minor = Integer.parseInt(split[1]);
            int update = Integer.parseInt(split[2]);
            if (MAJOR != major) {
                return MAJOR < major;
            } else if (MINOR != minor) {
                return MINOR < minor;
            } else if (UPDATE != update) {
                return UPDATE < update;
            } else {
                return false;
            }
        }
    }

    private static class Element {
        protected static final int FONT_SIZE = 12;
        protected static final int X_POSITION_1 = 15;
        protected static int height = 0;
        protected String name;
        protected Color color;

        private Element(String name, Color color) {
            this.name = name;
            this.color = color;
        }
    }

    private class Terminal {
        public void plotTerminal(String text, String resolution) {
            Resolution res = Resolution.fromString(resolution);
            String[] linesAsArray = text.split("\\R");
            for (int line = 0; line < linesAsArray.length && line < res.lines; line++) {
                for (int column = 0; column < linesAsArray[line].length() && column < res.columns; column++) {
                    char c = linesAsArray[line].charAt(column);
                    if (c != ' ') {
                        addTextToCanvas(String.valueOf(c),
                                column * res.letterWidth + 3,
                                line * res.letterHeight, res.fontSize, true, Color.WHITE, 0);
                    }
                }
            }
            repairFrameTimingInCaseOfExternalThreadSleep();
            plotCanvas();
        }

        private void repairFrameTimingInCaseOfExternalThreadSleep() {
            long timePassedSinceStartOfSecond = System.nanoTime() - gameLoop.startOfSecond;
            long timeThatShouldHavePassed = Math.round(gameLoop.currentFrame * GameLoop.NANOS_PER_FRAME);
            if (timePassedSinceStartOfSecond > timeThatShouldHavePassed) {
                gameLoop.currentFrame = 1;
                gameLoop.startOfSecond = System.nanoTime();
            }
        }

        private enum Resolution {
            RES_80x45(80, 45, 19, 15.7, 15.96), RES_128x64(128, 64, 15, 11, 9.95), RES_160x90(160, 90, 10, 7.9, 7.965), RES_240x135(240, 135, 5, 5.3, 5.315);

            public final int columns;
            public final int lines;
            final int fontSize;
            final double letterHeight;
            final double letterWidth;

            Resolution(int columns, int lines, int fontSize, double letterHeight, double letterWidth) {
                this.columns = columns;
                this.lines = lines;
                this.fontSize = fontSize;
                this.letterHeight = letterHeight;
                this.letterWidth = letterWidth;
            }

            private static Resolution fromString(String fontSize) {
                return switch (fontSize) {
                    case "S" -> RES_80x45;
                    case "M" -> RES_128x64;
                    case "L" -> RES_160x90;
                    case "XL" -> RES_240x135;
                    default ->
                            throw new IllegalArgumentException("Die Schriftgröße muss 'XL', 'L', 'M' oder 'S' sein.");
                };
            }
        }
    }

    private class Keyboard {
        private static final int BUFFER_SIZE = 20;
        private final ArrayBlockingQueue<Integer> currentlyPressedKeys;
        private final ArrayBlockingQueue<KeyEvent> typedKeys;
        private final ScheduledExecutorService executor;

        private Keyboard() {
            currentlyPressedKeys = new ArrayBlockingQueue<>(BUFFER_SIZE, true);
            typedKeys = new ArrayBlockingQueue<>(BUFFER_SIZE, true);
            executor = Executors.newSingleThreadScheduledExecutor();
        }

        private void update(KeyEvent keyEvent) {
            if (keyEvent.getID() == KeyEvent.KEY_PRESSED) {
                if (!currentlyPressedKeys.contains(keyEvent.getKeyCode())) {
                    currentlyPressedKeys.offer(keyEvent.getKeyCode());
                }
            } else if (keyEvent.getID() == KeyEvent.KEY_RELEASED) {
                currentlyPressedKeys.remove(keyEvent.getKeyCode());
                if (typedKeys.size() == BUFFER_SIZE) {
                    typedKeys.poll();
                }
                typedKeys.offer(keyEvent);
                if (keyEvent.isControlDown() && keyEvent.getKeyCode() == KeyEvent.VK_R) {
                    statistic.showStatistics = !statistic.showStatistics;
                }
            }
        }

        private void clearKeyEvents() {
            currentlyPressedKeys.clear();
            typedKeys.clear();
        }

        private KeyEvent[] typedKeys() {
            synchronized (typedKeys) {
                KeyEvent[] keys = typedKeys.toArray(new KeyEvent[0]);
                typedKeys.clear();
                return keys;
            }
        }

        private Integer[] keyCodesOfCurrentlyPressedKeys() {
            synchronized (currentlyPressedKeys) {
                return currentlyPressedKeys.toArray(new Integer[0]);
            }
        }

        public void clickKey(int keyCode) {
            pressKey(keyCode);
            executor.schedule(() -> releaseKey(keyCode), 50, TimeUnit.MILLISECONDS);
        }

        public void pressKey(int keyCode) {
            KeyEvent keyEvent = new KeyEvent(swingAdapter.frame, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
            update(keyEvent);
        }

        public void releaseKey(int keyCode) {
            KeyEvent keyEvent = new KeyEvent(swingAdapter.frame, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
            update(keyEvent);
        }
    }

    private class Statistic {

        private final StatisticBox statisticBox;
        private long gameLogicStartTime;
        private long gameLogicAverageDuration;
        private long drawImageStartTime;
        private long drawImageAverageDuration;
        private long paintImageStartTime;
        private long paintImageAverageDuration;
        private long lastStatisticsUpdateTime;
        private boolean showStatistics;
        private int cyclesCounter;
        private int framesCounter;
        private int invisiblePrintObjects;

        private Statistic() {
            statisticBox = new StatisticBox();
        }

        private void updateStatistic() {
            long currentTime = System.currentTimeMillis();
            long timePassed = currentTime - lastStatisticsUpdateTime;
            if (timePassed > 1_000) {
                if (firstCall()) {
                    lastStatisticsUpdateTime = System.currentTimeMillis();
                    return;
                }
                // FPS
                statisticBox.loopsPerSecondValue = cyclesCounter;
                statisticBox.framesPerSecondValue = framesCounter;
                cyclesCounter = 0;
                framesCounter = 0;

                // Average Times
                statisticBox.gameViewValue = (int) Math.max(1, drawImageAverageDuration);
                statisticBox.graphicValue = (int) Math.max(1, paintImageAverageDuration);
                statisticBox.gameValue = (int) Math.max(1, gameLogicAverageDuration);

                // PrintObjects
                int numberOfStatisticObjects = 35; // SimpleStartScreen has 8 PrintObjects
                statisticBox.visibleValue = Math.max(0, canvas.printObjects.size() - numberOfStatisticObjects);
                statisticBox.invisibleValue = invisiblePrintObjects;

                // Image buffer
                statisticBox.bufferSizeValue = (int) swingAdapter.sizeOfImageMapInMB;
                statisticBox.bufferOverflowValue = swingAdapter.imageMapRefreshCounter;
                lastStatisticsUpdateTime = currentTime;
            }
            if (showStatistics) {
                statisticBox.paintStatisticBox();
            }
        }

        private boolean firstCall() {
            return lastStatisticsUpdateTime == 0;
        }

        private void gameLogicTic() {
            gameLogicStartTime = System.currentTimeMillis();
        }

        private void drawImageTic() {
            drawImageStartTime = System.currentTimeMillis();
        }

        private void paintImageTic() {
            paintImageStartTime = System.currentTimeMillis();
        }

        private void gameLogicToc() {
            gameLogicAverageDuration = toc(gameLogicAverageDuration, gameLogicStartTime);
        }

        private void drawImageToc() {
            drawImageAverageDuration = toc(drawImageAverageDuration, drawImageStartTime);
        }

        private void paintImageToc() {
            paintImageAverageDuration = toc(paintImageAverageDuration, paintImageStartTime);
        }

        private long toc(long currentAverage, long startTime) {
            return Math.min(100, (4 * currentAverage + System.currentTimeMillis() - startTime) / 5);
        }
    }

    private class StatisticBox {
        private int boxYPosition;
        private int loopsPerSecondValue;
        private int framesPerSecondValue;
        private int gameViewValue;
        private int graphicValue;
        private int gameValue;
        private int visibleValue;
        private int invisibleValue;
        private int bufferSizeValue;
        private int bufferOverflowValue;

        private StatisticBox() {
            loopsPerSecondValue = GameLoop.FRAMES_PER_SECOND;
            framesPerSecondValue = GameLoop.FRAMES_PER_SECOND;
            gameViewValue = 1;
            graphicValue = 1;
            gameValue = 1;
        }

        private void paintStatisticBox() {
            boxYPosition = 5;
            addBox(new Title("Bildraten"),
                    new Line("Loops/Sekunde:", loopsPerSecondValue, null, false, 54, 50),
                    new Line("Bilder/Sekunde:", framesPerSecondValue, null, false, 50, 28));
            addBox(new Title("16 ms pro Bild"),
                    new Line("GameView:", gameViewValue, "ms", true, 10, 20),
                    new Line("Fenster:", graphicValue, "ms", true, 15, 20),
                    new Line("Spiel-Logik:", gameValue, "ms", true, 2, 3));
            addBox(new Title("Spiel-Objekte"),
                    new Line("Sichtbar:", visibleValue, null, true, 200, 300),
                    new Line("Unsichtbar:", invisibleValue, null, true, 100, 200));
            addBox(new Title("Bildpuffer"),
                    new Line("Größe:", bufferSizeValue, "MB", true, 500, 700),
                    new Line("Überläufe:", bufferOverflowValue, null, true, 1, 2));
        }

        private void addBox(Title title, Line... lines) {
            int boxXPosition = 5;
            int gap = 5;
            int width = 175;
            int height = 3 * gap + Title.height + lines.length * Line.height;
            addRectangleToCanvas(boxXPosition + 1, boxYPosition + 1, width, height, 0, true, Color.BLACK);
            addRectangleToCanvas(boxXPosition, boxYPosition, width, height, 2, false, Color.WHITE);
            boxYPosition += gap;
            title.add(boxYPosition);
            boxYPosition += Title.height + gap;
            for (Line line : lines) {
                line.add(boxYPosition);
                boxYPosition += Line.height;
            }
            boxYPosition += gap;
        }

        private class Title extends Element {

            private Title(String description) {
                super(description, Color.WHITE);
                height = (int) (FONT_SIZE * 1.2);
            }

            private void add(int yPosition) {
                addTextToCanvas(centeredTitle(), X_POSITION_1, yPosition, FONT_SIZE, true, color, 0);
            }

            private String centeredTitle() {
                int gap = (21 - name.length()) / 2;
                return " ".repeat(gap) + name;
            }
        }

        private class Line extends Element {
            private static final int X_POSITION_2 = 130;
            private static final int X_POSITION_3 = 160;
            private final int value;
            private final String measure;

            private Line(String description, int value, String measure, boolean lowIsGood, int dangerous, int critical) {
                super(description, lowIsGood ? chooseColorLowIsGood(value, dangerous, critical) : chooseColorHighIsGood(value, dangerous, critical));
                this.value = value;
                this.measure = measure;
                height = (int) (FONT_SIZE * 1.2);
            }

            private static Color chooseColorHighIsGood(int value, int dangerous, int critical) {
                if (value <= critical) {
                    return Color.RED;
                } else if (value <= dangerous) {
                    return Color.ORANGE;
                } else {
                    return Color.WHITE;
                }
            }

            private static Color chooseColorLowIsGood(int value, int dangerous, int critical) {
                if (value >= critical) {
                    return Color.RED;
                } else if (value >= dangerous) {
                    return Color.ORANGE;
                } else {
                    return Color.WHITE;
                }
            }

            private void add(int yPosition) {
                addTextToCanvas(name, X_POSITION_1, yPosition, FONT_SIZE, false, color, 0);
                addTextToCanvas(numberWithHundredGap(value), X_POSITION_2, yPosition, FONT_SIZE, false, color, 0);
                if (measure != null) {
                    addTextToCanvas(measure, X_POSITION_3, yPosition, FONT_SIZE, false, color, 0);
                }
            }

            private String numberWithHundredGap(int number) {
                String gap = "";
                if (number < 10) {
                    gap += " ";
                }
                if (number < 100) {
                    gap += " ";
                }
                return gap + number;
            }
        }
    }

    private class GameLoop {
        private static final int FRAMES_PER_SECOND = 60;
        private static final double NANOS_PER_FRAME = 1_000_000_000d / FRAMES_PER_SECOND;
        private long startOfSecond;
        private int currentFrame;

        private GameLoop() {
            statistic.gameLogicTic();
            currentFrame = FRAMES_PER_SECOND; // Wird in der ersten Schleife auf 0 gesetzt
        }

        private void plotCanvas() {
            statistic.gameLogicToc();
            statistic.cyclesCounter++;
            statistic.updateStatistic();
            swingAdapter.paintImage(canvas.printObjects, canvas.backgroundColor);
            statistic.framesCounter++;
            canvas.printObjects.clear();
            statistic.invisiblePrintObjects = 0;
            sleepUntilEndOfFrame();
            statistic.gameLogicTic();
        }

        private void sleepUntilEndOfFrame() {
            long timePassedSinceStartOfSecond = System.nanoTime() - startOfSecond;
            long timeThatShouldHavePassed = Math.round(currentFrame * NANOS_PER_FRAME);
            long sleepTime = timeThatShouldHavePassed - timePassedSinceStartOfSecond;
            LockSupport.parkNanos(sleepTime);
            if (currentFrame == FRAMES_PER_SECOND) {
                currentFrame = 1;
                startOfSecond = System.nanoTime();
            } else {
                currentFrame++;
            }
        }
    }

    /**
     * Diese innere Klasse liefert eine Testumgebung für automatisierte Tests.
     */
    public class TestEnvironment {

        private TestEnvironment() {
        }

        /**
         * Diese Methode kann die Versionsnummer von GameView verifizieren.
         *
         * @param versionString Die mindestens erwartete Versionsnummer.
         * @return True, falls GameView mindestens die erwartete Versionsnummer hat.
         */
        public static boolean versionNumberIsAtLeast(String versionString) {
            return !Version.isSmallerThan(versionString);
        }

        /**
         * Diese Methode kann einen Tastendruck auf der Tastatur simulieren. Das kann für automatisierte Tests eingesetzt
         * werden. Es wird ein vollständiger Tastendruck simuliert, d.h. es wird ein KeyPressed- und ein KeyReleased-Event erzeugt.
         *
         * @param keyCode Ein KeyCode von <code>java.awt.event.KeyEvent</code>, z.B. <code>KeyEvent.VK_A</code> für den
         *                Buchstaben A.
         */
        public void clickKey(int keyCode) {
            keyboard.clickKey(keyCode);
        }

        /**
         * Diese Methode kann einen Tastendruck auf der Tastatur simulieren. Das kann für automatisierte Tests eingesetzt
         * werden. Es wird nur ein KeyPressed-Event erzeugt.
         *
         * @param keyCode Ein KeyCode von <code>java.awt.event.KeyEvent</code>, z.B. <code>KeyEvent.VK_A</code> für den
         *                Buchstaben A.
         */
        public void pressKey(int keyCode) {
            keyboard.pressKey(keyCode);
        }

        /**
         * Diese Methode kann einen Tastendruck auf der Tastatur simulieren. Das kann für automatisierte Tests eingesetzt
         * werden. Es wird nur ein KeyReleased-Event erzeugt.
         *
         * @param keyCode Ein KeyCode von <code>java.awt.event.KeyEvent</code>, z.B. <code>KeyEvent.VK_A</code> für den
         *                Buchstaben A.
         */
        public void releaseKey(int keyCode) {
            keyboard.releaseKey(keyCode);
        }

        /**
         * Schließt das GameView-Fenster.
         */
        public void closeGameView() {
            swingAdapter.closeGameView();
            instances--;
        }
    }
}