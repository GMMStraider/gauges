package eu.straider.web.gwt.gauges.client.components;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import eu.straider.web.gwt.gauges.client.ColorRange;

public class MarkerGauge<T extends Number> extends AbstractGauge<T> {

    private double lineWidth;
    private SimpleGaugeValueAnimation animation;

    public MarkerGauge(T minValue, T maxValue, T value) {
        this(20, minValue, maxValue, value);
    }

    public MarkerGauge(double lineWidth, T minValue, T maxValue, T value) {
        super();
        animation = new SimpleGaugeValueAnimation(this);
        this.lineWidth = lineWidth;
        setFont("bold 32px Lucida Console");
        setMinValue(minValue);
        setMaxValue(maxValue);
        setAnimationEnabled(true);
        setValue(value, false);
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
    void drawGaugeDial(double currentValue) {
        int width = getCanvas().getCanvasElement().getWidth();
        int height = getCanvas().getCanvasElement().getHeight();
        double maxVal = (getMaxValue().doubleValue() - getMinValue().doubleValue());
        double onePercentGauge = (double) height / (double) 100;
        double onePercentVal = (double) 100 / maxVal;
        drawColorRanges(onePercentGauge, onePercentVal, width, height);
        getContext().setStrokeStyle(getGaugeColor());
        getContext().setLineWidth(1);
        getContext().beginPath();
        double gaugePos = height - (onePercentGauge * (onePercentVal * (currentValue)));
        getContext().moveTo(width / 4 - (2 * lineWidth), gaugePos);
        getContext().lineTo(width / 4 + (2 * lineWidth), gaugePos);
        getContext().closePath();
        getContext().stroke();
    }

    @Override
    void drawGaugeBackground(double currentValue) {
        int width = getCanvas().getCanvasElement().getWidth();
        int height = getCanvas().getCanvasElement().getHeight();
        getContext().setStrokeStyle(getBackgroundColor());
        getContext().setLineWidth(lineWidth + (2 * getBorderWidth()));
    }

    @Override
    void drawGaugeText(double currentValue) {
        getContext().setFillStyle(CssColor.make("black"));
        getContext().setFont(getFont());
        getContext().setTextAlign(Context2d.TextAlign.CENTER);
        getContext().fillText(getValueFormat().format(currentValue), getCanvas().getCanvasElement().getWidth() / 2, getCanvas().getCanvasElement().getHeight() / 2);
    }

    @Override
    void drawGaugeBorder(double currentValue) {
        int width = getCanvas().getCanvasElement().getWidth();
        int height = getCanvas().getCanvasElement().getHeight();
        getContext().setStrokeStyle(getBorderColor());
        getContext().setLineWidth(lineWidth + (2 * getBorderWidth()));
    }

    @Override
    protected void drawGauge(double currentValue) {
        int width = getCanvas().getCanvasElement().getWidth();
        int height = getCanvas().getCanvasElement().getHeight();
        getContext().restore();
        getContext().clearRect(0, 0, width, height);
        super.drawGauge(currentValue);
    }

    @Override
    void drawGaugeTicks(double currentValue) {
    }

    private void drawColorRanges(double onePercentGauge, double onePercentVal, int width, int height) {
        getContext().setLineWidth(lineWidth);
        getContext().setLineCap(Context2d.LineCap.BUTT);
        for (ColorRange range : getColorRanges()) {
            Number min = range.getMin();
            Number max = range.getMax();
            double minHeight = onePercentGauge * (onePercentVal * (min.doubleValue()));
            double maxHeight = onePercentGauge * (onePercentVal * (max.doubleValue()));
            getContext().setStrokeStyle(range.getColor());
            getContext().beginPath();
            getContext().moveTo(width / 4, height - minHeight);
            getContext().lineTo(width / 4, height - maxHeight);
            getContext().closePath();
            getContext().stroke();
        }
    }
}
