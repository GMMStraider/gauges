package eu.straider.web.gwt.gauges.client.components;

import com.google.gwt.animation.client.Animation;
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
    public final void setValue(T value, boolean fireEvents) {
        T oldValue = getValue();
        super.setValue(value, fireEvents);

        if (value.doubleValue() > getMaxValue().doubleValue()) {
            value = getMaxValue();
        } else if (value.doubleValue() < getMinValue().doubleValue()) {
            value = getMinValue();
        }
        if (oldValue == null) {
            oldValue = getMinValue();
        }

        if (isAnimationEnabled()) {
            SimpleGaugeValueAnimation animation = new SimpleGaugeValueAnimation();
            animation.goToValue(value, oldValue, getAnimationDuration());
        } else {
            drawArc(value.doubleValue());
            drawValueText(value);
        }
    }

    void drawArc(double arcValue) {
        setArcData(arcValue);
        double degrees = getDegreesForValue(arcValue);

        getContext().beginPath();
        getContext().arc(gaugeCenterX, gaugeCenterY, radius, Math.toRadians(180), Math.toRadians(degrees));
        getContext().lineTo(gaugeCenterX, gaugeCenterY);
        getContext().closePath();
        getContext().fill();
        if (isBorderEnabled()) {
            drawBorder();
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

    private void drawValueText(T valueText) {
        getContext().setFillStyle("black");
        getContext().setFont(getFont());
        getContext().setTextAlign(Context2d.TextAlign.CENTER);
        getContext().fillText(getValueFormat().format(valueText), getCanvas().getCanvasElement().getWidth() / 2, getCanvas().getCanvasElement().getHeight() / 1.2);
    }

    private void drawBorder() {
        getContext().beginPath();
        getContext().arc(gaugeCenterX, gaugeCenterY, radius, Math.toRadians(180), Math.toRadians(0));
        getContext().closePath();
        getContext().stroke();

        for (int val = getMinValue().intValue(); val <= getMaxValue().intValue(); val += 10) {
            getContext().beginPath();
            final double radiansVal = Math.toRadians(getDegreesForValue((int) val));
            int x = (int) (Math.cos(radiansVal) * (radius-10));
            int y = (int) (Math.sin(radiansVal) * (radius-10));
            getContext().moveTo(gaugeCenterX + x, gaugeCenterY + y);
            x = (int) (Math.cos(radiansVal) * radius);
            y = (int) (Math.sin(radiansVal) * radius);
            getContext().lineTo(gaugeCenterX + x, gaugeCenterY + y);
            getContext().stroke();
            getContext().closePath();
        }
    }

    private void setArcData(double arcValue) {
        getContext().restore();
        getContext().clearRect(0, 0, width, height);
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

    class SimpleGaugeValueAnimation extends Animation {

        private T oldValue;
        private T step;

        void goToValue(T toValue, T oldValue, int milliseconds) {
            this.oldValue = oldValue;
            step = (T) Double.valueOf(toValue.doubleValue() - oldValue.doubleValue());
            run(milliseconds);
        }

        @Override
        protected void onUpdate(double progress) {
            double tmp = oldValue.doubleValue() + (step.doubleValue() * progress);
            drawArc(tmp);
            drawValueText((T) new Double(tmp));
        }
    }
}
