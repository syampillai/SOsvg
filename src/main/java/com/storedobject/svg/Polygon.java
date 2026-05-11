package com.storedobject.svg;

import java.util.stream.Collectors;

/**
 * Represents an SVG polygon.
 *
 * @author Syam
 */
public class Polygon extends Polyline {

    /**
     * Constructor.
     */
    public Polygon() {
    }

    /**
     * Constructor.
     *
     * @param coordinates Coordinates (x1, y1, x2, y2, ...).
     */
    public Polygon(double... coordinates) {
        super(coordinates);
    }

    /**
     * Create a polygon.
     *
     * @param coordinates Coordinates (x1, y1, x2, y2, ...).
     * @return Polygon.
     */
    public static Polygon of(double... coordinates) {
        return new Polygon(coordinates);
    }

    @Override
    public Polygon point(double x, double y) {
        super.point(x, y);
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
        svg = styles("polygon") + "<polygon points=\"" + pStr + "\"/>";
        width = points.stream().mapToDouble(Point::x).max().orElse(0);
        height = points.stream().mapToDouble(Point::y).max().orElse(0);
        built = true;
    }
}
