package eu.straider.web.gwt.gauges.client.components;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import eu.straider.web.gwt.gauges.client.ColorRange;

public class SimpleFillArcGauge<T extends Number> extends AbstractGauge<T> {

    private int width;
    private int height;
    private double gaugeCenterX;
    private double gaugeCenterY;
    private double radius;

    public SimpleFillArcGauge(T minValue, T maxValue, T value) {
        this(3, minValue, maxValue, value);
    }

    public SimpleFillArcGauge(double borderWidth, T minValue, T maxValue, T value) {
        super();
        width = 0;
        height = 0;
        radius = 0;
        gaugeCenterX = 0;
        gaugeCenterY = 0;
        setBorderEnabled(true);
        setBorderWidth(borderWidth);
        setFont("bold 32px Lucida Console");
        setMinValue(minValue);
        setMaxValue(maxValue);
        setAnimationEnabled(true);
        setValue(value, false);
    }

    @Override
    public void setSize(int size) {
        getCanvas().setHeight(size / 2 + "px");
        getCanvas().setWidth(size + "px");

        getCanvas().setCoordinateSpaceHeight(size / 2);
        getCanvas().setCoordinateSpaceWidth(size);
        width = size;
        height = size / 2;
        radius = width / 2 - (getBorderWidth() * 4);
        gaugeCenterX = width / 2;
        gaugeCenterY = width / 2 - (getBorderWidth() * 2);
    }

    @Override
    void drawGaugeDial(double currentValue) {
        setArcData(currentValue);
        double degrees = getDegreesForValue(currentValue);

        getContext().beginPath();
        getContext().arc(gaugeCenterX, gaugeCenterY, radius, Math.toRadians(180), Math.toRadians(degrees));
        getContext().lineTo(gaugeCenterX, gaugeCenterY);
        getContext().closePath();
        getContext().fill();
    }

    private CssColor getCurrentGaugeColor(double arcValue) {
        CssColor color = null;
        for (ColorRange range : getColorRanges()) {
            if (arcValue >= range.getMin().doubleValue() && arcValue <= range.getMax().doubleValue()) {
                color = range.getColor();
            }
        }
        if (color == null) {
            color = getGaugeColor();
        }
        return color;
    }

    @Override
    void drawGaugeText(double currentValue) {
        getContext().setFillStyle(getTextColor());
        getContext().setFont(getFont());
        getContext().setTextAlign(Context2d.TextAlign.CENTER);
        getContext().fillText(getValueFormat().format(currentValue), getCanvas().getCanvasElement().getWidth() / 2, getCanvas().getCanvasElement().getHeight() / 1.2);
    }

    @Override
    void drawGaugeBorder(double currentValue) {
        getContext().beginPath();
        getContext().arc(gaugeCenterX, gaugeCenterY, radius, Math.toRadians(180), Math.toRadians(0));
        getContext().closePath();
        getContext().stroke();
    }

    @Override
    void drawGaugeBackground(double currentValue) {
        getContext().setFillStyle(getBackgroundColor());
        getContext().beginPath();
        getContext().arc(gaugeCenterX, gaugeCenterY, radius, Math.toRadians(180), Math.toRadians(0));
        getContext().closePath();
        getContext().fill();
    }

    @Override
    void drawGaugeTicks(double currentValue) {
        double majorTickLength = width / 100 * getMajorTicksSizeInPercentOfSize();
        double minorTickLength = width / 100 * getMinorTicksSizeInPercentOfSize();
        double maxVal = (getMaxValue().doubleValue() - getMinValue().doubleValue());
        double tickSizeMajor = maxVal / (getMajorTicks() - 1);
        double tickSizeMinor = tickSizeMajor / (getMinorTicks() + 1);
        double majorVal = getMinValue().doubleValue();
        for (int i = 0; i <= getMajorTicks(); i++) {
            double minorVal = majorVal;
            for (int j = 0; j < getMinorTicks(); j++) {
                minorVal += tickSizeMinor;
                drawTick(minorVal, radius - minorTickLength);
            }
            drawTick(majorVal, radius - majorTickLength);
            majorVal += tickSizeMajor;
        }
    }

    private void drawTick(double value, double tickRadius) {
        getContext().beginPath();
        final double radiansVal = Math.toRadians(getDegreesForValue(value));
        double x = (Math.cos(radiansVal) * (tickRadius));
        double y = (Math.sin(radiansVal) * (tickRadius));
        getContext().moveTo(gaugeCenterX + x, gaugeCenterY + y);
        x = (Math.cos(radiansVal) * radius);
        y = (Math.sin(radiansVal) * radius);
        getContext().lineTo(gaugeCenterX + x, gaugeCenterY + y);
        getContext().closePath();
        getContext().stroke();
    }

    @Override
    protected void drawGauge(double currentValue) {
        if (isVisible()) {
            getContext().restore();
            getContext().clearRect(0, 0, width, height);
            if (isBackgroundColorEnabled()) {
                drawGaugeBackground(currentValue);
            }
            drawGaugeDial(currentValue);
            if (isGaugeTextEnabled()) {
                drawGaugeText(currentValue);
            }
            if (isBorderEnabled()) {
                drawGaugeBorder(currentValue);
            }
            if (isTicksEnabled()) {
                drawGaugeTicks(currentValue);
            }
        }
    }

    private void setArcData(double arcValue) {
        getContext().setStrokeStyle(getBorderColor());
        getContext().setFillStyle(getCurrentGaugeColor(arcValue));
        getContext().setLineWidth(getBorderWidth());
        getContext().setLineCap(Context2d.LineCap.SQUARE);
    }

    private double getDegreesForValue(double arcValue) {
        double maxVal = (getMaxValue().doubleValue() - getMinValue().doubleValue());
        double onePercentDegree = (double) 180 / (double) 100;
        double onePercentVal = (double) 100 / maxVal;
        double degrees = onePercentDegree * (onePercentVal * (arcValue - getMinValue().doubleValue()));
        degrees = 180 + (int) degrees;
        if (degrees > 360) {
            degrees -= 360;
        }
        return degrees;
    }
}
