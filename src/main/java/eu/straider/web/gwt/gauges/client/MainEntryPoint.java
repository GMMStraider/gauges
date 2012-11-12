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
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
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
        addTicksOption();
        addColorOption();
        addFontOption();
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

    private void addColorOption() {
        final TextBox borderColor = new TextBox();
        Button borderColorApply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setBorderColor(CssColor.make(borderColor.getValue()));
                }
            }
        });
        borderColor.setValue("black");
        HorizontalPanel borderColorOptions = new HorizontalPanel();
        borderColorOptions.add(new Label("Border color: "));
        borderColorOptions.add(borderColor);
        borderColorOptions.add(borderColorApply);
        optionsPanel.add(borderColorOptions);
        final TextBox background = new TextBox();
        Button backgroundApply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setBackgroundColor(CssColor.make(background.getValue()));
                }
            }
        });
        background.setValue("lightgrey");
        HorizontalPanel backgroundColorOptions = new HorizontalPanel();
        backgroundColorOptions.add(new Label("Background color: "));
        backgroundColorOptions.add(background);
        backgroundColorOptions.add(backgroundApply);
        optionsPanel.add(backgroundColorOptions);
        final TextBox valueBox = new TextBox();
        Button valueColorApply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setValueColor(CssColor.make(valueBox.getValue()));
                }
            }
        });
        valueBox.setValue("black");
        HorizontalPanel valueColorOptions = new HorizontalPanel();
        valueColorOptions.add(new Label("Value color: "));
        valueColorOptions.add(valueBox);
        valueColorOptions.add(valueColorApply);
        optionsPanel.add(valueColorOptions);
        final TextBox captionBox = new TextBox();
        Button captionColorApply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setCaptionColor(CssColor.make(captionBox.getValue()));
                }
            }
        });
        captionBox.setValue("black");
        HorizontalPanel captionColorOptions = new HorizontalPanel();
        captionColorOptions.add(new Label("Caption color: "));
        captionColorOptions.add(captionBox);
        captionColorOptions.add(captionColorApply);
        optionsPanel.add(captionColorOptions);
    }

    private void addTicksOption() {
        final CheckBox valueCheck = new CheckBox("Value Text enabled");
        valueCheck.setValue(true);
        valueCheck.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setGaugeTextEnabled(valueCheck.getValue());
                    gauge.setValue(gauge.getValue(), false);
                }
            }
        });
        optionsPanel.add(valueCheck);
        final CheckBox captionCheck = new CheckBox("Caption enabled");
        captionCheck.setValue(true);
        captionCheck.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setCaptionEnabled(captionCheck.getValue());
                    gauge.setValue(gauge.getValue(), false);
                }
            }
        });
        optionsPanel.add(captionCheck);
        final CheckBox borderCheck = new CheckBox("Border enabled");
        borderCheck.setValue(true);
        borderCheck.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setBorderEnabled(borderCheck.getValue());
                    gauge.setValue(gauge.getValue(), false);
                }
            }
        });
        optionsPanel.add(borderCheck);
        final CheckBox backgroundCheck = new CheckBox("Background enabled");
        backgroundCheck.setValue(true);
        backgroundCheck.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setBackgroundColorEnabled(backgroundCheck.getValue());
                    gauge.setValue(gauge.getValue(), false);
                }
            }
        });
        optionsPanel.add(backgroundCheck);
        final CheckBox ticks = new CheckBox("Ticks enabled");
        ticks.setValue(true);
        ticks.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setTicksEnabled(ticks.getValue());
                    gauge.setValue(gauge.getValue(), false);
                }
            }
        });
        optionsPanel.add(ticks);
        final IntegerBox majorTicks = new IntegerBox();
        Button majorTicksApply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setMajorTicks(majorTicks.getValue());
                    gauge.setValue(gauge.getValue(), false);
                }
            }
        });
        majorTicks.setValue(11);
        final IntegerBox minorTicks = new IntegerBox();
        Button minorTicksApply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setMinorTicks(minorTicks.getValue());
                }
            }
        });
        minorTicks.setValue(4);
        HorizontalPanel majorTicksOptions = new HorizontalPanel();
        majorTicksOptions.add(new Label("Major Ticks: "));
        majorTicksOptions.add(majorTicks);
        majorTicksOptions.add(majorTicksApply);
        optionsPanel.add(majorTicksOptions);
        HorizontalPanel minorTicksOption = new HorizontalPanel();
        minorTicksOption.add(new Label("Minor Ticks: "));
        minorTicksOption.add(minorTicks);
        minorTicksOption.add(minorTicksApply);
        optionsPanel.add(minorTicksOption);
        
        final DoubleBox minorTicksSize = new DoubleBox();
        Button minorTicksSizeApply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setMinorTicksSizeInPercentOfSize(minorTicksSize.getValue());
                }
            }
        });
        minorTicksSize.setValue(2.0);
        HorizontalPanel minorTicksSizeOptions = new HorizontalPanel();
        minorTicksSizeOptions.add(new Label("Minor Ticks Size: "));
        minorTicksSizeOptions.add(minorTicksSize);
        minorTicksSizeOptions.add(minorTicksSizeApply);
        optionsPanel.add(minorTicksSizeOptions);
        final DoubleBox majorTicksSize = new DoubleBox();
        Button majorTicksSizeApply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setMajorTicksSizeInPercentOfSize(majorTicksSize.getValue());
                }
            }
        });
        majorTicksSize.setValue(4.0);
        HorizontalPanel majorTicksSizeOptions = new HorizontalPanel();
        majorTicksSizeOptions.add(new Label("Major Ticks Size: "));
        majorTicksSizeOptions.add(majorTicksSize);
        majorTicksSizeOptions.add(majorTicksSizeApply);
        optionsPanel.add(majorTicksSizeOptions);
        
        final TextBox ticksColor = new TextBox();
        Button ticksColorApply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setTickColor(CssColor.make(ticksColor.getValue()));
                }
            }
        });
        ticksColor.setValue("black");
        HorizontalPanel ticksColorOptions = new HorizontalPanel();
        ticksColorOptions.add(new Label("Ticks color: "));
        ticksColorOptions.add(ticksColor);
        ticksColorOptions.add(ticksColorApply);
        optionsPanel.add(ticksColorOptions);
    }

    private void addFontOption() {
        final TextBox valueBox = new TextBox();
        Button valueFontApply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setValueFont(valueBox.getValue());
                }
            }
        });
        valueBox.setValue("bold 32px Lucida Console");
        HorizontalPanel valueFontOptions = new HorizontalPanel();
        valueFontOptions.add(new Label("Value font: "));
        valueFontOptions.add(valueBox);
        valueFontOptions.add(valueFontApply);
        optionsPanel.add(valueFontOptions);
        final TextBox captionBox = new TextBox();
        Button captionFontApply = new Button("Apply", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Gauge<Integer> gauge : gauges.values()) {
                    gauge.setCaptionFont(captionBox.getValue());
                }
            }
        });
        captionBox.setValue("bold 14px Helvetica");
        HorizontalPanel captionFontOptions = new HorizontalPanel();
        captionFontOptions.add(new Label("Caption font: "));
        captionFontOptions.add(captionBox);
        captionFontOptions.add(captionFontApply);
        optionsPanel.add(captionFontOptions);
    }
}
