package com.storedobject.svg;

import com.storedobject.common.StringUtility;
import java.util.function.DoubleSupplier;

/**
 * Base class for all SVG objects.
 *
 * @author Syam
 */
public abstract class Svg {

    /**
     * Default constructor for the {@code Svg} class.
     * Initializes a new instance of the SVG object with default properties.
     */
    public Svg() {
    }

    /**
     * SVG content. This is typically set by the build() method. It should not contain the top level svg tag.
     */
    protected String svg;

    /**
     * Current x and y coordinates. These could be set to any value so that the final SVG output will be translated.
     */
    protected double x, y;

    /**
     * Default size of the SVG. This could be set to any value so that the final SVG output will compute the view box
     * using these values and the values of x and y.
     */
    protected double width = 600, height = 400;

    /**
     * Build the SVG. This method is called before getting the SVG output.
     */
    public abstract void build();

    /**
     * Check if the chart is built.
     *
     * @return True if built.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public abstract boolean isBuilt();

    private String svg(String width, String height) {
        if(!isBuilt()) {
            build();
        }
        double w = this.width + x, h = this.height + y;
        String s;
        if(w <= 0 || h <= 0) { // Out of bounds
            h = y = 0.1;
            s = "<g></g>";
        } else {
            s = svg;
            if(x != 0 || y != 0) {
                s = "<g transform=\"translate(" + toString(x, 2) + ", " + toString(y, 2)
                        + ")\">\n" + s + "\n</g>";
            }
        }
        if(width == null) {
            width = toString(w, 2);
        }
        if(height == null) {
            height = toString(h, 2);
        }
        return "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 "
                + toString(w, 2) + " " + toString(h, 2)
                + "\" width=\"" + width + "\" height=\"" + height + "\">\n" + s + "\n</svg>";
    }

    /**
     * Get the SVG output using its default dimensions.
     *
     * @return SVG string.
     */
    public final String getSvg() {
        return svg(null, null);
    }

    /**
     * Get the SVG output that fills its container (100% width and height).
     *
     * @return SVG string.
     */
    public final String getFilledSvg() {
        return getScaledSvg(100, 100);
    }

    /**
     * Get the SVG output with specified scale percentages for width and height.
     *
     * @param scaleWidthPercentage Scale percentage for width (0 to 100).
     * @param scaleHeightPercentage Scale percentage for height (0 to 100).
     * @return SVG string.
     */
    public final String getScaledSvg(double scaleWidthPercentage, double scaleHeightPercentage) {
        return svg(scaleWidthPercentage <= 0 || scaleWidthPercentage > 100 ? "auto"
                        : toString(scaleWidthPercentage, 4) + "%",
                scaleHeightPercentage <= 0 || scaleHeightPercentage > 100 ? "auto"
                        : toString(scaleHeightPercentage, 4) + "%");
    }

