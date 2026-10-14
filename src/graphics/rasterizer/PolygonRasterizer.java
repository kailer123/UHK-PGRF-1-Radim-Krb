package graphics.rasterizer;

import model.Polygon;

/**
 * Draws a polygon into a raster.
 *
 * @author Radim Krb
 */
public interface PolygonRasterizer {

    void rasterize(Polygon polygon);

}
