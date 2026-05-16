package com.storedobject.svg;

/**
 * A wrapper for {@link Node} that allows it to be moved.
 *
 * @author Syam
 */
public final class MovableNode extends TransformedNode {

    private final Mover mover = new Mover();

    /**
     * Constructor.
     *
     * @param embedded Node to be wrapped.
     */
    private MovableNode(Node embedded) {
        super(embedded);
        transform(new Mover());
    }

    /**
     * Create a {@link MovableNode} from an {@link Node}.
     *
     * @param embedded Node to be wrapped.
     * @return MovableSvg.
     */
    public static MovableNode create(Node embedded) {
        return embedded instanceof MovableNode m ? m : new MovableNode(embedded);
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
     * @param x New x coordinate.
     * @param y New y coordinate.
     */
    public void move(double x, double y) {
        mover.move(x, y);
    }
}
