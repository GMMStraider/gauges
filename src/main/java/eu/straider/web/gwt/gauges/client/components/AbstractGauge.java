package eu.straider.web.gwt.gauges.client.components;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import eu.straider.web.gwt.gauges.client.ColorRange;
import eu.straider.web.gwt.gauges.client.Gauge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class AbstractGauge<T extends Number> extends Composite implements Gauge<T> {

    private Context2d context;
    private Canvas canvas;
    private T value;
    private boolean animate;
    private T minValue;
    private T maxValue;
    private int animationDuration;
    private NumberFormat valueMask;
    private String font;
    private List<ColorRange<T>> colorRanges;
    private CssColor gaugeColor;

    public AbstractGauge() {
        animate = true;
        gaugeColor = CssColor.make("black");
        colorRanges = new ArrayList<ColorRange<T>>();
        valueMask = NumberFormat.getFormat("0");
        font = "normal 10px monospace";
        canvas = Canvas.createIfSupported();
        context = canvas.getContext2d();
        animationDuration = 200;
        initWidget(canvas);
    }
    
    protected Canvas getCanvas() {
        return canvas;
    }

    protected Context2d getContext() {
        return context;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        setValue(value, true);
    }

    @Override
    public void setValue(T value, boolean fireEvents) {
        if(value.doubleValue() > maxValue.doubleValue()) {
            value = maxValue;
        } else if(value.doubleValue() < minValue.doubleValue()) {
            value = minValue;
        }
        this.value = value;
        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isAnimationEnabled() {
        return animate;
    }

    @Override
    public void setAnimationEnabled(boolean enable) {
        animate = enable;
    }

    @Override
    public T getMinValue() {
        return minValue;
    }

    @Override
    public T getMaxValue() {
        return maxValue;
    }

    @Override
    public void setMinValue(T minValue) {
        this.minValue = minValue;
    }

    @Override
    public void setMaxValue(T maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public void setHeight(int height) {
        canvas.setHeight(height+"px");
        
        canvas.setCoordinateSpaceHeight(height);
    }

    @Override
    public void setWidth(int width) {
        canvas.setWidth(width+"px");
        canvas.setCoordinateSpaceWidth(width);
    }
    
    @Override
    public void setAnimationDuration(int milliseconds) {
        animationDuration = milliseconds;
    }
    
    @Override
    public int getAnimationDuration() {
        return animationDuration;
    }

    @Override
    public void setValueFormat(NumberFormat mask) {
        this.valueMask = mask;
    }
    
    @Override
    public NumberFormat getValueFormat() {
        return valueMask;
    }
    
    @Override
    public void setFont(String font) {
        this.font = font;
    }
    
    @Override
    public String getFont() {
        return font;
    }
    
    @Override
    public void addColorRange(ColorRange range) {
        colorRanges.add(range);
    }
    
    @Override
    public List<ColorRange<T>> getColorRanges() {
        return Collections.unmodifiableList(colorRanges);
    }
    
    @Override
    public void removeColorRange(ColorRange range) {
        colorRanges.remove(range);
    }
    
    @Override
    public void clearColorRanges() {
        colorRanges.clear();
    }
    @Override
    public void setGaugeColor(CssColor color) {
        gaugeColor = color;
    }
    
    @Override
    public CssColor getGaugeColor() {
        return gaugeColor;
    }
}