    /**
     * Convert a double to string with specified decimal places and remove trailing zeros.
     *
     * @param v Value.
     * @param decimals Number of decimal places.
     * @return Formatted string.
     */
    public static String toString(double v, int decimals) {
        String s = StringUtility.format(v, decimals);
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
     * Returns the width of the SVG. This is the same as the view box width if x and y are 0.
     *
     * @return The width of the SVG.
     */
    public final double getWidth() {
        return width;
    }

    /**
     * Returns the height of the SVG. This is the same as the view box height if x and y are 0.
     *
     * @return The height of the SVG.
     */
    public final double getHeight() {
        return height;
    }

    /**
     * Returns the X coordinate.
     *
     * @return The X coordinate.
     */
    public final double getX() {
        return x;
    }

    /**
     * Returns the Y coordinate.
     *
     * @return The Y coordinate.
     */
    public final double getY() {
        return y;
    }

    /**
     * Add a margin to this SVG.
     *
     * @param margin Margin value.
     * @return A new {@link Svg} with the margin added.
     */
    public Svg margin(double margin) {
        return margin(margin, null);
    }

    /**
     * Add a margin to this SVG on a specific side.
     *
     * @param margin Margin value.
     * @param side Side on which to add the margin.
     * @return A new {@link Svg} with the margin added.
     */
    public Svg margin(double margin, Side side) {
        if(margin <= 0) {
            return this;
        }
        return new TransformedSvg(this, new Margin(margin, side));
    }

    /**
     * Add another SVG to this one at a specified side.
     *
     * @param svg SVG to add.
     * @param side Side where the SVG should be added.
     * @return A new {@link Svg} containing both.
     */
    public Svg add(Svg svg, Side side) {
        if(svg == null) {
            return this;
        }
        DoubleSupplier computeX = null, computeY = null;
        if(side != null) {
            switch (side) {
                case Left -> computeX = () -> this.x - svg.width;
                case Right -> computeX = () -> this.x + this.width;
                case Top -> computeY = () -> this.y - svg.height;
                case Bottom -> computeY = () -> this.y + this.height;
            }
        }
        return new TransformedSvg(svg, new Anchor(this, computeX, computeY));
    }

    /**
     * Add another SVG at a specific point.
     *
     * @param svg SVG to add.
     * @param at Point where the SVG should be added.
     * @return A new {@link Svg} containing both.
     */
    public Svg add(Svg svg, Point at) {
        return add(svg, at.x(), at.y());
    }

    /**
     * Add another SVG at a specific displacement.
     *
     * @param svg SVG to add.
     * @param dx X displacement.
     * @param dy Y displacement.
     * @return A new {@link Svg} containing both.
     */
    public Svg add(Svg svg, double dx, double dy) {
        return new TransformedSvg(svg,
                new Anchor(this, () -> this.x + svg.x + dx, () -> this.y + svg.y + dy));
    }

    /**
     * Scale this SVG to a new width.
     *
     * @param newWidth New width.
     * @return A new {@link Svg} scaled to the new width.
     */
    public Svg scaleToWidth(double newWidth) {
        return new TransformedSvg(this, new ScaleTo(this, newWidth, -1));
    }

    /**
     * Scale this SVG to a new height.
     *
     * @param newHeight New height.
     * @return A new {@link Svg} scaled to the new height.
     */
    public Svg scaleToHeight(double newHeight) {
        return new TransformedSvg(this, new ScaleTo(this, -1, newHeight));
    }

    /**
     * Scale this SVG to new dimensions.
     *
     * @param newWidth New width.
     * @param newHeight New height.
     * @return A new {@link Svg} scaled to the new dimensions.
     */
    public Svg scaleTo(double newWidth, double newHeight) {
        return new TransformedSvg(this, new ScaleTo(this, newWidth, newHeight));
    }

    /**
     * Scale this SVG by specified factors.
     *
     * @param scaleX X scale factor.
     * @param scaleY Y scale factor.
     * @return A new {@link Svg} scaled.
     */
    public Svg scale(double scaleX, double scaleY) {
        return new TransformedSvg(this, new Scale(this, scaleX, scaleY));
    }

    /**
     * Scale this SVG by a specified factor.
     *
     * @param scale Scale factor.
     * @return A new {@link Svg} scaled.
     */
    public Svg scale(double scale) {
        return new TransformedSvg(this, new Scale(this, scale, scale));
    }

    /**
     * Flip this SVG horizontally.
     *
     * @return A new {@link Svg} flipped horizontally.
     */
    public Svg flipHorizontally() {
        return new TransformedSvg(this, new Scale(this, -1, 1));
    }

    /**
     * Flip this SVG vertically.
     *
     * @return A new {@link Svg} flipped vertically.
     */
    public Svg flipVertically() {
        return new TransformedSvg(this, new Scale(this, 1, -1));
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
                String m = Svg.toString(margin, 2);
                return "<g transform=\"translate(" + m + "," + m + ")\">\n" + svg + "\n</g>";
            }
            return switch (side) {
                case Right, Bottom -> svg;
                case Left -> "<g transform=\"translate(" + Svg.toString(margin, 2) + ",0)\">\n" + svg + "\n</g>";
                case Top -> "<g transform=\"translate(0," + Svg.toString(margin, 2) + ")\">\n" + svg + "\n</g>";
            };
        }
    }

    /**
     * Transformer for moving an SVG.
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
         * Move to specified point.
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
            return "<g transform=\"translate(" + Svg.toString(x, 2) + ","
                    + Svg.toString(y, 2) + ")\">\n" + svg + "\n</g>";
        }
    }

    /**
     * Transformer for anchoring an SVG to another one.
     *
     * @author Syam
     */
    public static class Anchor implements Transformer {

        private final Svg anchoredTo;
        private final DoubleSupplier computeX, computeY;

        /**
         * Constructor.
         *
         * @param anchoredTo SVG to anchor to.
         * @param computeX Supplier for X coordinate.
         * @param computeY Supplier for Y coordinate.
         */
        public Anchor(Svg anchoredTo, DoubleSupplier computeX, DoubleSupplier computeY) {
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
        protected final Svg svg;

        /**
         * Constructor.
         *
         * @param svg SVG to scale.
         */
        AbstractScale(Svg svg) {
            this.svg = svg;
        }

        /**
         * Returns X coordinate as string.
         *
         * @return X coordinate.
         */
        protected String x() {
            return Svg.toString(this.svg.x, 2);
        }

        /**
         * Returns Y coordinate as string.
         *
         * @return Y coordinate.
         */
        protected String y() {
            return Svg.toString(this.svg.y, 2);
        }

        /**
         * Returns negative X coordinate as string.
         *
         * @return Negative X coordinate.
         */
        protected String xReverse() {
            return Svg.toString(-this.svg.x, 2);
        }

        /**
         * Returns negative Y coordinate as string.
         *
         * @return Negative Y coordinate.
         */
        protected String yReverse() {
            return Svg.toString(-this.svg.y, 2);
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
         * @param svg SVG to scale.
         * @param toWidth New width.
         * @param toHeight New height.
         */
        public ScaleTo(Svg svg, double toWidth, double toHeight) {
            super(svg);
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
            double scale = toHeight / svg.height;
            return svg.width * scale;
        }

        @Override
        public double transformHeight(double height) {
            if(toWidth <= 0 && toHeight <= 0) {
                return height;
            }
            if(toHeight > 0) {
                return toHeight;
            }
            double scale = toWidth / svg.width;
            return svg.height * scale;
        }

        @Override
        public String transform(String svg) {
            if(toWidth <= 0 && toHeight <= 0) {
                return svg;
            }
            String scaleX = Svg.toString(transformWidth(this.svg.width) / this.svg.width, 2);
            String scaleY = Svg.toString(transformHeight(this.svg.height) / this.svg.height, 2);
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
         * @param svg SVG to scale.
         * @param scaleX X scale factor.
         * @param scaleY Y scale factor.
         */
        public Scale(Svg svg, double scaleX, double scaleY) {
            super(svg);
            this.scaleX = scaleX;
            this.scaleY = scaleY;
        }

        @Override
        public double transformWidth(double width) {
            return width * scaleX;
        }

        @Override
        public double transformHeight(double height) {
            return height * scaleY;
        }

        @Override
        public String transform(String svg) {
            return "<g transform=\"translate(" + x() + "," + y() + ") \"scale(" + Svg.toString(this.scaleX, 2)
                    + "," + Svg.toString(this.scaleY, 2)
                    + ") translate(" + xReverse() + "," + yReverse() + ")\">" + svg + "</g>";
        }
    }
}
