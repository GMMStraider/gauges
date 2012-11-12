package eu.straider.web.gwt.gauges.client;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import eu.straider.web.gwt.gauges.client.components.MarkerGauge;
import eu.straider.web.gwt.gauges.client.components.SimpleFillArcGauge;
import eu.straider.web.gwt.gauges.client.components.SimpleGauge;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main entry point.
 */
public class MainEntryPoint implements EntryPoint {

    private FlowPanel gaugePanel = new FlowPanel();
    private VerticalPanel optionsPanel = new VerticalPanel();
    private Map<String, Gauge<Integer>> gauges = new HashMap<String, Gauge<Integer>>();
    private Map<String, Label> gaugesLabel = new HashMap<String, Label>();

    @Override
    public void onModuleLoad() {
        DockPanel dock = new DockPanel();
        dock.add(optionsPanel, DockPanel.WEST);
        dock.add(gaugePanel, DockPanel.CENTER);
        gauges.put("SimpleGauge", addSimpleGauge());
        gauges.put("SimpleFillArcGauge", addSimpleFillArcGauge());
        gauges.put("MarkerGauge", addMarkerGauge());
        addValueBox();
        addComboBoxes();
        RootPanel.get().add(dock);
    }

    private Gauge<Integer> addMarkerGauge() {
        VerticalPanel vertPanel = new VerticalPanel();
        final Gauge<Integer> gauge = new MarkerGauge(0, 100, 50);
        setGaugeData(gauge);
        Label label = new Label("MarkerGauge");
        gaugesLabel.put("MarkerGauge", label);
        vertPanel.add(label);
        vertPanel.add(gauge);
        gaugePanel.add(vertPanel);
        return gauge;
    }

    private Gauge<Integer> addSimpleGauge() {
        VerticalPanel vertPanel = new VerticalPanel();
        final Gauge<Integer> gauge = new SimpleGauge(20, 240, 0, 100, 50);
        setGaugeData(gauge);
        gauge.setCaption("SimpleGauge");
        gauge.setCaptionFont("bold 14px Helvetica");
        Label label = new Label("SimpleGauge");
        gaugesLabel.put("SimpleGauge", label);
        vertPanel.add(label);
        vertPanel.add(gauge);
        gaugePanel.add(vertPanel);
        label.setVisible(false);
        gauge.setVisible(false);
        return gauge;
    }

    private Gauge<Integer> addSimpleFillArcGauge() {
        VerticalPanel vertPanel = new VerticalPanel();
        final Gauge<Integer> gauge = new SimpleFillArcGauge(0, 100, 50);
        setGaugeData(gauge);
        gauge.setCaption("SimpleFillArcGauge");
        gauge.setCaptionFont("bold 14px Helvetica");
        Label label = new Label("SimpleFillArcGauge");
        gaugesLabel.put("SimpleFillArcGauge", label);
        vertPanel.add(label);
        vertPanel.add(gauge);
        gaugePanel.add(vertPanel);
        label.setVisible(false);
        gauge.setVisible(false);
        return gauge;
    }

    private void setGaugeData(final Gauge<Integer> gauge) {
        gauge.setSize(400);
        gauge.setBorderEnabled(true);
        gauge.setBorderWidth(2);
        gauge.setAnimationDuration(250);
        gauge.setValueFormat(NumberFormat.getFormat("0"));
        gauge.setMinorTicksSizeInPercentOfSize(2);
        gauge.setMajorTicksSizeInPercentOfSize(4);
        gauge.setMajorTicks(11);
        gauge.setMinorTicks(4);
        gauge.setTicksEnabled(true);
        gauge.setGaugeColor(CssColor.make("darkred"));
//        gauge.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(-20).setMaxValue(0).setColor(CssColor.make("blue")).build());
        gauge.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(0).setMaxValue(40).setColor(CssColor.make("green")).build());
        gauge.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(40).setMaxValue(75).setColor(CssColor.make("yellow")).build());
        gauge.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(75).setMaxValue(90).setColor(CssColor.make("orange")).build());
        gauge.addColorRange(new ColorRangeBuilder<Integer>().setMinValue(90).setMaxValue(100).setColor(CssColor.make("red")).build());
        gauge.setBackgroundColor(CssColor.make("lightgrey"));
    }

    private void addValueBox() {
        final IntegerBox box = new IntegerBox();
        Button apply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setValue(box.getValue());
                }
            }
        });
        box.setValue(50);
        HorizontalPanel valuePanel = new HorizontalPanel();
        valuePanel.add(new Label("Value: "));
        valuePanel.add(box);
        valuePanel.add(apply);
        optionsPanel.add(valuePanel);
    }

    private void addComboBoxes() {
        addVisibilityOptions();
        optionsPanel.add(new Label("Gauge Options"));
        addAnimationOption();
        addSizeOption();
    }

    private void addVisibilityOptions() {
        optionsPanel.add(new Label("Visible Gauges"));
        final CheckBox simpleGaugeCheckBox = new CheckBox("SimpleGauge");
        simpleGaugeCheckBox.setValue(gauges.get("SimpleGauge").isVisible());
        simpleGaugeCheckBox.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                gauges.get("SimpleGauge").setVisible(simpleGaugeCheckBox.getValue());
                gaugesLabel.get("SimpleGauge").setVisible(simpleGaugeCheckBox.getValue());
            }
        });
        optionsPanel.add(simpleGaugeCheckBox);
        final CheckBox simpleFillArcGaugeCheckBox = new CheckBox("SimpleFillArcGauge");
        simpleFillArcGaugeCheckBox.setValue(gauges.get("SimpleFillArcGauge").isVisible());
        simpleFillArcGaugeCheckBox.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                gauges.get("SimpleFillArcGauge").setVisible(simpleFillArcGaugeCheckBox.getValue());
                gaugesLabel.get("SimpleFillArcGauge").setVisible(simpleFillArcGaugeCheckBox.getValue());
            }
        });
        optionsPanel.add(simpleFillArcGaugeCheckBox);
        final CheckBox markerGaugeCheckBox = new CheckBox("MarkerGauge");
        markerGaugeCheckBox.setValue(gauges.get("MarkerGauge").isVisible());
        markerGaugeCheckBox.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                gauges.get("MarkerGauge").setVisible(markerGaugeCheckBox.getValue());
                gaugesLabel.get("MarkerGauge").setVisible(markerGaugeCheckBox.getValue());
            }
        });
        optionsPanel.add(markerGaugeCheckBox);
    }

    private void addAnimationOption() {
        final CheckBox animation = new CheckBox("Animation enabled");
        animation.setValue(true);
        animation.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setAnimationEnabled(animation.getValue());
                }
            }
        });
        optionsPanel.add(animation);
        final IntegerBox box = new IntegerBox();
        Button apply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setAnimationDuration(box.getValue());
                }
            }
        });
        box.setValue(200);
        HorizontalPanel animationDuration = new HorizontalPanel();
        animationDuration.add(new Label("AnimationDuration: "));
        animationDuration.add(box);
        animationDuration.add(apply);
        optionsPanel.add(animationDuration);
    }
    private void addSizeOption() {
        final IntegerBox box = new IntegerBox();
        Button apply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setSize(box.getValue());
                }
            }
        });
        box.setValue(400);
        HorizontalPanel size = new HorizontalPanel();
        size.add(new Label("Gauge Size: "));
        size.add(box);
        size.add(apply);
        optionsPanel.add(size);
    }
}
