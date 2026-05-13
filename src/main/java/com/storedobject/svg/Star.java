package com.storedobject.svg;

/**
 * Represents an SVG star.
 *
 * @author Syam
 */
public class Star extends Polygon {

    private double cx, cy, innerRadius, outerRadius;
    private int pointsCount;

    /**
     * Constructor.
     *
     * @param innerRadius Inner radius.
     * @param outerRadius Outer radius.
     * @param points Number of points.
     */
    public Star(double innerRadius, double outerRadius, int points) {
        this(0, 0, innerRadius, outerRadius, points);
    }

    /**
     * Constructor.
     *
     * @param center Center point.
     * @param innerRadius Inner radius.
     * @param outerRadius Outer radius.
     * @param points Number of points.
     */
    public Star(Point center, double innerRadius, double outerRadius, int points) {
        this(center.x(), center.y(), innerRadius, outerRadius, points);
    }

    /**
     * Constructor.
     *
     * @param cx Center X coordinate.
     * @param cy Center Y coordinate.
     * @param innerRadius Inner radius.
     * @param outerRadius Outer radius.
     * @param points Number of points.
     */
    public Star(double cx, double cy, double innerRadius, double outerRadius, int points) {
        this.cx = cx;
        this.cy = cy;
        this.innerRadius = Math.min(innerRadius, outerRadius);
        this.outerRadius = Math.max(innerRadius, outerRadius);
        this.pointsCount = points;
    }

    /**
     * Constructor.
     */
    public Star() {
        this(0, 0, 10, 20, 5);
    }

    /**
     * Set the center point.
     *
     * @param center Center point.
     */
    public void setCenter(Point center) {
        setCenter(center.x(), center.y());
    }

    /**
     * Set the center point.
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
     * Set the inner radius.
     *
     * @param innerRadius Inner radius.
     */
    public void setInnerRadius(double innerRadius) {
        this.innerRadius = innerRadius;
        built = false;
    }

    /**
     * Get the inner radius.
     *
     * @return Inner radius.
     */
    public double getInnerRadius() {
        return innerRadius;
    }

    /**
     * Set the outer radius.
     *
     * @param outerRadius Outer radius.
     */
    public void setOuterRadius(double outerRadius) {
        this.outerRadius = outerRadius;
        built = false;
    }

    /**
     * Get the outer radius.
     *
     * @return Outer radius.
     */
    public double getOuterRadius() {
        return outerRadius;
    }

    /**
     * Set the number of points.
     *
     * @param points Number of points.
     */
    public void setPoints(int points) {
        this.pointsCount = points;
        built = false;
    }

    /**
     * Get the number of points.
     *
     * @return Number of points.
     */
    public int getPoints() {
        return pointsCount;
    }

    /**
     * Create a star.
     *
     * @param center Center point.
     * @param innerRadius Inner radius.
     * @param outerRadius Outer radius.
     * @param points Number of points.
     * @return Star.
     */
    public static Star of(Point center, double innerRadius, double outerRadius, int points) {
        return new Star(center, innerRadius, outerRadius, points);
    }

    /**
     * Create a star.
     *
     * @param cx Center X coordinate.
     * @param cy Center Y coordinate.
     * @param innerRadius Inner radius.
     * @param outerRadius Outer radius.
     * @param points Number of points.
     * @return Star.
     */
    public static Star of(double cx, double cy, double innerRadius, double outerRadius, int points) {
        return new Star(cx, cy, innerRadius, outerRadius, points);
    }

    /**
     * Set the center.
     *
     * @param center Center point.
     * @return Self.
     */
    public Star center(Point center) {
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
    public Star center(double cx, double cy) {
        setCenter(cx, cy);
        return this;
    }

    /**
     * Set the inner radius.
     *
     * @param innerRadius Inner radius.
     * @return Self.
     */
    public Star innerRadius(double innerRadius) {
        setInnerRadius(innerRadius);
        return this;
    }

    /**
     * Set the outer radius.
     *
     * @param outerRadius Outer radius.
     * @return Self.
     */
    public Star outerRadius(double outerRadius) {
        setOuterRadius(outerRadius);
        return this;
    }

    /**
     * Set the number of points.
     *
     * @param points Number of points.
     * @return Self.
     */
    public Star points(int points) {
        setPoints(points);
        return this;
    }

    @Override
    public void build() {
        if (built) {
            return;
        }
        points.clear();
        double angle = Math.PI / pointsCount;
        for (int i = 0; i < 2 * pointsCount; i++) {
            double r = (i % 2 == 0) ? outerRadius : innerRadius;
            double x = cx + Math.cos(i * angle - Math.PI / 2) * r;
            double y = cy + Math.sin(i * angle - Math.PI / 2) * r;
            addPoint(x, y);
        }
        super.build();
    }
}
