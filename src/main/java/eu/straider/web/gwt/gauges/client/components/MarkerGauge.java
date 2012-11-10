package eu.straider.web.gwt.gauges.client.components;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import eu.straider.web.gwt.gauges.client.ColorRange;

public class MarkerGauge<T extends Number> extends AbstractGauge<T> {

    private final static double PADDING = 3;
    private int width = 1;
    private int height = 1;
    private double maxVal = 1;
    private double onePercentGauge = 1;
    private double onePercentVal = 1;
    private double majorTickLength = 1;
    private double minorTickLength = 1;
    private double tickSizeMajor = 1;
    private double tickSizeMinor = 1;
    private double tickerStartPos = 1;
    private double tickerTextWidth = 0;
    private double colorGaugeWidth = 0;

    public MarkerGauge(T minValue, T maxValue, T value) {
        super();
        width = 0;
        height = 0;
        setFont("bold 16px Lucida Console");
        setMinValue(minValue);
        setMaxValue(maxValue);
        setAnimationEnabled(true);
        setValue(value, false);
    }

    @Override
    public void setSize(int size) {
        width = size / 3;
        height = size;
        getCanvas().setHeight(height + "px");
        getCanvas().setWidth(width + "px");

        getCanvas().setCoordinateSpaceHeight(height);
        getCanvas().setCoordinateSpaceWidth(width);
        updateGaugeCalcData();
    }

    @Override
    public final void setMaxValue(T maxValue) {
        super.setMaxValue(maxValue);
        updateGaugeCalcData();
    }

    @Override
    public final void setMinValue(T minValue) {
        super.setMinValue(minValue);
        updateGaugeCalcData();
    }

    @Override
    public void setMajorTicks(int ticks) {
        super.setMajorTicks(ticks);
        updateGaugeCalcData();
    }

    @Override
    public void setMinorTicks(int ticks) {
        super.setMinorTicks(ticks);
        updateGaugeCalcData();
    }

    @Override
    public void setMajorTicksSizeInPercentOfSize(double size) {
        super.setMajorTicksSizeInPercentOfSize(size);
        updateGaugeCalcData();
    }

    @Override
    public void setMinorTicksSizeInPercentOfSize(double size) {
        super.setMinorTicksSizeInPercentOfSize(size);
        updateGaugeCalcData();
    }

    @Override
    public final void setFont(String font) {
        super.setFont(font);
        updateGaugeCalcData();
    }

    private void updateGaugeCalcData() {
        majorTickLength = getGaugeHeight() / 100 * getMajorTicksSizeInPercentOfSize();
        minorTickLength = getGaugeHeight() / 100 * getMinorTicksSizeInPercentOfSize();
        tickSizeMajor = maxVal / (getMajorTicks() - 1);
        tickSizeMinor = tickSizeMajor / (getMinorTicks() + 1);
        if (getMaxValue() != null && getMinValue() != null) {
            maxVal = getMaxValue().doubleValue() - getMinValue().doubleValue();
            onePercentGauge = (double) getGaugeHeight() / (double) 100;
            onePercentVal = (double) 100 / maxVal;
            getContext().setFont(getFont());
            double tmpMaxFontWidth = getContext().measureText(getValueFormat().format(getMaxValue().doubleValue())).getWidth();
            double tmpMinFontWidth = getContext().measureText(getValueFormat().format(getMinValue().doubleValue())).getWidth();
            if (tmpMaxFontWidth > tmpMinFontWidth) {
                tickerTextWidth = tmpMaxFontWidth;
            } else {
                tickerTextWidth = tmpMinFontWidth;
            }
            colorGaugeWidth = width - ((getGaugeStartPos() * 1.5) + tickerTextWidth + majorTickLength + (2 * getBorderWidth()) + (2 * PADDING));
            tickerStartPos = getGaugeStartPos() + colorGaugeWidth + PADDING;
        }
    }

    private double getGaugeStartPos() {
        return width / 5;
    }

    private double getGaugeHeight() {
        return height - (width / 2.5) - (2 * getBorderWidth());
    }

    @Override
    void drawGaugeDial(double currentValue) {
        drawColorRanges(onePercentGauge, onePercentVal);
        getContext().setFillStyle(getGaugeColor());
        getContext().beginPath();
        double gaugePos = getGaugePosForValue(currentValue);
        getContext().fillRect(3 * getBorderWidth(), gaugePos, width - (6 * getBorderWidth()), 1.5);
        getContext().closePath();
        getContext().fill();
    }

    @Override
    void drawGaugeBackground(double currentValue) {
        getContext().setFillStyle(getBackgroundColor());
        double tmpDelta = getBorderWidth() / 2 + (2 * getBorderWidth());
        fillRoundRect(tmpDelta, tmpDelta, width - (5 * getBorderWidth()), height - (5 * getBorderWidth()), width / 5);
    }

