package com.storedobject.svg;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an SVG polyline.
 *
 * @author Syam
 */
public class Polyline extends Element {

    /**
     * Points of the polyline.
     */
    protected final List<Point> points = new ArrayList<>();

    /**
     * Constructor.
     */
    public Polyline() {
        this((String) null);
    }

    /**
     * Constructor.
     *
     * @param coordinates Coordinates (x1, y1, x2, y2, ...).
     */
    public Polyline(double... coordinates) {
        this(null, coordinates);
    }

    protected Polyline(String tag) {
        super(tag);
    }

    protected Polyline(String tag, double... coordinates) {
        this(tag == null ? "polyline" : tag);
        for (int i = 0; i < coordinates.length; i += 2) {
            addPoint(coordinates[i], coordinates[i + 1]);
        }
    }

    /**
     * Add a point.
     *
     * @param point Point to add.
     */
    public void addPoint(Point point) {
        points.add(point);
        built = false;
    }

    /**
     * Add a point.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public void addPoint(double x, double y) {
        addPoint(new Point(x, y));
    }

    /**
     * Create a polyline.
     *
     * @param coordinates Coordinates (x1, y1, x2, y2, ...).
     * @return Polyline.
     */
    public static Polyline of(double... coordinates) {
        return new Polyline(coordinates);
    }

    /**
     * Add a point.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return Self.
     */
    public Polyline point(double x, double y) {
        return point(new Point(x, y));
    }

    /**
     * Add a point.
     *
     * @param point Point to add.
     * @return Self.
     */
    public Polyline point(Point point) {
        addPoint(point.x(), point.y());
        return this;
    }

    @Override
    public void build() {
        if (built) {
            return;
        }
        String pStr = points.stream()
                .map(p -> Svg.toString(p.x(), 4) + "," + Svg.toString(p.y(), 4))
                .collect(Collectors.joining(" "));
        svg = styleStart() + "points=\"" + pStr + "\"/>" + styleEnd();
        width = points.stream().mapToDouble(Point::x).max().orElse(0);
        height = points.stream().mapToDouble(Point::y).max().orElse(0);
        built = true;
    }
}
