package com.storedobject.svg;

import com.storedobject.svg.chart.Values;

/**
 * Represents a point in 2D space.
 *
 * @param x X coordinate.
 * @param y Y coordinate.
 * @author Syam
 */
public record Point(double x, double y) {

    /**
     * Converts the X coordinate of the point to its string representation.
     *
     * @return A string representation of the X coordinate.
     */
    public String sx() {
        return Values.toString(x);
    }

    /**
     * Converts the Y coordinate of the point to its string representation.
     *
     * @return A string representation of the Y coordinate.
     */
    public String sy() {
        return Values.toString(y);
    }
}
