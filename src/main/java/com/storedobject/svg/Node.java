package com.storedobject.svg;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleSupplier;

/**
 * Base class for all SVG objects. This represents a single visual unit or part of an SVG document. It has got its own
 * size and origin, and can be independently translated and scaled. Many such node objects can be combined to form
 * a larger visual composition.
 *
 * @author Syam
 */
public abstract class Node {

    private static final NumberFormat format = NumberFormat.getNumberInstance();
    static {
        format.setMaximumIntegerDigits(30);
        format.setRoundingMode(RoundingMode.HALF_UP);
    }
    private static final AtomicLong ID = new AtomicLong();

    /**
     * Unique identifier.
     */
    protected final long id = IDValue();

    /**
     * Default constructor for the {@code Node} class.
     * Initializes a new instance of the node object with default properties.
     */
    public Node() {
    }

    /**
     * SVG content of the node. This is typically set by the build() method. It should not contain the top level svg tag.
     * <p>Note: It can contain one or more svg tags clubbed together to form a logical unit or visual part.</p>
     */
    protected String svg;

    /**
     * Current x and y coordinates of this node. These could be set to any value so that the final SVG output
     * will be translated to this location.
     */
    protected double x = 0, y = 0;

    /**
     * The width and height of the node content.
     */
    protected double width = 600, height = 400;

    /**
     * Build the content. This method is called internally to get before obtaining the output.
     */
    public abstract void build();

    /**
     * Check if the content is built.
     *
     * @return True if built.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public abstract boolean isBuilt();

    /**
     * Retrieves the unique identifier of the node.
     *
     * @return The unique identifier as a long value.
     */
    public final long getId() {
        return id;
    }

