package controller;

import graphics.rasterizer.LineRasterizer;
import graphics.rasterizer.PolygonOutlineRasterizer;
import graphics.rasterizer.PolygonRasterizer;
import graphics.rasterizer.TrivialLineRasterizer;
import model.Line;
import model.Point;
import model.Polygon;
import view.Canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private enum Mode { LINE, POLYGON }

    private Mode mode = Mode.LINE;
    private Point lineStart;
    private Point currentPoint;
    private Frame frame;
    private String baseTitle;

    private final List<Line> lines = new ArrayList<>();
    private final Polygon polygon;
    private final Canvas canvas;
    private final LineRasterizer rasterizer;
    private final PolygonRasterizer polygonRasterizer;

    private static final int LINE_COLOR = Color.WHITE.getRGB();
    private static final int PREVIEW_COLOR = Color.RED.getRGB();

    public Controller(Canvas canvas) {
        this.canvas = canvas;
        this.polygon = new Polygon(LINE_COLOR);
        this.rasterizer = new TrivialLineRasterizer(canvas.getRaster());
        this.polygonRasterizer = new PolygonOutlineRasterizer(rasterizer);
    }

    public void init() {
        frame = (Frame) SwingUtilities.getWindowAncestor(canvas);
        baseTitle = frame.getTitle();
        updateTitle();

        canvas.clear();
        polygonRasterizer.rasterize(polygon);
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                canvas.requestFocusInWindow();
                if (!SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                currentPoint = getPoint(e);
                if (mode == Mode.LINE) {
                    lineStart = currentPoint;
                }
                render();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e) || currentPoint == null) {
                    return;
                }
                if (mode == Mode.LINE) {
                    lines.add(new Line(lineStart, getPoint(e), LINE_COLOR));
                } else {
                    polygon.addPoint(getPoint(e));
                }
                lineStart = null;
                currentPoint = null;
                render();
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentPoint == null) {
                    return;
                }
                currentPoint = getPoint(e);
                render();
            }
        });

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_C -> {
                        lines.clear();
                        polygon.clear();
                        lineStart = null;
                        currentPoint = null;
                        render();
                    }
                    case KeyEvent.VK_L -> setMode(Mode.LINE);
                    case KeyEvent.VK_P -> setMode(Mode.POLYGON);
                }
            }
        });
        canvas.repaint();
    }

    private void setMode(Mode newMode) {
        mode = newMode;
        lineStart = null;
        currentPoint = null;
        updateTitle();
        render();
    }

    private void updateTitle() {
        String modeName = mode == Mode.LINE ? "Line" : "Polygon";
        frame.setTitle(baseTitle + " | Mode: " + modeName + " (L = line, P = polygon, C = clear)");
    }

    private void render() {
        canvas.clear();
        for (Line line : lines) {
            rasterizer.rasterize(line);
        }
        polygonRasterizer.rasterize(polygon);
        if (currentPoint != null) {
            renderPreview();
        }
        canvas.repaint();
    }

    private void renderPreview() {
        if (mode == Mode.LINE) {
            rasterizer.rasterize(new Line(lineStart, currentPoint, PREVIEW_COLOR));
            return;
        }

        List<Point> points = polygon.getPoints();

        if (points.isEmpty()) {
            rasterizer.rasterize(new Line(currentPoint, currentPoint, PREVIEW_COLOR));
            return;
        }

        rasterizer.rasterize(new Line(points.getLast(), currentPoint, PREVIEW_COLOR));
        if (points.size() >= 2) {
            rasterizer.rasterize(new Line(currentPoint, points.getFirst(), PREVIEW_COLOR));
        }
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