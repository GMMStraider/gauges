package eu.straider.web.gwt.gauges.client.components;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import eu.straider.web.gwt.gauges.client.ColorRange;

public class SimpleFillArcGauge<T extends Number> extends AbstractGauge<T> {

    private double lineWidth;

    public SimpleFillArcGauge(T minValue, T maxValue, T value) {
        this(3, minValue, maxValue, value);
    }
    
    public SimpleFillArcGauge(double lineWidth, T minValue, T maxValue, T value) {
        super();
        this.lineWidth = lineWidth;
        setFont("bold 32px Lucida Console");
        setMinValue(minValue);
        setMaxValue(maxValue);
        setAnimationEnabled(true);
        setValue(value, false);
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
        if(oldValue == null) {
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
        getContext().restore();
        getContext().clearRect(0, 0, width, height);
        getContext().setStrokeStyle("black");
        getContext().setFillStyle(getCurrentGaugeColor(arcValue));
        getContext().setLineWidth(lineWidth);
        
        getContext().setLineCap(Context2d.LineCap.SQUARE);
        double maxVal = (getMaxValue().doubleValue()-getMinValue().doubleValue());
        double onePercentDegree = (double) 180 / (double) 100;
        double onePercentVal = (double) 100/maxVal ;
        double degrees = onePercentDegree * (onePercentVal * (arcValue-getMinValue().doubleValue()));

        int toDegrees = 180 + (int) degrees;
        if (toDegrees > 360) {
            toDegrees -= 360;
        }
        getContext().beginPath();
        getContext().arc(width / 2, height / 2, width / 2 - lineWidth, Math.toRadians(180), Math.toRadians(toDegrees));
        getContext().lineTo(width / 2, height / 2);
        getContext().closePath();
        getContext().fill();
        getContext().stroke();
    }

    private CssColor getCurrentGaugeColor(double arcValue) {
        CssColor color = null;
        for(ColorRange range : getColorRanges()) {
            if(arcValue>=range.getMin().doubleValue() && arcValue<=range.getMax().doubleValue()) {
                color = range.getColor();
            }
        }
        if(color == null) {
            color = getGaugeColor();
        }
        return color;
    }
    
    private void drawValueText(T valueText) {
        getContext().setFillStyle("black");
        getContext().setFont(getFont());
        getContext().setTextAlign(Context2d.TextAlign.CENTER);
        getContext().fillText(getValueFormat().format(valueText), getCanvas().getCanvasElement().getWidth() / 2, getCanvas().getCanvasElement().getHeight() / 2 + 50);
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
