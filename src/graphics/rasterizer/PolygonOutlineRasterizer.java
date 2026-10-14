package graphics.rasterizer;

import model.Line;
import model.Point;
import model.Polygon;

import java.util.List;

/**
 * Draws the outline of a polygon.
 * Edges connect consecutive vertices, the closing edge connects the last vertex with the first one.
 * Each edge is drawn using the given LineRasterizer.
 *
 * @author Radim Krb
 */
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

        // a single vertex is drawn as a point
        if (n == 1) {
            lineRasterizer.rasterize(new Line(points.get(0), points.get(0), color));
            return;
        }

        // two vertices form one line, the closing edge would be the same line again
        if (n == 2) {
            lineRasterizer.rasterize(new Line(points.get(0), points.get(1), color));
            return;
        }

        // edge i ends in vertex (i + 1) % n, so the last edge closes the polygon
        for (int i = 0; i < n; i++) {
            Point a = points.get(i);
            Point b = points.get((i + 1) % n);
            lineRasterizer.rasterize(new Line(a, b, color));
        }
    }

}