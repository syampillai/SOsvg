package com.storedobject.svg;

/**
 * Represents an SVG text.
 *
 * @author Syam
 */
public class Text extends Element {

    private double x, y;
    private String text;

    /**
     * Constructor.
     *
     * @param p Position.
     * @param text Text.
     */
    public Text(Point p, String text) {
        this(p.x(), p.y(), text);
    }

    /**
     * Constructor.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param text Text.
     */
    public Text(double x, double y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    /**
     * Constructor.
     *
     * @param text Text.
     */
    public Text(String text) {
        this(0, 0, text);
    }

    /**
     * Constructor.
     */
    public Text() {
        this(0, 0, "");
    }

    /**
     * Constructor.
     *
     * @param p Position.
     */
    public Text(Point p) {
        this(p.x(), p.y(), "");
    }

    /**
     * Constructor.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public Text(double x, double y) {
        this(x, y, "");
    }

    /**
     * Get the position.
     *
     * @return Position.
     */
    public Point getAt() {
        return new Point(x, y);
    }

    /**
     * Set the position.
     *
     * @param p Position.
     */
    public void setAt(Point p) {
        setX(p.x());
        setY(p.y());
    }

    /**
     * Set the position.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return Self.
     */
    public Text at(double x, double y) {
        setX(x);
        setY(y);
        return this;
    }

    /**
     * Set the X coordinate.
     *
     * @param x X coordinate.
     */
    public void setX(double x) {
        this.x = x;
        built = false;
    }

    /**
     * Set the Y coordinate.
     *
     * @param y Y coordinate.
     */
    public void setY(double y) {
        this.y = y;
        built = false;
    }

    /**
     * Set the text.
     *
     * @param text Text.
     */
    public void setText(String text) {
        this.text = text;
        built = false;
    }

    /**
     * Get the text.
     *
     * @return Text.
     */
    public String getText() {
        return text;
    }

    /**
     * Create a text.
     *
     * @param p Position.
     * @param text Text.
     * @return Text.
     */
    public Text of(Point p, String text) {
        return new Text(p, text);
    }

    /**
     * Create a text.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param text Text.
     * @return Text.
     */
    public static Text of(double x, double y, String text) {
        return new Text(x, y, text);
    }

    /**
     * Create a text.
     *
     * @param text Text.
     * @return Text.
     */
    public static Text of(String text) {
        return new Text(text);
    }

    /**
     * Set the X coordinate.
     *
     * @param x X coordinate.
     * @return Self.
     */
    public Text x(double x) {
        setX(x);
        return this;
    }

    /**
     * Set the Y coordinate.
     *
     * @param y Y coordinate.
     * @return Self.
     */
    public Text y(double y) {
        setY(y);
        return this;
    }

    /**
     * Set the text.
     *
     * @param text Text.
     * @return Self.
     */
    public Text text(String text) {
        setText(text);
        return this;
    }

    /**
     * Set the position.
     *
     * @param p Position.
     * @return Self.
     */
    public Text at(Point p) {
        setAt(p);
        return this;
    }

    @Override
    public void build() {
        if (built) {
            return;
        }
        if( text == null ) {
            text = "";
        }
        svg = styles("text") + "<text x=\"" + x + "\" y=\"" + y + "\">" + escapeXml(text) + "</text>";
        // Width and height of text are hard to estimate without font metrics.
        width = x + text.length() * 10;
        height = y + 10;
        built = true;
    }
}
