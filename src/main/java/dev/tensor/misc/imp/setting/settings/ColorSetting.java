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
        return name;
    }

    @Override
    public Color getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public Color getValue() {
        return value;
    }

    @Override
    public void setValue(Color value) {
        this.value = value;
    }

    public void setValueFromString(String RED, String GREEN, String BLUE, String ALPHA) throws NumberFormatException {
        int RED_INT = Integer.parseInt(RED);
        int GREEN_INT = Integer.parseInt(GREEN);
        int BLUE_INT = Integer.parseInt(BLUE);

        int ALPHA_INT;

        if (ALPHA.equalsIgnoreCase("NONE")) {
            ALPHA_INT = 255;
        } else {
            ALPHA_INT = Integer.parseInt(ALPHA);
        }

        this.setValue(new Color(RED_INT, GREEN_INT, BLUE_INT, ALPHA_INT));
    }

    @Override
    public void load(JsonElement jsonElement) {
        this.setValue(new Color(jsonElement.getAsInt()));
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add(this.getName(), new JsonPrimitive(this.getValue().getRGB()));
    }
}