    @Override
    void drawGaugeText(double currentValue) {
    }

    @Override
    void drawGaugeBorder(double currentValue) {
        getContext().setStrokeStyle(getBorderColor());
        getContext().setLineWidth(getBorderWidth());
        double tmpDelta = getBorderWidth() / 2 + (2 * getBorderWidth());
        strokeRoundRect(tmpDelta, tmpDelta, width - (5 * getBorderWidth()), height - (5 * getBorderWidth()), width / 5);
    }

    private void strokeRoundRect(double x, double y, double w, double h, double r) {
        roundRect(x, y, w, h, r);
        getContext().stroke();
    }

    private void fillRoundRect(double x, double y, double w, double h, double r) {
        roundRect(x, y, w, h, r);
        getContext().fill();
    }

    private void roundRect(double x, double y, double w, double h, double r) {
        getContext().beginPath();
        getContext().moveTo(x + r, y);
        getContext().lineTo(x + w - r, y);
        getContext().quadraticCurveTo(x + w, y, x + w, y + r);
        getContext().lineTo(x + w, y + h - r);
        getContext().quadraticCurveTo(x + w, y + h, x + w - r, y + h);
        getContext().lineTo(x + r, y + h);
        getContext().quadraticCurveTo(x, y + h, x, y + h - r);
        getContext().lineTo(x, y + r);
        getContext().quadraticCurveTo(x, y, x + r, y);
        getContext().closePath();
    }

    @Override
    protected void drawGauge(double currentValue) {
        getContext().restore();
        getContext().clearRect(0, 0, width, height);
        if (isBackgroundColorEnabled()) {
            drawGaugeBackground(currentValue);
        }
        if (isBorderEnabled()) {
            drawGaugeBorder(currentValue);
        }
        if (isGaugeTextEnabled()) {
            drawGaugeText(currentValue);
        }
        if (isTicksEnabled()) {
            drawGaugeTicks(currentValue);
        }
        drawGaugeDial(currentValue);
    }

    @Override
    void drawGaugeTicks(double currentValue) {
        double majorVal = getMinValue().doubleValue();
        for (int i = 0; i < getMajorTicks(); i++) {
            double minorVal = majorVal;
            if (minorVal < getMaxValue().doubleValue()) {
                for (int j = 0; j < getMinorTicks(); j++) {
                    minorVal += tickSizeMinor;
                    drawTick(minorVal, tickerStartPos, minorTickLength);
                }
            }
            drawTick(majorVal, tickerStartPos, majorTickLength);
            if (isGaugeTextEnabled()) {
                drawGaugeTickerText(majorVal, tickerStartPos + majorTickLength + PADDING);
            }
            majorVal += tickSizeMajor;
        }
    }

    private double getGaugePosForValue(double value) {
        return (getGaugeHeight() - (onePercentGauge * (onePercentVal * (value)))) + getGaugeStartPos();
    }

    private void drawTick(double value, double tickStartPosition, double tickLength) {
        getContext().setFillStyle(getTickColor());
        getContext().beginPath();
        getContext().setLineWidth(1);
        double gaugePos = getGaugePosForValue(value);
        getContext().fillRect((int) tickStartPosition, gaugePos, tickLength, 1);
        getContext().closePath();
        getContext().fill();
    }

    void drawGaugeTickerText(double currentValue, double textStartPosition) {
        getContext().setFillStyle(getTextColor());
        getContext().setFont(getFont());
        getContext().setTextAlign(Context2d.TextAlign.CENTER);
        getContext().setTextBaseline(Context2d.TextBaseline.MIDDLE);
        getContext().setTextAlign(Context2d.TextAlign.LEFT);
        double textPos = getGaugePosForValue(currentValue);
        getContext().fillText(getValueFormat().format(currentValue), textStartPosition, textPos);
    }

    private void drawColorRanges(double onePercentGauge, double onePercentVal) {
        for (ColorRange range : getColorRanges()) {
            Number min = range.getMin();
            Number max = range.getMax();
            double minHeight = onePercentGauge * (onePercentVal * (min.doubleValue()));
            double maxHeight = onePercentGauge * (onePercentVal * (max.doubleValue()));
            getContext().setFillStyle(range.getColor());
            getContext().beginPath();
            double colorHeight = (getGaugeHeight() - maxHeight) - (getGaugeHeight() - minHeight);
            getContext().fillRect(getGaugeStartPos(), getGaugeHeight() - minHeight + getGaugeStartPos(), colorGaugeWidth, colorHeight);
            getContext().closePath();
            getContext().fill();
        }
    }
}
