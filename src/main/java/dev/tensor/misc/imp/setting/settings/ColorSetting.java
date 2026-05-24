package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.setting.Setting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-15-2021
 */

public class ColorSetting implements Setting<Color> {

    private final String name;
    private final Color defaultValue;
    private Color value;

    public ColorSetting(String name, Color value) {
        this.name = name;
        this.defaultValue = value;
        this.value = value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Color getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public Color getValue() {
        return this.value;
    }

    @Override
    public String getValueString(boolean color) {
        return (color ? ChatColor.AQUA.text() : "") + "[" + this.getValue().getRed() + ", " + this.getValue().getGreen() + ", " + this.getValue().getBlue() + ", " + this.getValue().getAlpha() + "]";
    }

    @Override
    public void setValue(Color value) {
        this.value = value;
    }

    @Override
    public void setValueFromString(String string) throws Exception {
        String[] values = string.split("_");

        if (values.length < 4) {
            throw new Exception();
        }

        this.setValuesFromString(values[0], values[1], values[2], values[3]);
    }

    private void setValuesFromString(String RED, String GREEN, String BLUE, String ALPHA) throws NumberFormatException {
        int RED_INT = Integer.parseInt(RED);
        int GREEN_INT = Integer.parseInt(GREEN);
        int BLUE_INT = Integer.parseInt(BLUE);
        int ALPHA_INT = Integer.parseInt(ALPHA);

        this.setValue(new Color(RED_INT, GREEN_INT, BLUE_INT, ALPHA_INT));
    }

    @Override
    public void load(JsonElement jsonElement) {
        try {
            this.setValueFromString(jsonElement.getAsString());
        } catch (Exception ignored) {
            this.getTensorLogger().info("Failed to load ColorSetting: " + this.getName() + "!");
        }
    }

    @Override
    public void save(JsonObject jsonObject) {
        String value = this.getValue().getRed() + "_" + this.getValue().getGreen() + "_" + this.getValue().getBlue() + "_" + this.getValue().getAlpha();

        jsonObject.add(this.getName(), new JsonPrimitive(value));
    }
}