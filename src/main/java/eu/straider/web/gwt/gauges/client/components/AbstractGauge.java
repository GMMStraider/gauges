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
import eu.straider.web.gwt.gauges.client.GaugeAnimation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class AbstractGauge<T extends Number> extends Composite implements Gauge<T> {

    private Context2d context;
    private Canvas canvas;
    private T value;
    private boolean animate;
    private boolean borderVisible;
    private T minValue;
    private T maxValue;
    private int animationDuration;
    private NumberFormat valueMask;
    private String valueFont;
    private String captionFont;
    private boolean captionVisible;
    private List<ColorRange<T>> colorRanges;
    private CssColor gaugeColor;
    private CssColor borderColor;
    private CssColor backgroundColor;
    private CssColor tickColor;
    private CssColor textColor;
    private CssColor captionColor;
    private boolean backgroundEnabled;
    private double borderWidth;
    private int majorTicks = 0;
    private int minorTicks = 0;
    private boolean drawTicks;
    private boolean drawText;
    private double majorTicksSize = 1;
    private double minorTicksSize = 1;
    private String caption;
    private GaugeAnimation<T> gaugeAnimation;

    public AbstractGauge() {
        backgroundEnabled = true;
        animate = true;
        drawText = true;
        captionVisible = true;
        caption = "";
        gaugeColor = CssColor.make("black");
        borderColor = CssColor.make("black");
        tickColor = CssColor.make("black");
        textColor = CssColor.make("black");
        captionColor = CssColor.make("black");
        backgroundColor = CssColor.make("white");
        colorRanges = new ArrayList<ColorRange<T>>();
        valueMask = NumberFormat.getFormat("0");
        valueFont = "normal 10px monospace";
        captionFont = "normal 10px monospace";
        canvas = Canvas.createIfSupported();
        context = canvas.getContext2d();
        animationDuration = 200;
        borderWidth = 1;
        majorTicks = 0;
        minorTicks = 0;
        majorTicksSize = 3;
        minorTicksSize = 1;
        drawTicks = false;
        gaugeAnimation = new SimpleGaugeValueAnimation<T>(this);
        initWidget(canvas);
    }

    @Override
    public void setValueColor(CssColor color) {
        textColor = color;
        repaint();
    }

    @Override
    public CssColor getValueColor() {
        return textColor;
    }

    @Override
    public void setCaptionColor(CssColor color) {
        captionColor = color;
        repaint();
    }

    @Override
    public CssColor getCaptionColor() {
        return captionColor;
    }

    @Override
    public void setTickColor(CssColor color) {
        tickColor = color;
        repaint();
    }

    @Override
    public CssColor getTickColor() {
        return tickColor;
    }

    @Override
    public void setGaugeAnimation(GaugeAnimation<T> animation) {
        gaugeAnimation = animation;
    }

    @Override
    public GaugeAnimation<T> getGaugeAnimation() {
        return gaugeAnimation;
    }

    protected Canvas getCanvas() {
        return canvas;
    }

    protected Context2d getContext() {
        return context;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
        repaint();
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaptionFont(String font) {
        captionFont = font;
        repaint();
    }

    @Override
    public String getCaptionFont() {
        return captionFont;
    }

    @Override
    public void setCaptionEnabled(boolean enabled) {
        captionVisible = enabled;
        repaint();
    }

    @Override
    public boolean isCaptionEnabled() {
        return captionVisible;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            repaint();
        }
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
        T oldValue = getValue();
        if (value.doubleValue() > maxValue.doubleValue()) {
            value = maxValue;
        } else if (value.doubleValue() < minValue.doubleValue()) {
            value = minValue;
        }
        this.value = value;
        if (oldValue == null) {
            oldValue = getMinValue();
        }

        if (isAnimationEnabled()) {
            gaugeAnimation.goToValue(value, oldValue, getAnimationDuration());
        } else {
            repaint();
        }
        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void setBackgroundColor(CssColor color) {
        backgroundColor = color;
        repaint();
    }

    @Override
    public CssColor getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColorEnabled(boolean enabled) {
        backgroundEnabled = enabled;
        repaint();
    }

    @Override
    public boolean isBackgroundColorEnabled() {
        return backgroundEnabled;
    }

    @Override
    public boolean isAnimationEnabled() {
        return animate;
    }

    @Override
    public void setAnimationEnabled(boolean enable) {
        animate = enable;
        repaint();
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
        repaint();
    }

    @Override
    public void setMaxValue(T maxValue) {
        this.maxValue = maxValue;
        repaint();
    }

    @Override
    public void setSize(int size) {
        canvas.setHeight(size + "px");
        canvas.setWidth(size + "px");

        canvas.setCoordinateSpaceHeight(size);
        canvas.setCoordinateSpaceWidth(size);
        repaint();
    }

    @Override
    public void setAnimationDuration(int milliseconds) {
        animationDuration = milliseconds;
        repaint();
    }

    @Override
    public int getAnimationDuration() {
        return animationDuration;
    }

    @Override
    public void setValueFormat(NumberFormat mask) {
        this.valueMask = mask;
        repaint();
    }

    @Override
    public NumberFormat getValueFormat() {
        return valueMask;
    }

    @Override
    public void setValueFont(String font) {
        this.valueFont = font;
        repaint();
    }

    @Override
    public String getValueFont() {
        return valueFont;
    }

    @Override
    public void addColorRange(ColorRange range) {
        colorRanges.add(range);
        repaint();
    }

    @Override
    public List<ColorRange<T>> getColorRanges() {
        return Collections.unmodifiableList(colorRanges);
    }

    @Override
    public void removeColorRange(ColorRange range) {
        colorRanges.remove(range);
        repaint();
    }

    @Override
    public void clearColorRanges() {
        colorRanges.clear();
        repaint();
    }

    @Override
    public void setGaugeColor(CssColor color) {
        gaugeColor = color;
        repaint();
    }

    @Override
    public CssColor getGaugeColor() {
        return gaugeColor;
    }

    @Override
    public void setBorderWidth(double borderWidth) {
        this.borderWidth = borderWidth;
        repaint();
    }

    @Override
    public double getBorderWidth() {
        return borderWidth;
    }

    @Override
    public void setBorderColor(CssColor color) {
        this.borderColor = color;
        repaint();
    }

    @Override
    public CssColor getBorderColor() {
        return borderColor;
    }

    @Override
    public void setBorderEnabled(boolean enabled) {
        borderVisible = enabled;
        repaint();
    }

    @Override
    public boolean isBorderEnabled() {
        return borderVisible;
    }

    @Override
    public boolean isTicksEnabled() {
        return drawTicks;
    }

    @Override
    public void setTicksEnabled(boolean enabled) {
        drawTicks = enabled;
        repaint();
    }

    @Override
    public void setMinorTicks(int ticks) {
        minorTicks = ticks;
        repaint();
    }

    @Override
    public int getMinorTicks() {
        return minorTicks;
    }

    @Override
    public void setMajorTicks(int ticks) {
        majorTicks = ticks;
        repaint();
    }

    @Override
    public int getMajorTicks() {
        return majorTicks;
    }

    @Override
    public void setMajorTicksSizeInPercentOfSize(double size) {
        majorTicksSize = size;
        repaint();
    }

    @Override
    public void setMinorTicksSizeInPercentOfSize(double size) {
        minorTicksSize = size;
        repaint();
    }

    @Override
    public double getMajorTicksSizeInPercentOfSize() {
        return majorTicksSize;
    }

    @Override
    public double getMinorTicksSizeInPercentOfSize() {
        return minorTicksSize;
    }

    @Override
    public void setGaugeTextEnabled(boolean enabled) {
        drawText = enabled;
        repaint();
    }

    @Override
    public boolean isGaugeTextEnabled() {
        return drawText;
    }

    private void repaint() {
        if (getValue() != null) {
            drawGauge(getValue().doubleValue());
        }
    }

    protected void drawGauge(double currentValue) {
        if (isVisible()) {
            if (isBackgroundColorEnabled()) {
                drawGaugeBackground(currentValue);
            }
            if (isBorderEnabled()) {
                drawGaugeBorder(currentValue);
            }
            drawGaugeDial(currentValue);
            if (isGaugeTextEnabled()) {
                drawGaugeText(currentValue);
            }
            if (isCaptionEnabled()) {
                drawGaugeCaption();
            }
            if (isTicksEnabled()) {
                drawGaugeTicks(currentValue);
            }
        }
    }

    abstract void drawGaugeDial(double currentValue);

    abstract void drawGaugeText(double currentValue);

    abstract void drawGaugeCaption();

    abstract void drawGaugeBorder(double currentValue);

    abstract void drawGaugeTicks(double currentValue);

    abstract void drawGaugeBackground(double currentValue);
}
