package com.storedobject.svg.chart;

import com.storedobject.common.StringUtility;
import com.storedobject.svg.Element;
import com.storedobject.svg.Styles;
import com.storedobject.svg.Svg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This class represents a collection of data values to be plotted on a chart.
 * It handles labels, values, units, colors, and basic formatting.
 *
 * @author Syam
 */
public class Values {

    private static final String[] colors = new String[] { "#4A90D9", "#50B7A0", "#E59400", "#CC4C6C", "#7B68EE", "#E87C5F", "#5A9E8F", "#f87171" };
    private final List<Value> values = new ArrayList<>();
    private boolean built = false;
    private int sizeWhenBuilt = -1;
    private String unit;
    private Function<Value, String> labelFunction = v -> StringUtility.toString(v.label);
    private Function<Value, String> valueFunction = this::toUnit;
    private String labelName, valueName;
    private String valueNameColor = "#3b82f6";
    private String labelNameColor = "#222";
    private double total = Double.NaN;
    private String defaultValueColor = "#3b82f6";
    private final Map<String, Styles> styles = new HashMap<>();

    /**
     * Constructor.
     */
    public Values() {
    }

    /**
     * Constructor with a unit.
     *
     * @param unit Unit to be used for values.
     */
    public Values(String unit) {
        this.unit = unit;
    }

    /**
     * Set the unit for values.
     *
     * @param unit Unit to be set.
     */
    public void setUnit(String unit) {
        this.unit = unit == null ? "" : Svg.escapeXml(unit);
        built = false;
    }

    /**
     * Get the unit.
     *
     * @return Current unit.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Set the label name (typically used as an X-axis name).
     *
     * @param labelName Label name.
     */
    public void setLabelName(String labelName) {
        this.labelName = labelName == null || labelName.isBlank() ? null : Svg.escapeXml(labelName);
        built = false;
    }

    /**
     * Get the label name.
     *
     * @return Label name.
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * Set the color for the value name.
     *
     * @param valueNameColor Color to set.
     */
    public void setValueNameColor(String valueNameColor) {
        this.valueNameColor = valueNameColor == null || valueNameColor.isBlank() ? null : Svg.escapeXml(valueNameColor);
        built = false;
    }

    /**
     * Get the color for the value name.
     *
     * @return Color.
     */
    public String getValueNameColor() {
        return valueNameColor;
    }

    /**
     * Set the color for the label name.
     *
     * @param labelNameColor Color to set.
     */
    public void setLabelNameColor(String labelNameColor) {
        this.labelNameColor = labelNameColor == null || labelNameColor.isBlank() ? null : Svg.escapeXml(labelNameColor);
        built = false;
    }

    /**
     * Get the color for the label name.
     *
     * @return Color.
     */
    public String getLabelNameColor() {
        return labelNameColor;
    }

    /**
     * Set the value name (typically used as a Y-axis name).
     *
     * @param valueName Value name.
     */
    public void setValueName(String valueName) {
        this.valueName = valueName == null || valueName.isBlank() ? null : Svg.escapeXml(valueName);
        built = false;
    }

    /**
     * Get the value name.
     *
     * @return Value name.
     */
    public String getValueName() {
        return valueName;
    }

    /**
     * Get the formatted value for a given {@link Value}.
     *
     * @param value Value object.
     * @return Formatted value string.
     */
    public String getValue(Value value) {
        return Svg.escapeXml(valueFunction.apply(value));
    }

    /**
     * Get the formatted label for a given {@link Value}.
     *
     * @param value Value object.
     * @return Formatted label string.
     */
    public String getLabel(Value value) {
        return Svg.escapeXml(labelFunction.apply(value));
    }

    /**
     * Set the default color for values.
     *
     * @param defaultValueColor Color to set.
     */
    public void setDefaultValueColor(String defaultValueColor) {
        this.defaultValueColor = defaultValueColor == null || defaultValueColor.isBlank() ? "#3b82f6" : defaultValueColor;
        built = false;
    }

