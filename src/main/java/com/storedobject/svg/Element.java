package com.storedobject.svg;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents an SVG element. Unlike the {@link Svg} class, this class is more specific to individual SVG elements
 * and does not have the concept combining tage. It is used to represent standalone SVG tags like
 * <code>rect</code>,<code>circle</code>, etc. However, each element can have its own styles. It has a unique identifier
 * too.
 *
 * @author Syam
 */
public abstract class Element extends Svg {

    private static final AtomicLong ID = new AtomicLong();

    /**
     * The SVG tag associated with this element.
     */
    protected final String tag;

    /**
     * Unique identifier for this element.
     */
    protected final String id = ID();

    /**
     * Styles for this element.
     */
    protected final Styles styles = new Styles();

    /**
     * Flag to indicate if the element is built.
     */
    protected boolean built;

    /**
     * Constructs a new instance of the Element class.
     * This serves as the base constructor for all SVG elements,
     * initializing the object without setting specific styles or properties.
     *
     * @param tag The SVG tag associated with this element.
     */
    public Element(String tag) {
        this.tag = tag;
    }

    /**
     * Retrieves the SVG tag associated with this element.
     * The tag is a string representation of the specific SVG element type
     * (e.g., "rect", "circle", etc.) and is determined during the initialization
     * of the element.
     *
     * @return The string representing the SVG tag of this element.
     */
    public final String getTag() {
        return tag;
    }

    @Override
    public final boolean isBuilt() {
        return built;
    }

    /**
     * Retrieves the styles associated with this SVG element.
     *
     * @return An instance of {@code Styles} containing the style properties
     *         for this element in the form of key-value pairs.
     */
    public Styles getStyles() {
        return styles;
    }

    /**
     * Generate the starting part of the style string for the given tag.
     *
     * @return Starting part of the style string.
     */
    protected String styleStart() {
        if(styles.isEmpty()) {
            return "";
        }
        return "<g><defs><style>" + styles.build(id) + "</style></defs>\n" + tag + " id\"" + id + "\" ";
    }

    /**
     * Generates the closing tag for a styled group element if styles are present.
     *
     * @return A string containing the closing tag if styles are defined for the element;
     *         otherwise, an empty string.
     */
    protected String styleEnd() {
        return styles.isEmpty() ? "" : "</g>";
    }

    /**
     * Generates a unique identifier for SVG elements.
     * The identifier is based on an internal counter and follows
     * the format "so-svg-id-" prefixed to the incremented counter-value.
     *
     * @return A unique string identifier for an SVG element.
     */
    public static String ID() {
        return "so-svg-id-" + ID.incrementAndGet();
    }
}
