package eu.straider.web.gwt.gauges.client;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import eu.straider.web.gwt.gauges.client.components.MarkerGauge;
import eu.straider.web.gwt.gauges.client.components.SimpleFillArcGauge;
import eu.straider.web.gwt.gauges.client.components.SimpleGauge;

/**
 * Main entry point.
 */
public class MainEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
        final Gauge<Integer> gauge = addSimpleGauge();
        final Gauge<Integer> gauge2 = addSimpleFillArcGauge();
        final Gauge<Integer> gauge3 = addMarkerGauge();
        final IntegerBox box = new IntegerBox();
        RootPanel.get().add(box);
        Button apply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                gauge.setValue(box.getValue());
                gauge2.setValue(box.getValue());
                gauge3.setValue(box.getValue());
            }
        });
        RootPanel.get().add(apply);
    }

    private Gauge<Integer> addMarkerGauge() {
        final Gauge<Integer> gauge = new MarkerGauge(10, 0, 100, 50);
        setGaugeData(gauge);
        RootPanel.get().add(gauge);
        return gauge;
    }

    private Gauge<Integer> addSimpleGauge() {
        final Gauge<Integer> gauge = new SimpleGauge(20, 240, 0, 100, 50);
        setGaugeData(gauge);
        RootPanel.get().add(gauge);
        return gauge;
    }

    private Gauge<Integer> addSimpleFillArcGauge() {
        final Gauge<Integer> gauge2 = new SimpleFillArcGauge(0, 100, 50);
        setGaugeData(gauge2);
        RootPanel.get().add(gauge2);
        return gauge2;
    }

    private void setGaugeData(final Gauge<Integer> gauge) {
        gauge.setSize(400);
        gauge.setBorderEnabled(true);
        gauge.setBorderWidth(2);
        gauge.setAnimationDuration(250);
        gauge.setValueFormat(NumberFormat.getFormat("0"));
        gauge.setMinorTicksSizeInPercentOfSize(2);
        gauge.setMajorTicksSizeInPercentOfSize(4);
        gauge.setMajorTicks(6);
        gauge.setMinorTicks(5);
        gauge.setTicksEnabled(true);
        gauge.setGaugeColor(CssColor.make("darkred"));
        gauge.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(0).setMaxValue(50).setColor(CssColor.make("green")).build());
        gauge.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(50).setMaxValue(75).setColor(CssColor.make("yellow")).build());
        gauge.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(75).setMaxValue(100).setColor(CssColor.make("red")).build());
        gauge.setBackgroundColor(CssColor.make("lightgrey"));
    }

    
}
