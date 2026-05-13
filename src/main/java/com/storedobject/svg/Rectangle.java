package com.storedobject.svg;

/**
 * Represents an SVG rectangle.
 *
 * @author Syam
 */
public class Rectangle extends Element {

    private double x, y, w, h;

    /**
     * Constructor.
     *
     * @param p Top-left point.
     * @param w Width.
     * @param h Height.
     */
    public Rectangle(Point p, double w, double h) {
        this(p.x(), p.y(), w, h);
    }

    /**
     * Constructor.
     *
     * @param x Top-left X coordinate.
     * @param y Top-left Y coordinate.
     * @param w Width.
     * @param h Height.
     */
    public Rectangle(double x, double y, double w, double h) {
        super("rect");
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Constructor.
     *
     * @param w Width.
     * @param h Height.
     */
    public Rectangle(double w, double h) {
        this(0, 0, w, h);
    }

    /**
     * Constructor.
     *
     * @param p Top-left point.
     */
    public Rectangle(Point p) {
        this(p.x(), p.y(), 0, 0);
    }

    /**
     * Constructor.
     */
    public Rectangle() {
        this(0, 0, 0, 0);
    }

    /**
     * Get the position.
     *
     * @return Position.
     */
    public Point getPosition() {
        return new Point(x, y);
    }

    /**
     * Set the position.
     *
     * @param p Position.
     */
    public void setPosition(Point p) {
        setX(p.x());
        setY(p.y());
    }

    /**
     * Set the X coordinate.
     *
     * @param x X coordinate.
     */
    public void setX(double x) {
        this.x = x;
        built = false;
    }

    /**
     * Set the Y coordinate.
     *
     * @param y Y coordinate.
     */
    public void setY(double y) {
        this.y = y;
        built = false;
    }

    /**
     * Set the width.
     *
     * @param w Width.
     */
    public void setW(double w) {
        this.w = w;
        built = false;
    }

    /**
     * Get the width.
     *
     * @return Width.
     */
    public double getW() {
        return w;
    }

    /**
     * Set the height.
     *
     * @param h Height.
     */
    public void setH(double h) {
        this.h = h;
        built = false;
    }

    /**
     * Get the height.
     *
     * @return Height.
     */
    public double getH() {
        return h;
    }

    /**
     * Create a rectangle.
     *
     * @param p Top-left point.
     * @param w Width.
     * @param h Height.
     * @return Rectangle.
     */
    public static Rectangle of(Point p, double w, double h) {
        return new Rectangle(p, w, h);
    }

    /**
     * Create a rectangle.
     *
     * @param x Top-left X coordinate.
     * @param y Top-left Y coordinate.
     * @param w Width.
     * @param h Height.
     * @return Rectangle.
     */
    public static Rectangle of(double x, double y, double w, double h) {
        return new Rectangle(x, y, w, h);
    }

    /**
     * Create a rectangle at (0, 0).
     *
     * @param w Width.
     * @param h Height.
     * @return Rectangle.
     */
    public static Rectangle of(double w, double h) {
        return new Rectangle(w, h);
    }

    /**
     * Set the position.
     *
     * @param p Position.
     * @return Self.
     */
    public Rectangle at(Point p) {
        setPosition(p);
        return this;
    }

    /**
     * Set the position.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return Self.
     */
    public Rectangle at(double x, double y) {
        setX(x);
        setY(y);
        return this;
    }

    /**
     * Set the X coordinate.
     *
     * @param x X coordinate.
     * @return Self.
     */
    public Rectangle x(double x) {
        setX(x);
        return this;
    }

    /**
     * Set the Y coordinate.
     *
     * @param y Y coordinate.
     * @return Self.
     */
    public Rectangle y(double y) {
        setY(y);
        return this;
    }

    /**
     * Set the width.
     *
     * @param w Width.
     * @return Self.
     */
    public Rectangle width(double w) {
        setW(w);
        return this;
    }

    /**
     * Set the height.
     *
     * @param h Height.
     * @return Self.
     */
    public Rectangle height(double h) {
        setH(h);
        return this;
    }

    @Override
    public void build() {
        if(built) {
            return;
        }
        svg = styleStart() + "x=\"" + x + "\" y=\"" + y + "\" width=\"" + w + "\" height=\"" + h + "\"/>" + styleEnd();
        this.width = x + w;
        this.height = y + h;
        built = true;
    }
}
