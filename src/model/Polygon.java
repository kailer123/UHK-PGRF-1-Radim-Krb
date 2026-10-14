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

    public List<Point> getPoints() {
        return points;
    }

    public int getColor() {
        return color;
    }

    public List<Line> getLines() {
        List<Line> lines = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            lines.add(new Line(points.get(i), points.get(i + 1), color));
        }
        return lines;
    }

}