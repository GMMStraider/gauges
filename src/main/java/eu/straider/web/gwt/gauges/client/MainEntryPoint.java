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
import eu.straider.web.gwt.gauges.client.components.SimpleFillArcGauge;
import eu.straider.web.gwt.gauges.client.components.SimpleGauge;

/**
 * Main entry point.
 */
public class MainEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
        final Gauge<Integer> gauge = new SimpleGauge(20, 240, 0, 100, 50);
        gauge.setSize(400);
        gauge.setBorderEnabled(true);
        gauge.setBorderWidth(2);
        gauge.setAnimationDuration(250);
        gauge.setValueFormat(NumberFormat.getFormat("0"));
        gauge.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(0).setMaxValue(50).setColor(CssColor.make("green")).build());
        gauge.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(50).setMaxValue(75).setColor(CssColor.make("yellow")).build());
        gauge.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(75).setMaxValue(100).setColor(CssColor.make("red")).build());
        gauge.setBackgroundColor(CssColor.make("lightgrey"));
//        gauge.addStyleName("gauge");
        RootPanel.get().add(gauge);
        final Gauge<Integer> gauge2 = new SimpleFillArcGauge(0, 100, 50);
        gauge2.setSize(400);
        gauge2.setBorderEnabled(true);
        gauge2.setBorderWidth(2);
        gauge2.setAnimationDuration(250);
        gauge2.setValueFormat(NumberFormat.getFormat("0"));
        gauge2.setMinorTicksSizeInPercentOfSize(2);
        gauge2.setMajorTicksSizeInPercentOfSize(4);
        gauge2.setMajorTicks(6);
        gauge2.setMinorTicks(5);
        gauge2.setTicksEnabled(true);
        gauge2.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(0).setMaxValue(50).setColor(CssColor.make("green")).build());
        gauge2.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(50).setMaxValue(75).setColor(CssColor.make("yellow")).build());
        gauge2.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(75).setMaxValue(100).setColor(CssColor.make("red")).build());
        gauge2.setBackgroundColor(CssColor.make("lightgrey"));
        RootPanel.get().add(gauge2);
        final IntegerBox box = new IntegerBox();
        RootPanel.get().add(box);
        Button apply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                gauge.setValue(box.getValue());
                gauge2.setValue(box.getValue());
            }
        });
        RootPanel.get().add(apply);
    }

    
}
