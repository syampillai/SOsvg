package com.storedobject.svg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A canvas for drawing SVGs. It can contain multiple {@link Node} objects.
 *
 * @author Syam
 */
public final class Canvas extends Node {

    private final AtomicInteger ID = new AtomicInteger();
    private final int id = ID.getAndIncrement();
    private final List<Element> clips = new ArrayList<>();
    private final List<MovableNode> svgs = new ArrayList<>();
    private boolean autoSize = false;

    /**
     * Constructor.
     */
    public Canvas() {
        this(0, 0);
        autoSize = true;
    }

    /**
     * Constructor.
     *
     * @param width Width.
     * @param height Height.
     */
    public Canvas(double width, double height) {
        size(width, height);
    }

    /**
     * Add a clip element.
     *
     * @param clip Clip element.
     * @return Self.
     */
    public Canvas clip(Element clip) {
        clips.add(clip);
        return this;
    }

    /**
     * Remove a clip element.
     *
     * @param clip Clip element.
     */
    public void removeClip(Element clip) {
        clips.remove(clip);
    }

    /**
     * Set the size.
     *
     * @param width Width.
     * @param height Height.
     * @return Self.
     */
    public Canvas size(double width, double height) {
        this.width = width <= 0 ? 0.1 : width;
        this.height = height <= 0 ? 0.1 : height;
        if(!clips.isEmpty()) {
            clips.removeFirst();
        }
        clips.addFirst(new Rectangle(0, 0, this.width, this.height));
        return this;
    }

    /**
     * Set the auto-size flag.
     *
     * @param autoSize Auto-size flag.
     * @return Self.
     */
    public Canvas autoSize(boolean autoSize) {
        this.autoSize = autoSize;
        return this;
    }

    /**
     * Set the auto-size flag to true.
     *
     * @return Self.
     */
    public Canvas autoSize() {
        return autoSize(true);
    }

    /**
     * Add an SVG.
     *
     * @param node SVG to add.
     * @return The {@link MovableNode} that wraps the added SVG.
     */
    public MovableNode add(Node node) {
        MovableNode m = MovableNode.create(node);
        svgs.add(m);
        return m;
    }

    /**
     * Remove an SVG.
     *
     * @param node SVG to remove.
     * @return The removed SVG (could be a {@link MovableNode}).
     */
    public Node remove(Node node) {
        if(node instanceof MovableNode ms) {
            if(svgs.remove(ms)) {
                return ms;
            }
            node = ms.embedded;
        }
        for(MovableNode ms: svgs) {
            if(ms.embedded == node) {
                svgs.remove(ms);
                return node;
            }
        }
        return null;
    }

    @Override
    public void build() {
        svgs.forEach(MovableNode::build);
        if(autoSize) {
            double w = 0.1, h = 0.1;
            for (MovableNode svg : svgs) {
                w = Math.max(w, svg.width);
                h = Math.max(h, svg.height);
            }
            size(w, h);
        }
        StringBuilder s = new StringBuilder("<defs><clipPath id=\"so-clip-").append(id).append("\">\n");
        for(Element clip: clips) {
            if(!clip.isBuilt()) {
                clip.build();
            }
            s.append(clip.svg).append("\n");
        }
        s.append("</clipPath></defs>\n<g clip-path=\"url(#so-clip-").append(id).append(")\">\n");
        for(MovableNode svg: svgs) {
            s.append(svg.svg).append("\n");
        }
        s.append("</g>\n");
        this.svg = s.toString();
    }

    @Override
    public boolean isBuilt() {
        return false; // We will always build because we don't know the changes in the embedded SVGs
    }
}
