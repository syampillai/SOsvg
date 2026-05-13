package com.storedobject.svg;

/**
 * Represents an SVG ellipse.
 *
 * @author Syam
 */
public class Ellipse extends Element {

    private double cx, cy, rx, ry;

    /**
     * Constructor.
     *
     * @param center Center point.
     * @param rx Radius X.
     * @param ry Radius Y.
     */
    public Ellipse(Point center, double rx, double ry) {
        this(center.x(), center.y(), rx, ry);
    }

    /**
     * Constructor.
     *
     * @param cx Center X coordinate.
     * @param cy Center Y coordinate.
     * @param rx Radius X.
     * @param ry Radius Y.
     */
    public Ellipse(double cx, double cy, double rx, double ry) {
        super("ellipse");
        this.cx = cx;
        this.cy = cy;
        this.rx = rx;
        this.ry = ry;
    }

    /**
     * Constructor.
     *
     * @param rx Radius X.
     * @param ry Radius Y.
     */
    public Ellipse(double rx, double ry) {
        this(rx, ry, rx, ry);
    }

    /**
     * Constructor.
     *
     * @param center Center point.
     */
    public Ellipse(Point center) {
        this(center.x(), center.y(), 0, 0);
    }

    /**
     * Constructor.
     */
    public Ellipse() {
        this(0, 0, 0, 0);
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
     * Set the radius.
     *
     * @param rx Radius X.
     * @param ry Radius Y.
     */
    public void setRadius(double rx, double ry) {
        this.rx = rx;
        this.ry = ry;
        built = false;
    }

    /**
     * Get the radius X.
     *
     * @return Radius X.
     */
    public double getRadiusX() {
        return rx;
    }

    /**
     * Get the radius Y.
     *
     * @return Radius Y.
     */
    public double getRadiusY() {
        return ry;
    }

    /**
     * Create an ellipse.
     *
     * @param center Center point.
     * @param rx Radius X.
     * @param ry Radius Y.
     * @return Ellipse.
     */
    public static Ellipse of(Point center, double rx, double ry) {
        return new Ellipse(center, rx, ry);
    }

    /**
     * Create an ellipse.
     *
     * @param cx Center X coordinate.
     * @param cy Center Y coordinate.
     * @param rx Radius X.
     * @param ry Radius Y.
     * @return Ellipse.
     */
    public static Ellipse of(double cx, double cy, double rx, double ry) {
        return new Ellipse(cx, cy, rx, ry);
    }

    /**
     * Create an ellipse at (rx, ry).
     *
     * @param rx Radius X.
     * @param ry Radius Y.
     * @return Ellipse.
     */
    public static Ellipse of(double rx, double ry) {
        return new Ellipse(rx, ry);
    }

    /**
     * Set the center.
     *
     * @param center Center point.
     * @return Self.
     */
    public Ellipse center(Point center) {
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
    public Ellipse center(double cx, double cy) {
        setCenter(cx, cy);
        return this;
    }

    /**
     * Set the radius.
     *
     * @param rx Radius X.
     * @param ry Radius Y.
     * @return Self.
     */
    public Ellipse radius(double rx, double ry) {
        setRadius(rx, ry);
        return this;
    }

    @Override
    public void build() {
        if(built) {
            return;
        }
        svg = styleStart() + "cx=\"" + cx + "\" cy=\"" + cy + "\" rx=\"" + rx + "\" ry=\"" + ry
                + "\"/>" + styleEnd();
        width = cx + rx;
        height = cy + ry;
        built = true;
    }
}
