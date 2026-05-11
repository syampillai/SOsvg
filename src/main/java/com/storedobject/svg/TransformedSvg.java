package com.storedobject.svg;

/**
 * An SVG that is transformed using a {@link Transformer}.
 *
 * @author Syam
 */
public class TransformedSvg extends Svg {

    /**
     * The embedded SVG.
     */
    final Svg embedded;

    private Transformer transformer;

    /**
     * Constructor.
     *
     * @param embedded The SVG to transform.
     */
    public TransformedSvg(Svg embedded) {
        this(embedded, null);
    }

    /**
     * Constructor.
     *
     * @param embedded The SVG to transform.
     * @param transformer The transformer to use.
     */
    public TransformedSvg(Svg embedded, Transformer transformer) {
        this.embedded = embedded;
        this.transformer = transformer;
    }

    /**
     * Set the transformer.
     *
     * @param transformer Transformer.
     * @return Self.
     */
    public TransformedSvg transform(Transformer transformer) {
        this.transformer = transformer;
        return this;
    }

    @Override
    public final void build() {
        if(!embedded.isBuilt()) {
            embedded.build();
        }
        if(transformer == null) {
            this.svg = embedded.svg;
            return;
        }
        this.width = transformer.transformWidth(embedded.getWidth());
        this.height = transformer.transformHeight(embedded.getHeight());
        this.x = transformer.transformX(embedded.getX());
        this.y = transformer.transformY(embedded.getY());
        if(this.width <= 0 || this.height <= 0) {
            this.width = 0.1;
            this.height = 0.1;
            this.svg = "<g></g>\n";
        } else {
            this.svg = transformer.transform(embedded.svg);
        }
    }

    @Override
    public final boolean isBuilt() {
        return false; // We will always build because we don't know the changes in the embedded SVG
    }
}
