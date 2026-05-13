package com.storedobject.svg;

/**
 * Represents an SVG circle.
 *
 * @author Syam
 */
public class Circle extends Element {

    private double cx, cy, r;

    /**
     * Constructor.
     *
     * @param center Center point.
     * @param r Radius.
     */
    public Circle(Point center, double r) {
        this(center.x(), center.y(), r);
    }

    /**
     * Constructor.
     *
     * @param cx Center X coordinate.
     * @param cy Center Y coordinate.
     * @param r Radius.
     */
    public Circle(double cx, double cy, double r) {
        super("circle");
        this.cx = cx;
        this.cy = cy;
        this.r = r;
    }

    /**
     * Constructor.
     *
     * @param r Radius.
     */
    public Circle(double r) {
        this(r, r, r);
    }

    /**
     * Constructor.
     */
    public Circle() {
        this(0);
    }

    /**
     * Constructor.
     *
     * @param center Center point.
     */
    public Circle(Point center) {
        this(center.x(), center.y(), 0);
    }

    /**
     * Constructor.
     *
     * @param cx Center X coordinate.
     * @param cy Center Y coordinate.
     */
    public Circle(double cx, double cy) {
        this(cx, cy, 0);
    }

    /**
     * Set the radius.
     *
     * @param r Radius.
     */
    public void setRadius(double r) {
        this.r = r;
        built = false;
    }

    /**
     * Get the radius.
     *
     * @return Radius.
     */
    public double getRadius() {
        return r;
    }

    /**
     * Set the center.
     *
     * @param center Center point.
     */
    public void setCenter(Point center) {
        setCenter(center.x(), center.y());
    }

    /**
     * Set the center.
     *
     * @param cx Center X coordinate.
     * @param cy Center Y coordinate.
     */
    public void setCenter(double cx, double cy) {
        this.cx = cx;
        this.cy = cy;
        built = false;
    }

    /**
     * Get the center point.
     *
     * @return Center point.
     */
    public Point getCenter() {
        return new Point(cx, cy);
    }

    /**
     * Get the center X coordinate.
     *
     * @return Center X coordinate.
     */
    public double getCenterX() {
        return cx;
    }

    /**
     * Get the center Y coordinate.
     *
     * @return Center Y coordinate.
     */
    public double getCenterY() {
        return cy;
    }

    /**
     * Create a circle.
     *
     * @param center Center point.
     * @param r Radius.
     * @return Circle.
     */
    public static Circle of(Point center, double r) {
        return new Circle(center, r);
    }

    /**
     * Create a circle.
     *
     * @param cx Center X coordinate.
     * @param cy Center Y coordinate.
     * @param r Radius.
     * @return Circle.
     */
    public static Circle of(double cx, double cy, double r) {
        return new Circle(cx, cy, r);
    }

    /**
     * Create a circle.
     *
     * @param r Radius.
     * @return Circle.
     */
    public static Circle of(double r) {
        return new Circle(r);
    }

    /**
     * Set the center.
     *
     * @param center Center point.
     * @return Self.
     */
    public Circle center(Point center) {
        setCenter(center);
        return this;
    }

    /**
     * Set the center.
     *
     * @param cx Center X coordinate.
     * @param cy Center Y coordinate.
     * @return Self.
     */
    public Circle center(double cx, double cy) {
        setCenter(cx, cy);
        return this;
    }

    /**
     * Set the radius.
     *
     * @param r Radius.
     * @return Self.
     */
    public Circle radius(double r) {
        setRadius(r);
        return this;
    }

    @Override
    public void build() {
        if(built) {
            return;
        }
        svg = styleStart() + "cx=\"" + cx + "\" cy=\"" + cy + "\" r=\"" + r + "\"/>" + styleEnd();
        width = cx + r;
        height = cy + r;
        built = true;
    }
}
