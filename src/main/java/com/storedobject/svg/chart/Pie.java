package com.storedobject.svg.chart;

import com.storedobject.svg.Point;

/**
 * Pie Chart.
 *
 * @author Syam
 */
public class Pie extends Chart {

    private static final String start = """
                  <defs>
                    <style>
                      text {
                        font-family: Arial, Helvetica, sans-serif;
                        font-weight: bold;
                        font-size: 13px;
                        fill: #333;
                      }
                      ${V-STYLES}
                    </style>
                  </defs>
                """;
    private double donutHole = 80;
    private boolean showLegend = true;
    private double margin = 5;

    /**
     * Constructor.
     */
    public Pie() {
        this(0);
    }

    /**
     * Constructor with radius.
     *
     * @param radius Radius of the chart.
     */
    public Pie(double radius) {
        this(new Values(), radius);
        setUnit("%");
    }

    /**
     * Constructor with values and radius.
     *
     * @param values Values to be plotted.
     * @param radius Radius of the chart.
     */
    public Pie(Values values, double radius) {
        super(values);
        setRadius(radius);
    }

    /**
     * Constructor with values.
     *
     * @param values Values to be plotted.
     */
    public Pie(Values values) {
        this(values, 0);
    }
    /**
     * Set the radius of the pie.
     *
     * @param radius Radius in pixels.
     */
    public void setRadius(double radius) {
        if(radius <= 0.1) {
            radius = 180 - margin;
        }
        height = width = (radius + margin) * 2;
        values.unbuild();
    }

    /**
     * Set the radius of the donut hole. If set, the chart becomes a donut chart.
     *
     * @param donutHoleRadius Radius of the donut hole in pixels.
     */
    public void setDonutHoleRadius(double donutHoleRadius) {
        this.donutHole = donutHoleRadius * 2;
        values.unbuild();
    }

    /**
     * Check if the chart is a donut chart.
     *
     * @return True if donut chart.
     */
    public final boolean isDonut() {
        return donutHole > 0 && donutHole < (width - margin - margin - 20);
    }

    private double radius() {
        return (width / 2.0) - margin;
    }

    /**
     * Set whether to show the legend.
     *
     * @param showLegend True to show the legend.
     */
    public void setShowLegend(boolean showLegend) {
        this.showLegend = showLegend;
        values.unbuild();
    }

    /**
     * Set the margin.
     *
     * @param margin Margin.
     */
    public void setMargin(double margin) {
        this.margin = margin <= 0 ? 0 : margin;
    }

    /**
     * Get the margin.
     *
     * @return Margin.
     */
    public double getMargin() {
        return margin;
    }

