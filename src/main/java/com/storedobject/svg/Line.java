package com.storedobject.svg;

/**
 * Represents an SVG line.
 *
 * @author Syam
 */
public class Line extends Element {

    private double x1, y1, x2, y2;

    /**
     * Constructor.
     *
     * @param p1 Start point.
     * @param p2 End point.
     */
    public Line(Point p1, Point p2) {
        this(p1.x(), p1.y(), p2.x(), p2.y());
    }

    /**
     * Constructor.
     *
     * @param x1 Start X coordinate.
     * @param y1 Start Y coordinate.
     * @param x2 End X coordinate.
     * @param y2 End Y coordinate.
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Constructor.
     */
    public Line() {
        this(0, 0, 0, 0);
    }

    /**
     * Set the start X coordinate.
     *
     * @param x1 Start X coordinate.
     */
    public void setX1(double x1) {
        this.x1 = x1;
        built = false;
    }

    /**
     * Get the start point.
     *
     * @return Start point.
     */
    public Point getFrom() {
        return new Point(x1, y1);
    }

    /**
     * Get the end point.
     *
     * @return End point.
     */
    public Point getTo() {
        return new Point(x2, y2);
    }

    /**
     * Get the start X coordinate.
     *
     * @return Start X coordinate.
     */
    public double getX1() {
        return x1;
    }

    /**
     * Set the start Y coordinate.
     *
     * @param y1 Start Y coordinate.
     */
    public void setY1(double y1) {
        this.y1 = y1;
        built = false;
    }

    /**
     * Get the start Y coordinate.
     *
     * @return Start Y coordinate.
     */
    public double getY1() {
        return y1;
    }

    /**
     * Set the end X coordinate.
     *
     * @param x2 End X coordinate.
     */
    public void setX2(double x2) {
        this.x2 = x2;
        built = false;
    }

    /**
     * Get the end X coordinate.
     *
     * @return End X coordinate.
     */
    public double getX2() {
        return x2;
    }

    /**
     * Set the end Y coordinate.
     *
     * @param y2 End Y coordinate.
     */
    public void setY2(double y2) {
        this.y2 = y2;
        built = false;
    }

    /**
     * Get the end Y coordinate.
     *
     * @return End Y coordinate.
     */
    public double getY2() {
        return y2;
    }

    /**
     * Create a line.
     *
     * @param x1 Start X coordinate.
     * @param y1 Start Y coordinate.
     * @param x2 End X coordinate.
     * @param y2 End Y coordinate.
     * @return Line.
     */
    public static Line of(double x1, double y1, double x2, double y2) {
        return new Line(x1, y1, x2, y2);
    }

    /**
     * Create a line.
     *
     * @param p1 Start point.
     * @param p2 End point.
     * @return Line.
     */
    public static Line of(Point p1, Point p2) {
        return new Line(p1, p2);
    }

    /**
     * Set the start X coordinate.
     *
     * @param x1 Start X coordinate.
     * @return Self.
     */
    public Line x1(double x1) {
        setX1(x1);
        return this;
    }

    /**
     * Set the start Y coordinate.
     *
     * @param y1 Start Y coordinate.
     * @return Self.
     */
    public Line y1(double y1) {
        setY1(y1);
        return this;
    }

    /**
     * Set the end X coordinate.
     *
     * @param x2 End X coordinate.
     * @return Self.
     */
    public Line x2(double x2) {
        setX2(x2);
        return this;
    }

    /**
     * Set the end Y coordinate.
     *
     * @param y2 End Y coordinate.
     * @return Self.
     */
    public Line y2(double y2) {
        setY2(y2);
        return this;
    }

    /**
     * Set the start point.
     *
     * @param x1 Start X coordinate.
     * @param y1 Start Y coordinate.
     * @return Self.
     */
    public Line from(double x1, double y1) {
        setX1(x1);
        setY1(y1);
        return this;
    }

    /**
     * Set the end point.
     *
     * @param x2 End X coordinate.
     * @param y2 End Y coordinate.
     * @return Self.
     */
    public Line to(double x2, double y2) {
        setX2(x2);
        setY2(y2);
        return this;
    }

    /**
     * Set the start point.
     *
     * @param p1 Start point.
     * @return Self.
     */
    public Line from(Point p1) {
        setX1(p1.x());
        setY1(p1.y());
        return this;
    }

    /**
     * Set the end point.
     *
     * @param p2 End point.
     * @return Self.
     */
    public Line to(Point p2) {
        setX2(p2.x());
        setY2(p2.y());
        return this;
    }

    @Override
    public void build() {
        if(built) {
            return;
        }
        svg = styles("line") + "<line x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\"/>";
        width = Math.max(x1, x2);
        height = Math.max(y1, y2);
        built = true;
    }
}
