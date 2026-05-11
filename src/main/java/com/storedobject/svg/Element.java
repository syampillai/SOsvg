package com.storedobject.svg;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an SVG element.
 *
 * @author Syam
 */
public abstract class Element extends Svg {

    /**
     * Constructs a new instance of the Element class.
     * This serves as the base constructor for all SVG elements,
     * initializing the object without setting specific styles or properties.
     */
    public Element() {
    }

    /**
     * Styles for this element.
     */
    protected final Map<String, String> styles = new HashMap<>();

    /**
     * Flag to indicate if the element is built.
     */
    protected boolean built;

    @Override
    public final boolean isBuilt() {
        return built;
    }

    /**
     * Add a style to this element.
     *
     * @param name Name of the style.
     * @param value Value of the style.
     */
    public void addStyle(String name, String value) {
        styles.put(name, value);
    }

    /**
     * Remove a style from this element.
     *
     * @param name Name of the style to remove.
     */
    public void removeStyle(String name) {
        styles.remove(name);
    }

    /**
     * Generate the style string for the given tag.
     *
     * @param tag Tag name.
     * @return Style string.
     */
    protected String styles(String tag) {
        if(styles.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("<defs><style>");
        sb.append(tag).append(" {\n");
        for(Map.Entry<String, String> e: styles.entrySet()) {
            sb.append(escapeXml(e.getKey())).append(": ").append(escapeXml(e.getValue())).append(";\n");
        }
        sb.append("}\n</style></defs>");
        return sb.toString();
    }
}
