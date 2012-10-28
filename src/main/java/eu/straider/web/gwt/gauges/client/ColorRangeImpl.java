package eu.straider.web.gwt.gauges.client;

import com.google.gwt.canvas.dom.client.CssColor;

class ColorRangeImpl<T extends Number> implements ColorRange<T> {

    private final T minValue;
    private final T maxValue;
    private final CssColor color;

    public ColorRangeImpl(T minValue, T maxValue, CssColor color) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.color = color;
    }
    
    @Override
    public T getMin() {
        return minValue;
    }
    
    @Override
    public T getMax() {
        return maxValue;
    }
    
    @Override
    public CssColor getColor() {
        return color;
    }
    
}
