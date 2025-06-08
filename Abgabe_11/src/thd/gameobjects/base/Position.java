package thd.gameobjects.base;

import java.util.Objects;

/**
 * Position with x and y coordinates for game objects.
 * The window's resolution is managed by {@link GameView}.
 *
 * @see GameView
 */
public class Position implements Comparable<Position> {

    private double x;
    private double y;

    /**
     * Creates a position on (0, 0).
     */
    public Position() {
        this(0, 0);
    }

    /**
     * Creates a position with the coordinates of the given position.
     *
     * @param other Another position.
     */
    public Position(Position other) {
        this(other.x, other.y);
    }

    /**
     * Creates a position on (x, y).
     *
     * @param x X-coordinate on the window.
     * @param y Y-coordinate on the window.
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets x coordinate.
     *
     * @return x coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets y coordinate.
     *
     * @return y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Updates this position to the coordinates of the given position.
     *
     * @param other Another position.
     */
    public void updateCoordinates(Position other) {
        x = other.x;
        y = other.y;
    }

    /**
     * Updates this position to the coordinates of the new position.
     *
     * @param x X-coordinate on the window.
     * @param y Y-coordinate on the window.
     */
    public void updateCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * One pixel to the right.
     */
    public void right() {
        x++;
    }

    /**
     * To the right by the given number of pixels.
     *
     * @param pixel Number of pixels.
     */
    public void right(double pixel) {
        x += pixel;
    }

    /**
     * One pixel to the left.
     */
    public void left() {
        x--;
    }

    /**
     * To the left by the given number of pixels.
     *
     * @param pixel Number of pixels.
     */
    public void left(double pixel) {
        x -= pixel;
    }

    /**
     * One pixel upwards.
     */
    public void up() {
        y--;
    }

    /**
     * Upwards by the given number of pixels.
     *
     * @param pixel Number of pixels.
     */
    public void up(double pixel) {
        y -= pixel;
    }

    /**
     * One pixel downwards.
     */
    public void down() {
        y++;
    }

    /**
     * Downwards by the given number of pixels.
     *
     * @param pixel Number of pixels.
     */
    public void down(double pixel) {
        y += pixel;
    }

    /**
     * Calculates the distance between this position and another position.
     *
     * @param other The other position to calculate the distance.
     * @return The distance between the two positions.
     */
    public double distance(Position other) {
        double differenceX = other.getX() - x;
        double differenceY = other.getY() - y;
        return Math.sqrt((Math.pow(differenceX, 2) + Math.pow(differenceY, 2)));
    }

    /**
     * Moves towards the given position with the given speed.
     *
     * @param other        Another position.
     * @param speedInPixel Speed of movement in a single frame.
     */
    public void moveToPosition(Position other, double speedInPixel) {
        double distance = distance(other);
        if (distance <= speedInPixel) {
            updateCoordinates(other);
        } else {
            right((other.x - x) / distance * speedInPixel);
            down((other.y - y) / distance * speedInPixel);
        }
    }

    /**
     * Checks if this position is similar to the other position.
     *
     * @param other Another position.
     * @return True if this position has the same x- and y-coordinates as the other position,
     * when both are rounded to <code>int</code>.
     */
    public boolean similarTo(Position other) {
        return Math.round(x) == Math.round(other.x)
                && Math.round(y) == Math.round(other.y);
    }

    @Override
    public String toString() {
        return "Position (" + (int) Math.round(x) + ", " + (int) Math.round(y) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position other = (Position) o;
        return Double.compare(x, other.x) == 0
                && Double.compare(y, other.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(Position o) {
        return Double.compare(distance(new Position(0, 0)), o.distance(new Position(0, 0)));
    }
}