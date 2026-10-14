package controller;

import graphics.rasterizer.LineRasterizer;
import graphics.rasterizer.PolygonOutlineRasterizer;
import graphics.rasterizer.PolygonRasterizer;
import graphics.rasterizer.TrivialLineRasterizer;
import model.Line;
import model.Point;
import model.Polygon;
import view.Canvas;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private Point startPoint;
    private Point currentPoint;

    private final Polygon polygon;
    private final Canvas canvas;
    private final LineRasterizer rasterizer;
    private final PolygonRasterizer polygonRasterizer;

    private final List<Line> lines = new ArrayList<Line>();
    private static final int LINE_COLOR = Color.WHITE.getRGB();
    private static final int PREVIEW_COLOR = Color.RED.getRGB();

    public Controller(Canvas canvas) {
        this.canvas = canvas;
        this.polygon = new Polygon();
        this.rasterizer = new TrivialLineRasterizer(canvas.getRaster());
        this.polygonRasterizer = new PolygonOutlineRasterizer(rasterizer);

        lines.add(new Line(new Point(100, 100), new Point(300, 100), Color.WHITE.getRGB()));
        lines.add(new Line(new Point(300, 100), new Point(350, 250), Color.WHITE.getRGB()));
        lines.add(new Line(new Point(350, 250), new Point(200, 350), Color.WHITE.getRGB()));
        lines.add(new Line(new Point(200, 350), new Point(100, 250), Color.WHITE.getRGB()));
    }

    public void init() {
        canvas.clear();
        polygonRasterizer.rasterize(new Polygon(lines));
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = getPoint(e);
                currentPoint = startPoint;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lines.add(new Line(startPoint, getPoint(e), LINE_COLOR));
                currentPoint = null;
                startPoint = null;
                render();
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (startPoint == null) {
                    return;
                }
                currentPoint = getPoint(e);
                render();
            }
        });

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
        canvas.repaint();
    }

    private void render() {
        canvas.clear();
        polygonRasterizer.rasterize(polygon);
        canvas.repaint();
    }

    private void starLine(MouseEvent e) {
    }

    private void finishPolygon(MouseEvent e) {
        render();
    }

    private Point getPoint(MouseEvent e) {
        return new Point(e.getX(), e.getY());
    }

}