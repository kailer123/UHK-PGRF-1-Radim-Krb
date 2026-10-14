package graphics.rasterizer;

import model.Line;

/**
 * Draws a line into a raster. Implementations differ in the rasterization algorithm.
 *
 * @author Radim Krb
 */
public interface LineRasterizer {

    void rasterize(Line line);

}
