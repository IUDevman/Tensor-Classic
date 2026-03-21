package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.imp.setting.Setting;

import java.awt.*;

/**
 * @author IUDevman
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
    public void setValue(Color value) {
        this.value = value;
    }

    public void setValueFromString(String RED, String GREEN, String BLUE, String ALPHA) throws NumberFormatException {
        int RED_INT = Integer.parseInt(RED);
        int GREEN_INT = Integer.parseInt(GREEN);
        int BLUE_INT = Integer.parseInt(BLUE);
        int ALPHA_INT = Integer.parseInt(ALPHA);

        this.setValue(new Color(RED_INT, GREEN_INT, BLUE_INT, ALPHA_INT));
    }

    @Override
    public void load(JsonElement jsonElement) {
        String value = jsonElement.getAsString();

        String[] values = value.split("_");

        if (values.length < 4) {
            this.getTensorLogger().info("Failed to load ColorSetting '" + this.getName() + "' (value length < 4)!");
        }

        try {
            this.setValueFromString(values[0], values[1], values[2], values[3]);

        }catch (Exception ignored) {
            this.getTensorLogger().info("Failed to load ColorSetting '" + this.getName() + "' (Exception)!");
        }
    }

    @Override
    public void save(JsonObject jsonObject) {
        String value = this.getValue().getRed() + "_" + this.getValue().getGreen() + "_" + this.getValue().getBlue() + "_" + this.getValue().getAlpha();

        jsonObject.add(this.getName(), new JsonPrimitive(value));
    }
}