    /**
     * Convert a double to string with specified decimal places and remove trailing zeros.
     *
     * @param v Value.
     * @param decimals Number of decimal places.
     * @return Formatted string.
     */
    public static String toString(double v, int decimals) {
        String s = format(v, decimals);
        if(s.contains(".")) {
            while (s.endsWith("0")) {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }

    private static String format(double value, int decimals) {
        boolean negative = value < 0;
        if(negative) {
            value = -value;
        }
        if(decimals < 0) {
            format.setMinimumFractionDigits(decimals < -1 ? -decimals : 14);
            format.setMaximumFractionDigits(decimals < -1 ? -decimals : 14);
        } else {
            format.setMinimumFractionDigits(decimals);
            format.setMaximumFractionDigits(decimals);
        }
        String s = format.format(value);
        if(negative) {
            return "-" + s;
        }
        return s;
    }

    /**
     * Escape XML special characters in a string.
     *
     * @param text Text to escape.
     * @return Escaped string.
     */
    public static String escapeXml(String text) {
        if(text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    /**
     * Generates a globally unique identifier in a thread-safe manner.
     * The method increments the current ID value atomically. If the ID value
     * reaches {@code Long.MAX_VALUE}, it resets to 0 before incrementing.
     *
     * @return A unique identifier as a long value.
     */
    public static long IDValue() {
        if(ID.get() == Long.MAX_VALUE) {
            ID.set(0);
        }
        return ID.incrementAndGet();
    }

    /**
     * Generates a unique string identifier for the given numerical ID.
     *
     * @param id The numerical ID to be converted into a string identifier.
     * @return A string in the format "so-svg-" followed by the numerical ID.
     */
    public static String ID(long id) {
        return "so-svg-" + id;
    }

    /**
     * Returns the width of the content. This is the same as the view box width if x and y are 0.
     *
     * @return The width of the content.
     */
    public final double getWidth() {
        return width;
    }

    /**
     * Returns the height of the content. This is the same as the view box height if x and y are 0.
     *
     * @return The height of the content.
     */
    public final double getHeight() {
        return height;
    }

    /**
     * Returns the X coordinate of the content.
     *
     * @return The X coordinate.
     */
    public final double getX() {
        return x;
    }

    /**
     * Returns the Y coordinate of the content.
     *
     * @return The Y coordinate.
     */
    public final double getY() {
        return y;
    }

    /**
     * Add a margin to this content.
     *
     * @param margin Margin value to be added to each side.
     * @return A new {@link Node} with the margin added.
     */
    public Node margin(double margin) {
        return margin(margin, null);
    }

    /**
     * Add a margin to this SVG on a specific side.
     *
     * @param margin Margin value.
     * @param side Side on which to add the margin.
     * @return A new {@link Node} with the margin added.
     */
    public Node margin(double margin, Side side) {
        if(margin <= 0) {
            return this;
        }
        return new TransformedNode(this, new Margin(margin, side));
    }

    /**
     * Add another node to this one at a specified side.
     *
     * @param node Node to add.
     * @param side Side where the node should be added.
     * @return A new {@link Node} containing both.
     */
    public Node add(Node node, Side side) {
        if(node == null) {
            return this;
        }
        DoubleSupplier computeX = null, computeY = null;
        if(side != null) {
            switch (side) {
                case Left -> computeX = () -> this.x - node.width;
                case Right -> computeX = () -> this.x + this.width;
                case Top -> computeY = () -> this.y - node.height;
                case Bottom -> computeY = () -> this.y + this.height;
            }
        }
        return new TransformedNode(node, new Anchor(this, computeX, computeY));
    }

    /**
     * Add another node at a specific point.
     *
     * @param node Node to add.
     * @param at Point where the node should be added.
     * @return A new {@link Node} containing both.
     */
    public Node add(Node node, Point at) {
        return add(node, at.x(), at.y());
    }

    /**
     * Add another node at a specific displacement.
     *
     * @param node Node to add.
     * @param dx X displacement.
     * @param dy Y displacement.
     * @return A new {@link Node} containing both.
     */
    public Node add(Node node, double dx, double dy) {
        return new TransformedNode(node,
                new Anchor(this, () -> this.x + node.x + dx, () -> this.y + node.y + dy));
    }

    /**
     * Scale this node content to a new width.
     *
     * @param newWidth New width.
     * @return A new {@link Node} scaled to the new width.
     */
    public Node scaleToWidth(double newWidth) {
        return new TransformedNode(this, new ScaleTo(this, newWidth, -1));
    }

    /**
     * Scale this node content to a new height.
     *
     * @param newHeight New height.
     * @return A new {@link Node} scaled to the new height.
     */
    public Node scaleToHeight(double newHeight) {
        return new TransformedNode(this, new ScaleTo(this, -1, newHeight));
    }

    /**
     * Scale this node content to new dimensions.
     *
     * @param newWidth New width.
     * @param newHeight New height.
     * @return A new {@link Node} scaled to the new dimensions.
     */
    public Node scaleTo(double newWidth, double newHeight) {
        return new TransformedNode(this, new ScaleTo(this, newWidth, newHeight));
    }

    /**
     * Scale this node content by specified factors.
     *
     * @param scaleX X scale factor.
     * @param scaleY Y scale factor.
     * @return A new {@link Node} scaled.
     */
    public Node scale(double scaleX, double scaleY) {
        return new TransformedNode(this, new Scale(this, scaleX, scaleY));
    }

    /**
     * Scale this node content by a specified factor.
     *
     * @param scale Scale factor.
     * @return A new {@link Node} scaled.
     */
    public Node scale(double scale) {
        return new TransformedNode(this, new Scale(this, scale, scale));
    }

    /**
     * Flip this node content horizontally.
     *
     * @return A new {@link Node} flipped horizontally.
     */
    public Node flipHorizontally() {
        return new TransformedNode(this, new Scale(this, -1, 1));
    }

    /**
     * Flip this node content vertically.
     *
     * @return A new {@link Node} flipped vertically.
     */
    public Node flipVertically() {
        return new TransformedNode(this, new Scale(this, 1, -1));
    }

    /**
     * Creates a new {@link Document} instance associated with the current {@code Node}.
     * This method initializes and returns a Document object for the node.
     *
     * @return A new {@link Document} instance associated with the current {@code Node}.
     */
    public final Document createDocument() {
        return new Document(this);
    }

    /**
     * Transformer for adding margins.
     *
     * @author Syam
     */
    public static class Margin implements Transformer {

        private double margin;
        private Side side;

        /**
         * Constructor.
         *
         * @param margin Margin value.
         */
        public Margin(double margin) {
            this(margin, null);
        }

        /**
         * Constructor.
         *
         * @param margin Margin value.
         * @param side Side.
         */
        public Margin(double margin, Side side) {
            this.margin = margin;
            this.side = side;
        }

        /**
         * Set the side.
         *
         * @param side Side.
         */
        public void setSide(Side side) {
            this.side = side;
        }

        /**
         * Get the side.
         *
         * @return Side.
         */
        public Side getSide() {
            return side;
        }

        /**
         * Get the margin.
         *
         * @return Margin.
         */
        public double getMargin() {
            return margin;
        }

        /**
         * Set the margin.
         *
         * @param margin Margin.
         */
        public void setMargin(double margin) {
            this.margin = margin;
        }

        @Override
        public final double transformWidth(double width) {
            if(side == null) {
                return width + (2 * margin);
            }
            return switch (side) {
                case Left, Right -> width + margin;
                case Top, Bottom -> width;
            };
        }

        @Override
        public final double transformHeight(double height) {
            if(side == null) {
                return height + (2 * margin);
            }
            return switch (side) {
                case Left, Right -> height;
                case Top, Bottom -> height + margin;
            };
        }

        @Override
        public final String transform(String svg) {
            if(side == null) {
                String m = Node.toString(margin, 2);
                return "<g transform=\"translate(" + m + "," + m + ")\">\n" + svg + "\n</g>";
            }
            return switch (side) {
                case Right, Bottom -> svg;
                case Left -> "<g transform=\"translate(" + Node.toString(margin, 2) + ",0)\">\n" + svg + "\n</g>";
                case Top -> "<g transform=\"translate(0," + Node.toString(margin, 2) + ")\">\n" + svg + "\n</g>";
            };
        }
    }

    /**
     * Transformer for moving a node.
     *
     * @author Syam
     */
    public static class Mover implements Transformer {

        private double x;
        private double y;

        /**
         * Constructor.
         */
        public Mover() {
            this(0, 0);
        }

        /**
         * Constructor.
         *
         * @param x X displacement.
         * @param y Y displacement.
         */
        public Mover(double x, double y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Constructor.
         *
         * @param p Displacement point.
         */
        public Mover(Point p) {
            this(p.x(), p.y());
        }

        /**
         * Move by specified displacement.
         *
         * @param x X displacement.
         * @param y Y displacement.
         */
        public void move(double x, double y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Move to a specified point.
         *
         * @param p Point.
         */
        public void move(Point p) {
            move(p.x(), p.y());
        }

        @Override
        public final double transformWidth(double width) {
            return x + width;
        }

        @Override
        public final double transformHeight(double height) {
            return y + height;
        }

        @Override
        public final String transform(String svg) {
            return "<g transform=\"translate(" + Node.toString(x, 2) + ","
                    + Node.toString(y, 2) + ")\">\n" + svg + "\n</g>";
        }
    }

    /**
     * Transformer for anchoring a node to another one.
     *
     * @author Syam
     */
    public static class Anchor implements Transformer {

        private final Node anchoredTo;
        private final DoubleSupplier computeX, computeY;

        /**
         * Constructor.
         *
         * @param anchoredTo Node to anchor to.
         * @param computeX Supplier for X coordinate.
         * @param computeY Supplier for Y coordinate.
         */
        public Anchor(Node anchoredTo, DoubleSupplier computeX, DoubleSupplier computeY) {
            this.anchoredTo = anchoredTo;
            this.computeX = computeX;
            this.computeY = computeY;
        }

        @Override
        public double transformWidth(double width) {
            return width;
        }

        @Override
        public double transformHeight(double height) {
            return height;
        }

        @Override
        public double transformX(double x) {
            if(!anchoredTo.isBuilt()) {
                anchoredTo.build();
            }
            if(computeX == null) {
                return x;
            }
            return computeX.getAsDouble();
        }

        @Override
        public double transformY(double y) {
            if(!anchoredTo.isBuilt()) {
                anchoredTo.build();
            }
            if(computeY == null) {
                return y;
            }
            return computeY.getAsDouble();
        }

        @Override
        public String transform(String svg) {
            return svg;
        }
    }

    /**
     * Base class for scale transformers.
     */
    static abstract class AbstractScale implements Transformer {

        /**
         * Represents the SVG instance associated with the scale transformation.
         * This variable provides access to the SVG data needed for coordinate
         * transformations and scaling operations.
         * It is initialized through the constructor of the containing class.
         */
        protected final Node node;

        /**
         * Constructor.
         *
         * @param node SVG to scale.
         */
        AbstractScale(Node node) {
            this.node = node;
        }

        /**
         * Returns X coordinate as string.
         *
         * @return X coordinate.
         */
        protected String x() {
            return Node.toString(this.node.x, 2);
        }

        /**
         * Returns Y coordinate as string.
         *
         * @return Y coordinate.
         */
        protected String y() {
            return Node.toString(this.node.y, 2);
        }

        /**
         * Returns negative X coordinate as string.
         *
         * @return Negative X coordinate.
         */
        protected String xReverse() {
            return Node.toString(-this.node.x, 2);
        }

        /**
         * Returns negative Y coordinate as string.
         *
         * @return Negative Y coordinate.
         */
        protected String yReverse() {
            return Node.toString(-this.node.y, 2);
        }
    }

    /**
     * Transformer for scaling to a specific size.
     *
     * @author Syam
     */
    public static class ScaleTo extends AbstractScale {

        private final double toWidth, toHeight;

        /**
         * Constructor.
         *
         * @param node SVG to scale.
         * @param toWidth New width.
         * @param toHeight New height.
         */
        public ScaleTo(Node node, double toWidth, double toHeight) {
            super(node);
            this.toWidth = toWidth;
            this.toHeight = toHeight;
        }

        @Override
        public double transformWidth(double width) {
            if(toWidth <= 0 && toHeight <= 0) {
                return width;
            }
            if(toWidth > 0) {
                return toWidth;
            }
            double scale = toHeight / node.height;
            return node.width * scale;
        }

        @Override
        public double transformHeight(double height) {
            if(toWidth <= 0 && toHeight <= 0) {
                return height;
            }
            if(toHeight > 0) {
                return toHeight;
            }
            double scale = toWidth / node.width;
            return node.height * scale;
        }

        @Override
        public String transform(String svg) {
            if(toWidth <= 0 && toHeight <= 0) {
                return svg;
            }
            String scaleX = Node.toString(transformWidth(this.node.width) / this.node.width, 2);
            String scaleY = Node.toString(transformHeight(this.node.height) / this.node.height, 2);
            return "<g transform=\"translate(" + x() + "," + y() + ") \"scale(" + scaleX + "," + scaleY
                    + ") translate(" + xReverse() + "," + yReverse() + ")\">" + svg + "</g>";
        }
    }

    /**
     * Transformer for scaling by factors.
     *
     * @author Syam
     */
    public static class Scale extends AbstractScale {

        private final double scaleX, scaleY;

        /**
         * Constructor.
         *
         * @param node SVG to scale.
         * @param scaleX X scale factor.
         * @param scaleY Y scale factor.
         */
        public Scale(Node node, double scaleX, double scaleY) {
            super(node);
            this.scaleX = scaleX;
            this.scaleY = scaleY;
        }

        @Override
        public double transformWidth(double width) {
            return Math.abs(width * scaleX);
        }

        @Override
        public double transformHeight(double height) {
            return Math.abs(height * scaleY);
        }

        @Override
        public String transform(String svg) {
            if(this.node.x == 0 && this.node.y == 0) {
                return "<g transform=\"scale(" + Node.toString(this.scaleX, 2)
                        + "," + Node.toString(this.scaleY, 2)
                        + ")\">" + svg + "</g>";
            }
            return "<g transform=\"translate(" + x() + "," + y() + ") scale(" + Node.toString(this.scaleX, 2)
                    + "," + Node.toString(this.scaleY, 2)
                    + ") translate(" + xReverse() + "," + yReverse() + ")\">" + svg + "</g>";
        }
    }
}
