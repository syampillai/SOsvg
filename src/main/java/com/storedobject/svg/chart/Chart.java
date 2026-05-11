package com.storedobject.svg.chart;

import com.storedobject.svg.Svg;

/**
 * Base class for all SVG charts.
 *
 * @author Syam
 */
public abstract class Chart extends Svg {

    /**
     * Data values of the chart.
     */
    protected final Values values;

    /**
     * Constructor.
     *
     * @param values The data values to be plotted.
     */
    public Chart(Values values) {
        this.values = values;
    }

    /**
     * Get the data values.
     *
     * @return The {@link Values} object.
     */
    public Values getValues() {
        return values;
    }

    /**
     * Add a data value.
     *
     * @param value The value to add.
     */
    public void addValue(Values.Value value) {
        values.add(value);
    }

    /**
     * Add a data value with a label.
     *
     * @param label The label for the value.
     * @param value The value.
     */
    public void addValue(Object label, double value) {
        values.add(label, value);
    }

    /**
     * Set the label name (typically used as X-axis name).
     *
     * @param labelName Label name.
     */
    public void setLabelName(String labelName) {
        values.setLabelName(labelName);
    }

    /**
     * Set the value name (typically used as Y-axis name).
     *
     * @param valueName Value name.
     */
    public void setValueName(String valueName) {
        values.setValueName(valueName);
    }

    /**
     * Set the unit for values.
     *
     * @param unit Unit to be set.
     */
    public void setUnit(String unit) {
        values.setUnit(unit);
    }

    /**
     * Automatically assign colors to data points.
     */
    public void colorize() {
        values.colorize();
    }

    /**
     * Build the chart. This method should be called before getting the SVG output.
     * It ensures the underlying {@link Values} are also built.
     */
    public void build() {
        if (!values.isBuilt()) {
            values.build();
        }
    }

    /**
     * Check if the chart is built.
     *
     * @return True if built.
     */
    public final boolean isBuilt() {
        return values == null || values.isBuilt();
    }
}
