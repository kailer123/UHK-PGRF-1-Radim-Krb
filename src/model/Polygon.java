package model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    private final List<Point> points = new ArrayList<>();
    private final int color;

    public Polygon(int color) {
        this.color = color;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void setPoint(int index, Point point) {
        points.set(index, point);
    }

    public void removePoint(int index) {
        points.remove(index);
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getColor() {
        return color;
    }

    public void clear() {
        points.clear();
    }

}