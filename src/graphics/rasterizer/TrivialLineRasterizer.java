package graphics.rasterizer;

import graphics.Raster;
import model.Line;

/**
 * Line rasterization using the trivial algorithm, based on the line equation y = k * x + q.
 *     Gentle lines (|dy| <= |dx|) step along x and compute y = k * x + q.
 *     Steep lines (|dy| > |dx|) step along y and compute x = k * y + q, so there are no gaps.
 *     Endpoints are swapped when needed, so lines can be drawn in all directions.
 *     Vertical lines are handled as steep lines, a degenerate line (single point) as one pixel.
 *     The loop is limited to the visible part of the raster.
 */
public class TrivialLineRasterizer implements LineRasterizer {

    private final Raster raster;

    public TrivialLineRasterizer(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        rasterize(
                line.getPoint1().getX(),
                line.getPoint1().getY(),
                line.getPoint2().getX(),
                line.getPoint2().getY(),
                line.getColor()
        );
    }

    private void rasterize(int x1, int y1, int x2, int y2, int color) {
        if (x1 == x2 && y1 == y2) {
            raster.setPixel(x1, y1, color);
            return;
        }

        if (Math.abs(y2 - y1) <= Math.abs(x2 - x1)) {
            if (x2 < x1) {
                int temp = x1; x1 = x2; x2 = temp;
                temp = y1; y1 = y2; y2 = temp;
            }

            float k = (y2 - y1) / (float) (x2 - x1);
            float q = y1 - k * x1;

            int startX = Math.max(x1, 0);
            int endX = Math.min(x2, raster.getWidth() - 1);

            for (int x = startX; x <= endX; x++) {
                float y = k * x + q;
                raster.setPixel(x, Math.round(y), color);
            }
        } else {
            if (y2 < y1) {
                int temp = x1; x1 = x2; x2 = temp;
                temp = y1; y1 = y2; y2 = temp;
            }

            float k = (x2 - x1) / (float) (y2 - y1);
            float q = x1 - k * y1;

            int startY = Math.max(y1, 0);
            int endY = Math.min(y2, raster.getHeight() - 1);

            for (int y = startY; y <= endY; y++) {
                float x = k * y + q;
                raster.setPixel(Math.round(x), y, color);
            }
        }
    }

}
