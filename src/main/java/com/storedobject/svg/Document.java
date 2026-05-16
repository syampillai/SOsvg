package com.storedobject.svg;

import java.util.HashSet;
import java.util.Objects;

/**
 * The Document class represents a collection of {@link Node} objects and provides functionality
 * to generate scalable vector graphic (SVG) output. It allows the
 * storage and management of {@link Node} instances while adding SVG-specific methods to generate
 * graphical representations of the contained nodes.
 * <p>Note: When a node is added and if it has a parent, its root-parent is considered when the SVG is
 * generated finally.</p>
 *
 * @author Syam
 */
public class Document extends HashSet<Node> {

    /**
     * Default constructor for the Document class.
     * Initializes a new instance of the Document without any content or nodes.
     */
    public Document() {}

    /**
     * Constructs a new Document object, initializing it with a single Node.
     *
     * @param node The Node to be added to the Document during its creation.
     */
    public Document(Node node) {
        add(node);
    }

    /**
     * Cleans up the current set by performing the following tasks:
     * 1. Removes all null entries from the set.
     * 2. Identifies all root nodes (nodes without a parent).
     * 3. Removes all nodes that have a parent from the set.
     * 4. Adds the root parent of each identified root node back to the set.
     * <p></p>
     * This method ensures that the set retains only root-level nodes and eliminates unnecessary or redundant
     * child nodes during the cleanup process.
     * <p>Note: You may call this method before doing generic {@link java.util.Set} operations. Otherwise, methods
     * such as {@link #size()}, {@link #isEmpty()}, {@link #stream()}, etc. may return incorrect results.</p>
     */
    public void cleanUp() {
        removeIf(Objects::isNull);
        HashSet<Node> nodes = new HashSet<>();
        for (Node node : this) {
            if (node.parent != null) {
                continue;
            }
            nodes.add(node);
        }
        removeIf(n -> n.parent != null);
        nodes.forEach(n -> this.add(n.getRootParent()));
    }

    private String svg(String width, String height) {
        cleanUp();
        double x1, y1, w, h;
        String s;
        if(isEmpty()) {
            s = "<g></g>";
            x1 = y1 = 0;
            w = h = 0.1;
        } else {
            StringBuilder sb = new StringBuilder();
            x1 = Double.POSITIVE_INFINITY;
            y1 = Double.POSITIVE_INFINITY;
            double x2 = Double.NEGATIVE_INFINITY, y2 = Double.NEGATIVE_INFINITY;
            for (Node node : this) {
                if (!node.isBuilt()) {
                    node.build();
                }
                if(sb.isEmpty()) {
                    sb.append("\n");
                }
                sb.append(node.svg);
                x1 = Math.min(x1, node.x);
                y1 = Math.min(y1, node.y);
                x2 = Math.max(x2, node.x + node.width);
                y2 = Math.max(y2, node.y + node.height);
            }
            s = sb.toString();
            w = x2 - x1;
            h = y2 - y1;
        }
        if(x1 < 0 || y1 < 0) {
            s = "<g transform=\"translate(" + Node.toString(Math.min(0, -x1), 2)
                    + ", " + Node.toString(Math.min(0, -y1), 2) + ")\">\n" + s + "\n</g>";
        }
        if(width == null) {
            width = Node.toString(w, 2);
        }
        if(height == null) {
            height = Node.toString(h, 2);
        }
        return "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 "
                + Node.toString(w, 2) + " " + Node.toString(h, 2)
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
     * @param scaleWidthPercentage Scale percentage for width (0 to 100). A value of 0 means "auto" width to keep the aspect ratio.
     * @param scaleHeightPercentage Scale percentage for height (0 to 100). A value of 0 means "auto" height to keep the aspect ratio.
     * @return SVG string.
     */
    public final String getScaledSvg(double scaleWidthPercentage, double scaleHeightPercentage) {
        return svg(scaleWidthPercentage <= 0 || scaleWidthPercentage > 100 ? "auto"
                        : Node.toString(scaleWidthPercentage, 4) + "%",
                scaleHeightPercentage <= 0 || scaleHeightPercentage > 100 ? "auto"
                        : Node.toString(scaleHeightPercentage, 4) + "%");
    }

    @Override
    public String toString() {
        return getSvg();
    }
}
