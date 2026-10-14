package graphics;

import java.awt.*;

/**
 * Raster drawing surface – stores pixels and can display them.
 *
 * @author Radim Krb
 */
public interface Raster {

    int getWidth();
    int getHeight();

    void clear(int color);
    void present(Graphics graphics);
    /** Sets the color of a pixel. Pixels outside the raster are ignored. */
    void setPixel(int x, int y, int color);

}
