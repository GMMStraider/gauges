package eu.straider.web.gwt.gauges.client.components;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import eu.straider.web.gwt.gauges.client.ColorRange;

public class SimpleFillArcGauge<T extends Number> extends AbstractGauge<T> {

    public SimpleFillArcGauge(T minValue, T maxValue, T value) {
        this(3, minValue, maxValue, value);
    }

    public SimpleFillArcGauge(double borderWidth, T minValue, T maxValue, T value) {
        super();
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
        getCanvas().setHeight(size/2+"px");
        getCanvas().setWidth(size+"px");
        
        getCanvas().setCoordinateSpaceHeight(size/2);
        getCanvas().setCoordinateSpaceWidth(size);
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
        int width = getCanvas().getCanvasElement().getWidth();
        int height = getCanvas().getCanvasElement().getHeight();
        double degrees = setArcData(width, height, arcValue);

        int toDegrees = 180 + (int) degrees;
        if (toDegrees > 360) {
            toDegrees -= 360;
        }
        getContext().beginPath();
        getContext().arc(width / 2, width / 2-(getBorderWidth()*2), width / 2 - (getBorderWidth()*4), Math.toRadians(180), Math.toRadians(toDegrees));
        getContext().lineTo(width / 2, width / 2-(getBorderWidth()*2));
        getContext().closePath();
        getContext().fill();
        if (isBorderEnabled()) {
            drawBorder(width, height);
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
        getContext().fillText(getValueFormat().format(valueText), getCanvas().getCanvasElement().getWidth() / 2, getCanvas().getCanvasElement().getHeight() /1.2);
    }

    private void drawBorder(int width, int height) {
        getContext().beginPath();
        getContext().arc(width / 2, width / 2-(getBorderWidth()*2), width / 2 - (getBorderWidth()*4), Math.toRadians(180), Math.toRadians(0));
        getContext().closePath();
        getContext().stroke();
    }

    private double setArcData(int width, int height, double arcValue) {
        getContext().restore();
        getContext().clearRect(0, 0, width, height);
        getContext().setStrokeStyle(getBorderColor());
        getContext().setFillStyle(getCurrentGaugeColor(arcValue));
        getContext().setLineWidth(getBorderWidth());
        getContext().setLineCap(Context2d.LineCap.SQUARE);
        double maxVal = (getMaxValue().doubleValue() - getMinValue().doubleValue());
        double onePercentDegree = (double) 180 / (double) 100;
        double onePercentVal = (double) 100 / maxVal;
        double degrees = onePercentDegree * (onePercentVal * (arcValue - getMinValue().doubleValue()));
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
