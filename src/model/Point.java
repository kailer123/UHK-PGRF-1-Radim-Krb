package model;

/**
 * Point in raster coordinates. Immutable.
 *
 * @author Radim Krb
 */
public class Point {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /** Squared distance to another point. The square root is not needed when comparing distances. */
    public int distanceSquared(Point other) {
        int dx = x - other.x;
        int dy = y - other.y;
        return dx * dx + dy * dy;
    }

}
