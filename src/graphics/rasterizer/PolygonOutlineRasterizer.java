package graphics.rasterizer;

import model.Line;
import model.Point;
import model.Polygon;

import java.util.List;

public class PolygonOutlineRasterizer implements PolygonRasterizer {

    private final LineRasterizer lineRasterizer;

    public PolygonOutlineRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    @Override
    public void rasterize(Polygon polygon) {
        List<Point> points = polygon.getPoints();
        int n = points.size();
        int color = polygon.getColor();

        if (n == 0) {
            return;
        }

        if (n == 1) {
            lineRasterizer.rasterize(new Line(points.get(0), points.get(0), color));
            return;
        }

        if (n == 2) {
            lineRasterizer.rasterize(new Line(points.get(0), points.get(1), color));
            return;
        }

        for (int i = 0; i < n; i++) {
            Point a = points.get(i);
            Point b = points.get((i + 1) % n);
            lineRasterizer.rasterize(new Line(a, b, color));
        }
    }

}