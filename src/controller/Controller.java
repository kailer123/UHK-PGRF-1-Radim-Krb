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
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private enum Mode { LINE, POLYGON }

    private static final int LINE_COLOR = Color.WHITE.getRGB();
    private static final int PREVIEW_COLOR = Color.RED.getRGB();

    private Mode mode = Mode.LINE;
    private Point lineStart;
    private Point currentPoint;
    private Frame frame;
    private String baseTitle;

    private int editedPolygonIndex = -1;
    private int editedLineIndex = -1;
    private boolean editedLineStart;

    private final List<Line> lines = new ArrayList<>();
    private final Polygon polygon;
    private final Canvas canvas;
    private final LineRasterizer rasterizer;
    private final PolygonRasterizer polygonRasterizer;

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

        initMouse();
        initKeyboard();
        render();
    }

    private void initMouse() {
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                canvas.requestFocusInWindow();
                if (SwingUtilities.isRightMouseButton(e) && e.isShiftDown()) {
                    deleteNearestVertex(getPoint(e));
                    return;
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    selectNearestVertex(getPoint(e));
                    return;
                }
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
                if (SwingUtilities.isRightMouseButton(e)) {
                    stopEdit();
                    return;
                }
                if (!SwingUtilities.isLeftMouseButton(e) || currentPoint == null) {
                    return;
                }
                if (mode == Mode.LINE) {
                    lines.add(new Line(lineStart, getPoint(e), LINE_COLOR));
                } else {
                    addPolygonPoint(getPoint(e));
                }
                lineStart = null;
                currentPoint = null;
                render();
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isEditing()) {
                    moveEditedVertex(getPoint(e));
                    render();
                    return;
                }
                if (currentPoint == null) {
                    return;
                }
                currentPoint = getPoint(e);
                render();
            }
        });
    }

    private void initKeyboard() {
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_C -> clear();
                    case KeyEvent.VK_L -> setMode(Mode.LINE);
                    case KeyEvent.VK_P -> setMode(Mode.POLYGON);
                }
            }
        });
    }

    private void selectNearestVertex(Point mouse) {
        stopEdit();
        int bestDistance = Integer.MAX_VALUE;

        List<Point> points = polygon.getPoints();
        for (int i = 0; i < points.size(); i++) {
            int distance = points.get(i).distanceSquared(mouse);
            if (distance < bestDistance) {
                bestDistance = distance;
                editedPolygonIndex = i;
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            int distanceStart = line.getPoint1().distanceSquared(mouse);
            int distanceEnd = line.getPoint2().distanceSquared(mouse);
            if (distanceStart < bestDistance || distanceEnd < bestDistance) {
                bestDistance = Math.min(distanceStart, distanceEnd);
                editedPolygonIndex = -1;
                editedLineIndex = i;
                editedLineStart = distanceStart <= distanceEnd;
            }
        }
    }

    private void deleteNearestVertex(Point mouse) {
        selectNearestVertex(mouse);
        if (editedPolygonIndex >= 0) {
            polygon.removePoint(editedPolygonIndex);
        } else if (editedLineIndex >= 0) {
            lines.remove(editedLineIndex);
        }
        stopEdit();
        render();
    }

    private void moveEditedVertex(Point mouse) {
        if (editedPolygonIndex >= 0) {
            polygon.setPoint(editedPolygonIndex, mouse);
        } else if (editedLineIndex >= 0) {
            Line line = lines.get(editedLineIndex);
            Line moved = editedLineStart
                    ? new Line(mouse, line.getPoint2(), line.getColor())
                    : new Line(line.getPoint1(), mouse, line.getColor());
            lines.set(editedLineIndex, moved);
        }
    }

    private boolean isEditing() {
        return editedPolygonIndex >= 0 || editedLineIndex >= 0;
    }

    private void stopEdit() {
        editedPolygonIndex = -1;
        editedLineIndex = -1;
    }

    private void clear() {
        stopEdit();
        lines.clear();
        polygon.clear();
        lineStart = null;
        currentPoint = null;
        render();
    }

    private void setMode(Mode newMode) {
        stopEdit();
        mode = newMode;
        lineStart = null;
        currentPoint = null;
        updateTitle();
        render();
    }

    private void updateTitle() {
        String modeName = mode == Mode.LINE ? "Line" : "Polygon";
        frame.setTitle(baseTitle + " | Mode: " + modeName + " (L = line, P = polygon, C = clear, right drag = move vertex, shift + right click = delete vertex)");
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

        if (points.size() == 1) {
            rasterizer.rasterize(new Line(points.getFirst(), currentPoint, PREVIEW_COLOR));
            return;
        }

        int edge = polygon.findNearestEdge(currentPoint);
        Point a = points.get(edge);
        Point b = points.get((edge + 1) % points.size());
        rasterizer.rasterize(new Line(a, currentPoint, PREVIEW_COLOR));
        rasterizer.rasterize(new Line(currentPoint, b, PREVIEW_COLOR));
    }

    private void addPolygonPoint(Point point) {
        if (polygon.getPoints().size() < 2) {
            polygon.addPoint(point);
            return;
        }
        int edge = polygon.findNearestEdge(point);
        polygon.insertPoint(edge + 1, point);
    }

    private Point getPoint(MouseEvent e) {
        return new Point(e.getX(), e.getY());
    }

}