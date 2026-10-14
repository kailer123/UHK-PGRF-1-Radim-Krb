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

    public void insertPoint(int index, Point point) {
        points.add(index, point);
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

    public int findNearestEdge(Point point) {
        int n = points.size();
        int nearest = -1;
        double bestDistance = Double.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            double distance = distanceToSegmentSquared(point, points.get(i), points.get((i + 1) % n));
            if (distance < bestDistance) {
                bestDistance = distance;
                nearest = i;
            }
        }
        return nearest;
    }

    private static double distanceToSegmentSquared(Point p, Point a, Point b) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double lengthSquared = dx * dx + dy * dy;

        if (lengthSquared == 0) {
            return p.distanceSquared(a);
        }

        double t = ((p.getX() - a.getX()) * dx + (p.getY() - a.getY()) * dy) / lengthSquared;
        t = Math.max(0, Math.min(1, t));

        double closestX = a.getX() + t * dx;
        double closestY = a.getY() + t * dy;
        double ex = p.getX() - closestX;
        double ey = p.getY() - closestY;
        return ex * ex + ey * ey;
    }

}