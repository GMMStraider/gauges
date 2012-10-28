package eu.straider.web.gwt.gauges.client;

import com.google.gwt.canvas.dom.client.CssColor;

public class ColorRangeBuilder<T extends Number> {

    private T minValue;
    private T maxValue;
    private CssColor color;

    public ColorRangeBuilder() {
    }

    public ColorRangeBuilder<T> setMinValue(T minValue) {
        this.minValue = minValue;
        return this;
    }

    public ColorRangeBuilder<T> setMaxValue(T maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public ColorRangeBuilder<T> setColor(CssColor color) {
        this.color = color;
        return this;
    }

    public ColorRange<T> build() {
        ColorRange<T> range = new ColorRangeImpl<T>(minValue, maxValue, color);
        return range;
    }
}
