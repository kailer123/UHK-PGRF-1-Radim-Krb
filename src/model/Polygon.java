package model;

import java.util.List;

public class Polygon {

    private final List<Line> lines;

    public Polygon() {
        this(List.of());
    }

    public Polygon(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

}