    /**
     * Get the color for a given {@link Value}.
     *
     * @param value Value object.
     * @return Color string.
     */
    public String getColor(Value value) {
        String c = value.color;
        return c == null || c.isBlank() ? defaultValueColor : c;
    }

    /**
     * Set the function to generate labels for {@link Value} objects.
     *
     * @param labelFunction Label function.
     */
    public void setLabelFunction(Function<Value, String> labelFunction) {
        this.labelFunction = labelFunction;
        built = false;
    }

    /**
     * Set the function to generate formatted value strings for {@link Value} objects.
     *
     * @param valueFunction Value function.
     */
    public void setValueFunction(Function<Value, String> valueFunction) {
        this.valueFunction = valueFunction;
        built = false;
    }

    /**
     * Get the total sum of all values.
     *
     * @return Total sum.
     */
    public double getTotal() {
        if(Double.isNaN(total)) {
            total = stream().mapToDouble(v -> v.value).sum();
        }
        return total;
    }

    /**
     * Get the percentage of a given {@link Value} relative to the total.
     *
     * @param value Value object.
     * @return Percentage.
     */
    public double getPercentage(Value value) {
        return value.value / getTotal() * 100;
    }

    /**
     * Get the percentage string for a given {@link Value}.
     *
     * @param value Value object.
     * @return Percentage string (e.g., "50.0%").
     */
    public String getPercentageString(Value value) {
        return toString(getPercentage(value)) + "%";
    }

    /**
     * Add a {@link Value} to the collection.
     *
     * @param value Value object to add.
     */
    public void add(Value value) {
        values.add(nonNull(value));
        total = Double.NaN;
        built = false;
    }

    /**
     * Add a value with a label.
     *
     * @param label Label for the value.
     * @param value Value.
     */
    public void add(Object label, double value) {
        add(new Value(label, value));
    }

    /**
     * Remove a {@link Value} from the collection.
     *
     * @param value Value object to remove.
     */
    public void remove(Value value) {
        values.remove(value);
        total = Double.NaN;
        built = false;
    }

    /**
     * Remove values with a specific label.
     *
     * @param label Label of the values to remove.
     */
    public void remove(Object label) {
        values.removeIf(v -> v.label.equals(label));
        total = Double.NaN;
        built = false;
    }

    /**
     * Clear all values.
     */
    public void clear() {
        values.clear();
        total = Double.NaN;
        built = false;
    }

    /**
     * Get a stream of values.
     *
     * @return Stream of {@link Value} objects.
     */
    public Stream<Value> stream() {
        return values.stream();
    }

    /**
     * Get the list of values.
     *
     * @return List of {@link Value} objects.
     */
    public List<Value> list() {
        return values;
    }

    /**
     * Get the value at a specific index.
     *
     * @param index Index.
     * @return Value at the index.
     */
    public Value get(int index) {
        return values.get(index);
    }

    /**
     * Get the number of values.
     *
     * @return Size.
     */
    public int size() {
        return values.size();
    }

    /**
     * Format a {@link Value} with its unit and specified decimals.
     *
     * @param v Value object.
     * @param decimals Number of decimal places.
     * @return Formatted string.
     */
    public String toUnit(Value v, int decimals) {
        return toString(v.value, decimals, unit);
    }

    /**
     * Format a {@link Value} with its unit.
     *
     * @param v Value object.
     * @return Formatted string.
     */
    public String toUnit(Value v) {
        return toString(v.value, unit);
    }

    /**
     * Format a double value with its unit and specified decimals.
     *
     * @param v Value.
     * @param decimals Number of decimal places.
     * @return Formatted string.
     */
    public String toUnit(double v, int decimals) {
        return toString(v, decimals, unit);
    }

    /**
     * Format a double value with its unit.
     *
     * @param v Value.
     * @return Formatted string.
     */
    public String toUnit(double v) {
        return toString(v, unit);
    }

    /**
     * Convert a double to a string with 2 decimal places.
     *
     * @param v Value.
     * @return Formatted string.
     */
    public static String toString(double v) {
        return Svg.toString(v, 2);
    }

