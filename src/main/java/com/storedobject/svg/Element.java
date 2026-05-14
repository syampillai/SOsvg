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
        String s = tag + " id=\"" + id + "\" cursor=\"pointer\" ";
        if(styles.isEmpty()) {
            return "<" + s;
        }
        StringBuilder sb = new StringBuilder("<g><defs><style>");
        styles.build(sb, id);
        sb.append("</style></defs>\n").append(s);
        return sb.toString();
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
     * Generates a unique identifier string for an SVG element by utilizing the current ID value.
     * This method internally invokes {@code IDValue()} to retrieve a unique numerical value and
     * combines it with a predefined prefix using {@code ID(long id)}.
     *
     * @return A unique string identifier constructed for the SVG element in the format "so-svg-id-{id}".
     */
    public static String ID() {
        return ID(IDValue());
    }

    /**
     * Generates a unique numerical value to be used as an identifier.
     * The ID value is maintained as a thread-safe atomic counter, ensuring
     * uniqueness across multiple invocations. When the maximum value for
     * a 64-bit signed integer is reached, the counter resets to zero.
     *
     * @return A unique {@code long} value incremented from the atomic counter.
     */
    public static long IDValue() {
        if(ID.get() == Long.MAX_VALUE) {
            ID.set(0);
        }
        return ID.incrementAndGet();
    }

    /**
     * Generates a unique string identifier for an SVG element based on the provided numerical ID.
     * The resulting identifier is prefixed with "so-svg-id-", followed by the specified ID value.
     *
     * @param id The numerical ID used to generate the unique identifier.
     * @return A string representing the unique SVG element identifier in the format "so-svg-id-{id}".
     */
    public static String ID(long id) {
        return "so-svg-id-" + id;
    }
}
