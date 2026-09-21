package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.setting.NumberSetting;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public class DoubleSetting implements NumberSetting<Double> {

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
    public String getValueTypeName() {
        return "Double";
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
    public String getValueString(boolean color) {
        return (color ? ChatColor.AQUA.text() : "") + this.getFormattedFromValue(this.getValue());
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
    public void setValueFromString(String string) throws Exception {
        double VALUE = Double.parseDouble(string);

        this.setValue(VALUE);
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

    @SuppressWarnings("MalformedFormatString")
    public String getFormattedFromValue(double VALUE) {
        return String.format("%." + this.getDecimal() + "f", VALUE);
    }

    @Override
    public void load(JsonElement jsonElement) {
        try {
            this.setValueFromString(jsonElement.getAsString());
        } catch (Exception ignored) {
            this.getTensorLogger().log("Failed to load DoubleSetting: " + this.getName() + " for hack: " + this.getHackNameForSetting() + "!", TensorLogger.Level.ERROR);
        }
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add(this.getName(), new JsonPrimitive(this.getValue().toString()));
    }

    @Override
    public String getDescription() {
        return "Penis " + this.getName();
    }
}
