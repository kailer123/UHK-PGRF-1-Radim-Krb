package view;

import graphics.Raster;
import graphics.RasterImage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Canvas extends JPanel {

    private final RasterImage image;
    private static final Color BACKGROUND_COLOR = new Color(0x2F2F2F);

    public Canvas(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        image = new RasterImage(width, height);
        clear();
    }

    public void clear() {
        image.clear(BACKGROUND_COLOR.getRGB());
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        image.present(graphics);
    }

    public Raster getRaster() {
        return image;
    }

}
