package com.storedobject.svg;

/**
 * A wrapper for {@link Svg} that allows it to be moved.
 *
 * @author Syam
 */
public final class MovableSvg extends TransformedSvg {

    private final Mover mover = new Mover();

    /**
     * Constructor.
     *
     * @param embedded SVG to be wrapped.
     */
    private MovableSvg(Svg embedded) {
        super(embedded);
        transform(new Mover());
    }

    /**
     * Create a {@link MovableSvg} from an {@link Svg}.
     *
     * @param embedded SVG to be wrapped.
     * @return MovableSvg.
     */
    public static MovableSvg create(Svg embedded) {
        return embedded instanceof MovableSvg m ? m : new MovableSvg(embedded);
    }

    /**
     * Move to a point.
     *
     * @param p Point.
     */
    public void move(Point p) {
        mover.move(p);
    }

    /**
     * Move by displacement.
     *
     * @param x X displacement.
     * @param y Y displacement.
     */
    public void move(double x, double y) {
        mover.move(x, y);
    }
}
