package eu.straider.web.gwt.gauges.client;

public interface GaugeAnimation<T extends Number> {

    void goToValue(T toValue, T oldValue, int milliseconds);
}