    /**
     * Build the pie chart SVG.
     */
    @Override
    public void build() {
        if(values.isBuilt()) {
            return;
        }
        if("%".equals(values.getUnit()) && !"100".equals(toString(values.getTotal(), 0))) {
            values.setUnit("");
        }
        colorize();
        values.build();

        StringBuilder svg = new StringBuilder(start.replace("${V-STYLES}", values.buildStyles(this)));

        boolean isDonut = isDonut();
        String radius = Values.toString(radius());
        String donutHoleRadius = Values.toString(this.donutHole);
        String centerX = Values.toString(width / 2.0);
        String centerY = Values.toString(height / 2.0);

        // Draw pie slices
        double currentAngle = -90; // Start from top (12 o'clock)
        for (Values.Value slice : values.list()) {
            double angleSweep = values.getPercentage(slice) * 3.6; // 360° * (percentage / 100)
            double endAngle = currentAngle + angleSweep;

            // Calculate coordinates for the arc
            Point start = pointOnCircle(currentAngle);
            Point end = pointOnCircle(endAngle);

            // Determine if arc is more than 180 degrees
            boolean largeArc = angleSweep > 180;

            // Build path for the slice
            String pathData;
            if (isDonut) {
                // Donut slice (arc with inner radius)
                Point innerStart = pointOnCircle(currentAngle, this.donutHole);
                Point innerEnd = pointOnCircle(endAngle, this.donutHole);

                pathData = String.format(
                        "M %s,%s L %s,%s A %s,%s 0 %s 1 %s,%s L %s,%s A %s,%s 0 %s 0 %s,%s Z",
                        start.sx(), start.sy(),
                        start.sx(), start.sy(),
                        radius, radius, largeArc ? "1" : "0",
                        end.sx(), end.sy(),
                        innerEnd.sx(), innerEnd.sy(),
                        donutHoleRadius, donutHoleRadius, largeArc ? "1" : "0",
                        innerStart.sx(), innerStart.sy()
                );
            } else {
                // Standard pie slice
                pathData = String.format(
                        "M %s,%s L %s,%s A %s,%s 0 %s 1 %s,%s Z",
                        centerX, centerY,
                        start.sx(), start.sy(),
                        radius, radius, largeArc ? "1" : "0",
                        end.sx(), end.sy()
                );
            }

            svg.append(String.format("<path id=\"%s\" d=\"%s\" fill=\"%s\" stroke=\"white\" stroke-width=\"2\"><title>%s</title></path>\n",
                    slice.id(this), pathData, values.getColor(slice), valueS(slice)));

            currentAngle = endAngle;
        }

        // Donut center circle
        if (isDonut) {
            svg.append(String.format(
                    "<circle cx=\"%s\" cy=\"%s\" r=\"%s\" fill=\"none\" stroke=\"#e5e7eb\" stroke-width=\"2\"/>\n",
                    centerX, centerY, donutHoleRadius));

            // Center text
            svg.append(String.format(
                    "<text x=\"%s\" y=\"%s\" text-anchor=\"middle\" fill=\"#374151\">Total</text>\n",
                    centerX, Values.toString((height / 2.0) - 5)));

            svg.append(String.format(
                    "<text x=\"%s\" y=\"%s\" text-anchor=\"middle\" fill=\"#1f2937\">%s</text>\n",
                    centerX, Values.toString((height / 2.0) + 15), values.toUnit(values.getTotal())));
        }

        // Legend
        svg.append(generateLegend());

        this.svg = svg.toString();
    }

    /**
     * Calculates a point on the circle at a given angle.
     */
    private Point pointOnCircle(double angleDegrees) {
        return pointOnCircle(angleDegrees, radius());
    }

    private Point pointOnCircle(double angleDegrees, double r) {
        double angleRad = Math.toRadians(angleDegrees);
        double centerX = width / 2.0;
        double centerY = height / 2.0;
        double x = centerX + r * Math.cos(angleRad);
        double y = centerY + r * Math.sin(angleRad);
        return new Point(x, y);
    }

    /**
     * Generates the legend SVG.
     */
    private String generateLegend() {
        if (!showLegend) {
            return "";
        }
        StringBuilder legend = new StringBuilder();
        double legendX = margin + 15;
        double legendStartY = height - (values.size() * 24) - 20;
        legend.append(String.format("<g transform=\"translate(%s, %s)\">\n", toString(legendX, 2),
                toString(legendStartY, 2)));
        int y = 0;
        for (Values.Value slice : values.list()) {
            legend.append(String.format("<rect x=\"0\" y=\"%d\" width=\"16\" height=\"16\" rx=\"3\" fill=\"%s\"/>\n",
                    y, values.getColor(slice)));
            legend.append(String.format("<text x=\"24\" y=\"%d\" fill=\"#374151\">%s</text>\n",
                    y + 13, valueS(slice)));
            y += 24;
        }
        legend.append("</g>\n");
        return legend.toString();
    }

    private String valueS(Values.Value slice) {
        String s = values.getLabel(slice) + " (" + values.getValue(slice);
        if(s.endsWith("%")) {
            return s + ")";
        }
        return s + " " + values.getPercentageString(slice) + ")";
    }
}