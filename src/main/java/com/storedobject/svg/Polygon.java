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
        super("polygon");
    }

    /**
     * Constructor.
     *
     * @param coordinates Coordinates (x1, y1, x2, y2, ...).
     */
    public Polygon(double... coordinates) {
        super("polygon", coordinates);
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
}
