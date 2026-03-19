package dev.tensor.misc.imp.setting.settings;

import dev.tensor.misc.imp.setting.NumberSetting;
import dev.tensor.misc.imp.setting.Setting;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public class DoubleSetting implements Setting<Double>, NumberSetting<Double> {

    private final String NAME;
    private final int DECIMAL;
    private final double MIN_VALUE;
    private final double MAX_VALUE;
    private final double DEFAULT_VALUE;

    private double VALUE;

    public DoubleSetting(String NAME, double VALUE, double MIN_VALUE, double MAX_VALUE, int DECIMAL) {
        this.NAME = NAME;
        this.DECIMAL = DECIMAL;
        this.MIN_VALUE = MIN_VALUE;
        this.MAX_VALUE = MAX_VALUE;

        this.setValue(VALUE);

        this.DEFAULT_VALUE = this.VALUE;
    }


    @Override
    public String getName() {
        return this.NAME;
    }

    @Override
    public Double getDefaultValue() {
        return this.DEFAULT_VALUE;
    }

    @Override
    public Double getValue() {
        return this.VALUE;
    }

    @Override
    public void setValue(Double VALUE) throws NumberFormatException {
        if (VALUE < this.getMin()) {
            VALUE = this.getMin();
        } else if (VALUE > this.getMax()) {
            VALUE = this.getMax();
        }

        String FORMATTED_VALUE = this.getFormattedFromValue(VALUE);

        this.VALUE = Double.parseDouble(FORMATTED_VALUE);

    }

    @Override
    public Double getMin() {
        return this.MIN_VALUE;
    }

    @Override
    public Double getMax() {
        return this.MAX_VALUE;
    }

    public int getDecimal() {
        return this.DECIMAL;
    }

    public String getFormattedValue() {
        return this.getFormattedFromValue(this.getValue());
    }

    @SuppressWarnings("MalformedFormatString")
    private String getFormattedFromValue(double VALUE) {
        return String.format("%." + this.getDecimal() + "f", VALUE);
    }
}
