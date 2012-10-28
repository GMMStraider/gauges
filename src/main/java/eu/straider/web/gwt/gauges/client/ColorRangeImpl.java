package eu.straider.web.gwt.gauges.client;

import com.google.gwt.canvas.dom.client.CssColor;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.minValue);
        hash = 53 * hash + Objects.hashCode(this.maxValue);
        hash = 53 * hash + Objects.hashCode(this.color);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ColorRangeImpl<T> other = (ColorRangeImpl<T>) obj;
        if (!Objects.equals(this.minValue, other.minValue)) {
            return false;
        }
        if (!Objects.equals(this.maxValue, other.maxValue)) {
            return false;
        }
        if (!Objects.equals(this.color, other.color)) {
            return false;
        }
        return true;
    }
    
}
