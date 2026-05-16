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
     * Constructs a CSS style block for an element represented by a given ID.
     * The method appends the CSS properties and their respective values stored in the current instance
     * to the provided StringBuilder object, using the specified ID to define the block selector.
     *
     * @param sb the StringBuilder to which the CSS style block will be appended
     * @param id the identifier of the CSS element for which the style block will be built
     */
    public void build(StringBuilder sb, String id) {
        sb.append("#").append(id).append(" {\n");
        String key;
        for(Map.Entry<String, String> e: entrySet()) {
            key = e.getKey();
            if("id".equals(key)) { // Skip the id property
                continue;
            }
            sb.append(Node.escapeXml(key)).append(": ").append(Node.escapeXml(e.getValue())).append(";\n");
        }
        sb.append("}\n");
    }
}
