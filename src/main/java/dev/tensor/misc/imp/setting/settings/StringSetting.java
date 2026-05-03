package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.setting.Setting;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public class StringSetting implements Setting<String> {

    private final String NAME;
    private final String DEFAULT_VALUE;

    private String VALUE;

    public StringSetting(String NAME, String VALUE) {
        this.NAME = NAME;
        this.DEFAULT_VALUE = VALUE;

        this.VALUE = VALUE;
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Override
    public String getDefaultValue() {
        return this.DEFAULT_VALUE;
    }

    @Override
    public String getValue() {
        return this.VALUE;
    }

    @Override
    public String getValueString(boolean color) {
        return (color ? ChatColor.AQUA.text() : "") + this.getValue();
    }

    @Override
    public void setValue(String VALUE) {
        this.VALUE = VALUE;
    }

    @Override
    public void setValueFromString(String string) throws Exception {

        if (!string.contains(" ")) {
            string = string.replace("_", " ");
        }

        this.setValue(string);
    }

    @Override
    public void load(JsonElement jsonElement) {
        try {
            this.setValueFromString(jsonElement.getAsString());
        } catch (Exception ignored) {
            this.getTensorLogger().info("Failed to load StringSetting: " + this.getName() + "!");
        }
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add(this.getName(), new JsonPrimitive(this.getValue()));
    }
}
