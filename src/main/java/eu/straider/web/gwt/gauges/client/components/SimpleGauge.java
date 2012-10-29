package eu.straider.web.gwt.gauges.client.components;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import eu.straider.web.gwt.gauges.client.ColorRange;

public class SimpleGauge<T extends Number> extends AbstractGauge<T> {

    private double lineWidth;
    private int gaugeDegrees;
    private int startDegrees = 150;
    private SimpleGaugeValueAnimation animation;

    public SimpleGauge(T minValue, T maxValue, T value) {
        this(20, 240, minValue, maxValue, value);
    }

    public SimpleGauge(double lineWidth, int gaugeDegrees, T minValue, T maxValue, T value) {
        super();
        animation = new SimpleGaugeValueAnimation(this);
        setGaugeDegrees(gaugeDegrees);
        this.lineWidth = lineWidth;
        setFont("bold 32px Lucida Console");
        setMinValue(minValue);
        setMaxValue(maxValue);
        setAnimationEnabled(true);
        setValue(value, false);
    }

    public final void setGaugeDegrees(int degrees) {
        if (degrees > 1) {
            gaugeDegrees = degrees;
            startDegrees = 270 - (gaugeDegrees / 2);
        }
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

    private void drawGenericArc(double arcValue, int width, int height) {
        getContext().setLineCap(Context2d.LineCap.ROUND);
        double maxVal = (getMaxValue().doubleValue() - getMinValue().doubleValue());
        double onePercentDegree = (double) gaugeDegrees / (double) 100;
        double onePercentVal = (double) 100 / maxVal;
        double degrees = onePercentDegree * (onePercentVal * (arcValue - getMinValue().doubleValue()));

        int toDegrees = startDegrees + (int) degrees;
        if (toDegrees > 360) {
            toDegrees -= 360;
        }
        getContext().beginPath();
        getContext().closePath();
        getContext().arc(width / 2, height / 2, width / 2 - lineWidth, Math.toRadians(startDegrees), Math.toRadians(toDegrees));
        getContext().stroke();
    }

    @Override
    void drawGaugeDial(double currentValue) {
        int width = getCanvas().getCanvasElement().getWidth();
        int height = getCanvas().getCanvasElement().getHeight();
        getContext().restore();
        if (!isBorderEnabled()) {
            getContext().clearRect(0, 0, width, height);
        }
        getContext().setStrokeStyle(getCurrentGaugeColor(currentValue));
        getContext().setLineWidth(lineWidth);
        drawGenericArc(currentValue, width, height);
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
        getContext().restore();
        getContext().clearRect(0, 0, width, height);
        getContext().setStrokeStyle(getBorderColor());
        getContext().setLineWidth(lineWidth + (2 * getBorderWidth()));
        drawGenericArc(currentValue, width, height);
    }

    @Override
    void drawGaugeTicks(double currentValue) {
        
    }
}
