# SOsvg

A simple, easy to use and flexible SVG manipulation library with some basic charting capabilities.

## Features

- **Basic SVG Elements**: Support for various SVG shapes including:
    - `Circle`
    - `Ellipse`
    - `Rectangle`
    - `Square`
    - `Line`
    - `Polyline`
    - `Polygon`
    - `Star`
    - `Text`
- **Charting Capabilities**: Easy-to-use chart types:
    - `Bars` (Bar Chart)
    - `Pie` (Pie Chart)
    - `LinePlot` (Line Chart)
    - `DayPlot`, `MonthPlot`, `YearPlot` (Temporal Plots)
- **Canvas System**: A `Canvas` class to aggregate and manage multiple SVG elements and charts with clipping support.
- **Fluent API**: Many classes support a fluent interface for easy object creation and configuration.
- **Auto-sizing**: Canvas can automatically calculate its size based on its content.

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.storedobject</groupId>
    <artifactId>so-node</artifactId>
    <version>0.0.2</version>
</dependency>
```

*Note: This library doesn't depend on any other libraries!*

## Usage Examples

### Creating a Simple Canvas with a Star

```java
import com.storedobject.node.Canvas;
import com.storedobject.node.Star;

Canvas canvas = new Canvas(200, 200);
canvas.add(Star.of(100, 100, 50, 100, 8));
String svgOutput = canvas.getSvg();
```

### Creating a Bar Chart

```java
import com.storedobject.node.chart.Bars;

Bars bars = new Bars();
bars.setLabelName("Categories");
bars.setValueName("Value");
bars.setUnit("%");
bars.addValue("Category A", 10);
bars.addValue("Category B", 90);
bars.addValue("Category C", 45);

String chartSvg = bars.getSvg();
```

### Creating a Pie Chart

```java
import com.storedobject.node.chart.Pie;

Pie pie = new Pie();
pie.addValue("A", 15);
pie.addValue("B", 25);
pie.addValue("C", 60);

String pieSvg = pie.getSvg();
```

### Using Canvas with Charts

```java
import com.storedobject.node.Canvas;
import com.storedobject.node.chart.DayPlot;

Canvas canvas = new Canvas(600, 400);
DayPlot plot = new DayPlot(new double[] { 10, 20, 30, 25, 50 }, "Title", 20, 20, 5);
canvas.add(plot);
String finalSvg = canvas.getSvg();
```

## License

This project is licensed under the Apache License, Version 2.0. See the `pom.xml` or [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt) for more details.

## Author

**Syam Pillai** - [syampillai](https://github.com/syampillai)
