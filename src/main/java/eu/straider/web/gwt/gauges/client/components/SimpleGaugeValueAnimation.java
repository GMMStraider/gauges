package eu.straider.web.gwt.gauges.client.components;

import eu.straider.web.gwt.gauges.client.GaugeAnimation;
import com.google.gwt.animation.client.Animation;

class SimpleGaugeValueAnimation<T extends Number> extends Animation implements GaugeAnimation<T> {

    private T oldValue;
    private T step;
    private AbstractGauge<T> gauge;

    public SimpleGaugeValueAnimation(AbstractGauge<T> gauge) {
        this.gauge = gauge;
    }

    @Override
    protected void onUpdate(double progress) {
        double tmp = oldValue.doubleValue() + (step.doubleValue() * progress);
        gauge.drawGauge(tmp);
    }

    @Override
    public void goToValue(T toValue, T oldValue, int milliseconds) {
        this.oldValue = oldValue;
        step = (T) Double.valueOf(toValue.doubleValue() - oldValue.doubleValue());
        run(milliseconds);
    }
}
