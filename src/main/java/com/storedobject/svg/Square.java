package com.storedobject.svg;

/**
 * Represents an SVG square.
 *
 * @author Syam
 */
public class Square extends Rectangle {

    /**
     * Constructor.
     *
     * @param p Top-left point.
     * @param size Size.
     */
    public Square(Point p, double size) {
        super(p, size, size);
    }

    /**
     * Constructor.
     *
     * @param x Top-left X coordinate.
     * @param y Top-left Y coordinate.
     * @param size Size.
     */
    public Square(double x, double y, double size) {
        super(x, y, size, size);
    }

    /**
     * Constructor.
     *
     * @param size Size.
     */
    public Square(double size) {
        super(size, size);
    }

    /**
     * Constructor.
     */
    public Square() {
        super();
    }

    /**
     * Constructor.
     *
     * @param p Top-left point.
     */
    public Square(Point p) {
        super(p, 0, 0);
    }

    /**
     * Set the size.
     *
     * @param size Size.
     */
    public void setSize(double size) {
        super.setW(size);
        super.setH(size);
    }

    /**
     * Get the size.
     *
     * @return Size.
     */
    public double getSize() {
        return getW();
    }

    @Override
    public void setW(double w) {
        setSize(w);
    }

    @Override
    public void setH(double h) {
        setSize(h);
    }

    /**
     * Create a square.
     *
     * @param p Top-left point.
     * @param size Size.
     * @return Square.
     */
    public static Square of(Point p, double size) {
        return new Square(p, size);
    }

    /**
     * Create a square.
     *
     * @param x Top-left X coordinate.
     * @param y Top-left Y coordinate.
     * @param size Size.
     * @return Square.
     */
    public static Square of(double x, double y, double size) {
        return new Square(x, y, size);
    }

    /**
     * Create a square at (0, 0).
     *
     * @param size Size.
     * @return Square.
     */
    public static Square of(double size) {
        return new Square(size);
    }

    /**
     * Set the size.
     *
     * @param size Size.
     * @return Self.
     */
    public Square size(double size) {
        setSize(size);
        return this;
    }

    @Override
    public Square at(Point p) {
        super.at(p);
        return this;
    }

    @Override
    public Square at(double x, double y) {
        super.at(x, y);
        return this;
    }

    @Override
    public Square x(double x) {
        super.x(x);
        return this;
    }

    @Override
    public Square y(double y) {
        super.y(y);
        return this;
    }

    @Override
    public Square width(double w) {
        setSize(w);
        return this;
    }

    @Override
    public Square height(double h) {
        setSize(h);
        return this;
    }
}