    /**
     * Convert a double to string with unit.
     *
     * @param v Value.
     * @param unit Unit.
     * @return Formatted string.
     */
    public static String toString(double v, String unit) {
        return toString(v, 2, unit);
    }

    /**
     * Convert a double to string with unit and decimal places.
     *
     * @param v Value.
     * @param decimals Decimal places.
     * @param unit Unit.
     * @return Formatted string.
     */
    public static String toString(double v, int decimals, String unit) {
        String s = Svg.toString(v, decimals);
        if(unit != null) {
            s += unit;
        }
        return s;
    }

    /**
     * Check if the values are built.
     *
     * @return True if built.
     */
    public boolean isBuilt() {
        return built && sizeWhenBuilt == values.size();
    }

    /**
     * Mark as built.
     */
    void build() {
        built = true;
        sizeWhenBuilt = values.size();
    }

    /**
     * Mark as isn't built.
     */
    void unbuild() {
        built = false;
    }

    /**
     * Automatically assign colors to values that don't have one.
     */
    public void colorize() {
        int n = values.size();
        Value v;
        while(n > 0) {
            v = values.removeFirst();
            if(v.color == null) {
                v = new Value(v.label, v.value, colors[n % colors.length]);
            }
            values.add(v);
            --n;
        }
    }

    private boolean isNull(Object o) {
        return o == null || StringUtility.toString(o).isBlank();
    }

    private Object nonNull(Object o) {
        return isNull(o) ? ("X" + (values.size() + 1)) : o;
    }

    private Object nonNull() {
        return nonNull((Object) null);
    }

    private Value nonNull(Value v) {
        return v == null ? new Value(nonNull(), 0, null)
                : (isNull(v.label) ? new Value(nonNull(v.label), v.value, v.color) : v);
    }

    /**
     * Sets the styles for the specified {@link Value}.
     * If the provided {@link Value} is null, the method returns without making any changes.
     *
     * @param value The {@link Value} for which styles need to be set.
     * @param styles The {@link Styles} to associate with the given {@link Value}.
     */
    public void setStyle(Value value, Styles styles) {
        if(value == null) {
            return;
        }
        this.styles.put(value.id, styles);
    }

    /**
     * Retrieves the styles associated with the specified {@link Value}.
     * If the provided {@link Value} is null, this method returns null.
     *
     * @param value The {@link Value} for which to retrieve the associated styles.
     * @return The {@link Styles} associated with the specified {@link Value},
     *         or null if the {@link Value} is null or has no associated styles.
     */
    public Styles getStyle(Value value) {
        return value == null ? null : styles.get(value.id);
    }

    /**
     * Constructs a string representation of all styles by iterating over the existing style definitions.
     * Each style is built with its associated identifier and appended to the result, separated by new lines.
     * If no styles are present, returns an empty string.
     *
     * @return A string containing all constructed styles, or an empty string if there are no styles.
     */
    public String buildStyles() {
        if(styles.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        styles.forEach((id, styles) -> sb.append(styles.build(id)).append("\n"));
        return sb.toString();
    }

    /**
     * Represents a single data point in the chart.
     *
     * @param label Label for the data point.
     * @param value Numerical value.
     * @param color Optional color for the data point.
     * @param id Whatever value passed is ignored and a unique ID is generated.
     *
     * @author Syam
     */
    public record Value(Object label, double value, String color, String id) {

        /**
         * Constructor.
         *
         * @param label Label.
         * @param value Value.
         * @param color Color.
         * @param id Whatever value passed is ignored and a unique ID is generated.
         */
        public Value(Object label, double value, String color, @SuppressWarnings("unused") String id) {
            this.label = label;
            this.value = value;
            this.color = color;
            this.id = Element.ID();
        }

        /**
         * Constructor.
         *
         * @param label Label.
         * @param value Value.
         * @param color Color.
         */
        public Value(Object label, double value, String color) {
            this(label, value, color, null);
        }

        /**
         * Constructor.
         *
         * @param label Label.
         * @param value Value.
         */
        public Value(Object label, double value) {
            this(label, value, null, null);
        }
    }
}
