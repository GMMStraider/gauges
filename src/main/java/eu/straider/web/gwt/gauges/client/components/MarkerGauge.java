package eu.straider.web.gwt.gauges.client.components;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import eu.straider.web.gwt.gauges.client.ColorRange;

public class MarkerGauge<T extends Number> extends AbstractGauge<T> {

    private double lineWidth;
    private SimpleGaugeValueAnimation animation;
    private int width;
    private int height;

    public MarkerGauge(T minValue, T maxValue, T value) {
        this(20, minValue, maxValue, value);
    }

    public MarkerGauge(double lineWidth, T minValue, T maxValue, T value) {
        super();
        animation = new SimpleGaugeValueAnimation(this);
        this.lineWidth = lineWidth;
        width = 0;
        height = 0;
        setFont("bold 32px Lucida Console");
        setMinValue(minValue);
        setMaxValue(maxValue);
        setAnimationEnabled(true);
        setValue(value, false);
    }

    @Override
    public void setSize(int size) {
        getCanvas().setHeight(size + "px");
        getCanvas().setWidth(size / 2 + "px");

        getCanvas().setCoordinateSpaceHeight(size);
        getCanvas().setCoordinateSpaceWidth(size / 2);
        width = size / 2;
        height = size;
    }

    private double getGaugeHeight() {
        return height - (2 * getBorderWidth());
    }

    @Override
    void drawGaugeDial(double currentValue) {
        double maxVal = (getMaxValue().doubleValue() - getMinValue().doubleValue());
        double onePercentGauge = (double) getGaugeHeight() / (double) 100;
        double onePercentVal = (double) 100 / maxVal;
        drawColorRanges(onePercentGauge, onePercentVal);
        getContext().setFillStyle(getGaugeColor());
        getContext().beginPath();
        double gaugePos = (getGaugeHeight() - (onePercentGauge * (onePercentVal * (currentValue)))) + getBorderWidth();
        getContext().fillRect(width / 4 - (2 * lineWidth), gaugePos, 4 * lineWidth, 1);
        getContext().closePath();
        getContext().fill();
    }

    @Override
    void drawGaugeBackground(double currentValue) {
        getContext().setStrokeStyle(getBackgroundColor());
        getContext().setLineWidth(lineWidth + (2 * getBorderWidth()));
    }

    @Override
    void drawGaugeText(double currentValue) {
        getContext().setFillStyle(CssColor.make("black"));
        getContext().setFont(getFont());
        getContext().setTextAlign(Context2d.TextAlign.CENTER);
        getContext().fillText(getValueFormat().format(currentValue), (width / 4) * 3, height / 2);
    }

    @Override
    void drawGaugeBorder(double currentValue) {
        getContext().setStrokeStyle(getBorderColor());
        getContext().setLineWidth(lineWidth + (2 * getBorderWidth()));
    }

    @Override
    protected void drawGauge(double currentValue) {
        getContext().restore();
        getContext().clearRect(0, 0, width, height);
        super.drawGauge(currentValue);
    }

    @Override
    void drawGaugeTicks(double currentValue) {
        double majorTickLength = getGaugeHeight() / 100 * getMajorTicksSizeInPercentOfSize();
        double minorTickLength = getGaugeHeight() / 100 * getMinorTicksSizeInPercentOfSize();
        double maxVal = (getMaxValue().doubleValue() - getMinValue().doubleValue());
        double tickSizeMajor = maxVal / (getMajorTicks() - 1);
        double tickSizeMinor = tickSizeMajor / (getMinorTicks() + 1);
        double majorVal = getMinValue().doubleValue();
        for (int i = 0; i <= getMajorTicks(); i++) {
            double minorVal = majorVal;
            for (int j = 0; j < getMinorTicks(); j++) {
                minorVal += tickSizeMinor;
                drawTick(minorVal, width / 4 + (2 * lineWidth), minorTickLength);
            }
            drawTick(majorVal, width / 4 + (2 * lineWidth), majorTickLength);
            majorVal += tickSizeMajor;
        }
    }

    private void drawTick(double value, double tickStartPosition, double tickLength) {
        getContext().setFillStyle(CssColor.make("black"));
        getContext().beginPath();
        getContext().setLineWidth(1);
        double maxVal = (getMaxValue().doubleValue() - getMinValue().doubleValue());
        double onePercentGauge = (double) getGaugeHeight() / (double) 100;
        double onePercentVal = (double) 100 / maxVal;
        double gaugePos = (getGaugeHeight() - (onePercentGauge * (onePercentVal * (value)))) + getBorderWidth();
        getContext().fillRect((int) tickStartPosition, gaugePos, tickLength, 1);
        getContext().closePath();
        getContext().fill();
    }

    private void drawColorRanges(double onePercentGauge, double onePercentVal) {
        getContext().setLineWidth(lineWidth);
        getContext().setLineCap(Context2d.LineCap.BUTT);
        for (ColorRange range : getColorRanges()) {
            Number min = range.getMin();
            Number max = range.getMax();
            double minHeight = onePercentGauge * (onePercentVal * (min.doubleValue()));
            double maxHeight = onePercentGauge * (onePercentVal * (max.doubleValue()));
            getContext().setStrokeStyle(range.getColor());
            getContext().beginPath();
            getContext().moveTo(width / 4, getGaugeHeight() - minHeight + getBorderWidth());
            getContext().lineTo(width / 4, getGaugeHeight() - maxHeight + getBorderWidth());
            getContext().closePath();
            getContext().stroke();
        }
    }
}
