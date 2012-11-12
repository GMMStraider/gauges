package eu.straider.web.gwt.gauges.client;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsRenderable;
import com.google.gwt.user.client.ui.IsWidget;
import java.util.List;

public interface Gauge<T extends Number> extends HasValue<T>, HasAnimation, IsRenderable, IsWidget, HasVisibility, EventListener, HasAttachHandlers {

    @Override
    T getValue();

    @Override
    void setValue(T value);

    @Override
    void setValue(T value, boolean fireEvents);

    T getMinValue();

    T getMaxValue();

    void setMinValue(T minValue);

    void setMaxValue(T maxValue);
    
    void setCaption(String caption);
    
    String getCaption();
    
    void setCaptionFont(String font);
    
    String getCaptionFont();
    
    void setCaptionEnabled(boolean enabled);
    
    boolean isCaptionEnabled();
    
    void setCaptionColor(CssColor color);
    
    CssColor getCaptionColor();

    void setSize(int size);

    void setAnimationDuration(int milliseconds);

    int getAnimationDuration();

    void setValueFormat(NumberFormat mask);

    NumberFormat getValueFormat();

    void setValueFont(String font);

    String getValueFont();

    void addColorRange(ColorRange<T> range);

    List<ColorRange<T>> getColorRanges();

    void removeColorRange(ColorRange<T> range);

    void clearColorRanges();

    void setGaugeColor(CssColor color);

    CssColor getGaugeColor();

    void setStyleName(String style);

    String getStyleName();
    
    void addStyleName(String style);
    
    void removeStyleName(String style);
    
    void setBorderWidth(double borderWidth);
    
    double getBorderWidth();
    
    void setBorderColor(CssColor color);

    CssColor getBorderColor();
    
    void setBorderEnabled(boolean enabled);
    
    boolean isBorderEnabled();
    
    void setBackgroundColor(CssColor color);
    
    CssColor getBackgroundColor();
    
    void setBackgroundColorEnabled(boolean enabled);
    
    boolean isBackgroundColorEnabled();
    
    void setValueColor(CssColor color);
    
    CssColor getValueColor();
    
    void setTickColor(CssColor color);
    
    CssColor getTickColor();
    
    boolean isTicksEnabled();
    
    void setTicksEnabled(boolean enabled);
    
    void setMinorTicks(int ticks);
    
    int getMinorTicks();
    
    void setMajorTicks(int ticks);
    
    int getMajorTicks();
    
    void setMajorTicksSizeInPercentOfSize(double size);
    
    void setMinorTicksSizeInPercentOfSize(double size);
    
    double getMajorTicksSizeInPercentOfSize();
    
    double getMinorTicksSizeInPercentOfSize();
    
    void setGaugeTextEnabled(boolean enabled);
    
    boolean isGaugeTextEnabled();
    
    void setGaugeAnimation(GaugeAnimation<T> animation);
    
    GaugeAnimation<T> getGaugeAnimation();
}
