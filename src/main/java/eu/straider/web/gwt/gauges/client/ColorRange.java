package eu.straider.web.gwt.gauges.client;

import com.google.gwt.canvas.dom.client.CssColor;

public interface ColorRange<T extends Number> {

    T getMin();
    
    T getMax();
    
    CssColor getColor();
}
