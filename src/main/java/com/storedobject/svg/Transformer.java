package com.storedobject.svg;

/**
 * Interface for transforming a node.
 *
 * @author Syam
 */
public interface Transformer {

    /**
     * Transform the width.
     *
     * @param width Width.
     * @return Transformed width.
     */
    double transformWidth(double width);

    /**
     * Transform the height.
     *
     * @param height Height.
     * @return Transformed height.
     */
    double transformHeight(double height);

    /**
     * Transform the X coordinate.
     *
     * @param x X coordinate.
     * @return Transformed X coordinate.
     */
    default double transformX(double x) {
        return x;
    }

    /**
     * Transform the Y coordinate.
     *
     * @param y Y coordinate.
     * @return Transformed Y coordinate.
     */
    default double transformY(double y) {
        return y;
    }

    /**
     * Transform the node content.
     *
     * @param svg Node content.
     * @return Transformed node content.
     */
    String transform(String svg);
}
