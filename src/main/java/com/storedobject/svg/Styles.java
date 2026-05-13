package com.storedobject.svg;

import java.util.HashMap;
import java.util.Map;

/**
 * The Styles class extends HashMap and provides functionality for constructing
 * CSS style definitions. Each entry in the map represents a CSS property and its value.
 *
 * @author Syam
 */
public class Styles extends HashMap<String, String> {

    /**
     * Creates a new instance of the Styles class, which extends HashMap. This constructor
     * initializes an empty map that will hold CSS property-value pairs for style definitions.
     */
    public Styles() {
    }

    /**
     * Constructs a CSS style definition block for the specified ID based on the current
     * map entries, where each entry represents a CSS property and its value.
     *
     * @param id the identifier for the CSS block (prepended with a hash '#' symbol)
     * @return a string representing the CSS block with escaped property names and values
     */
    public String build(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(id).append(" {\n");
        for(Map.Entry<String, String> e: entrySet()) {
            sb.append(Svg.escapeXml(e.getKey())).append(": ").append(Svg.escapeXml(e.getValue())).append(";\n");
        }
        sb.append("}\n");
        return sb.toString();
    }
}
