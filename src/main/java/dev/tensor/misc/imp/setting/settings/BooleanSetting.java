package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.setting.Setting;

/**
 * @author Hoosiers
 * @since 04-12-2021
 */

public class BooleanSetting implements Setting<Boolean> {

    private final String NAME;
    private final boolean DEFAULT_VALUE;

    private final String DESCRIPTION;

    private boolean VALUE;

    public BooleanSetting(String NAME, boolean VALUE, String DESCRIPTION) {
        this.NAME = NAME;
        this.DEFAULT_VALUE = VALUE;

        this.VALUE = VALUE;

        this.DESCRIPTION = DESCRIPTION;
    }

    @Override
    public String getValueTypeName() {
        return "Boolean";
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Override
    public Boolean getDefaultValue() {
        return this.DEFAULT_VALUE;
    }

    @Override
    public Boolean getValue() {
        return this.VALUE;
    }

    @Override
    public String getValueString(boolean color) {
        return (color ? (this.getValue() ? ChatColor.GREEN.text() : ChatColor.RED.text()) : "") + this.getValue().toString();
    }

    @Override
    public void setValue(Boolean VALUE) {
        this.VALUE = VALUE;
    }

    @Override
    public void setValueFromString(String string) throws Exception {
        boolean VALUE = Boolean.parseBoolean(string);

        this.setValue(VALUE);
    }

    @Override
    public void load(JsonElement jsonElement) {
        this.setValue(jsonElement.getAsBoolean());
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add(this.getName(), new JsonPrimitive(this.getValue().toString()));
    }

    @Override
    public String getDescription() {
        return this.DESCRIPTION;
    }
}
