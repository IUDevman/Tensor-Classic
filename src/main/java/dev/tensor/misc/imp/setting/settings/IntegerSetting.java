package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.setting.NumberSetting;
import dev.tensor.misc.imp.setting.Setting;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public class IntegerSetting implements Setting<Integer>, NumberSetting<Integer> {

    private final String NAME;
    private final int MIN_VALUE;
    private final int MAX_VALUE;
    private final int DEFAULT_VALUE;

    private int VALUE;

    public IntegerSetting(String NAME, int VALUE, int MIN_VALUE, int MAX_VALUE) {
        this.NAME = NAME;
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
    public Integer getDefaultValue() {
        return this.DEFAULT_VALUE;
    }

    @Override
    public Integer getValue() {
        return this.VALUE;
    }

    @Override
    public String getValueString(boolean color) {
        return (color ? ChatColor.AQUA.text() : "") + this.getValue().toString();
    }

    @Override
    public void setValue(Integer VALUE) {
        if (VALUE < this.getMin()) {
            VALUE = this.getMin();
        } else if (VALUE > this.getMax()) {
            VALUE = this.getMax();
        }

        this.VALUE = VALUE;
    }

    @Override
    public void setValueFromString(String string) throws Exception {
        int VALUE = Integer.parseInt(string);

        this.setValue(VALUE);
    }

    @Override
    public Integer getMin() {
        return this.MIN_VALUE;
    }

    @Override
    public Integer getMax() {
        return this.MAX_VALUE;
    }

    @Override
    public void load(JsonElement jsonElement) {
        try {
            this.setValueFromString(jsonElement.getAsString());
        } catch (Exception ignored) {
            this.getTensorLogger().info("Failed to load IntegerSetting: " + this.getName() + "!");
        }
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add(this.getName(), new JsonPrimitive(this.getValue().toString()));
    }
}
