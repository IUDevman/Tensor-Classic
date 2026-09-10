package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.color.SmallColorPickerObject;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.helpers.color.RainbowColor;
import dev.tensor.misc.imp.setting.Setting;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 04-15-2021
 */

public class ColorSetting implements Setting<Color> {

    private final String name;
    private final Color defaultValue;
    private Color value;

    private boolean rainbow = false;

    private final ArrayList<SmallColorPickerObject> smallColorPickerObjects = new ArrayList<>();

    public ColorSetting(String name, Color value) {
        this.name = name;
        this.defaultValue = value;
        this.value = value;
    }

    public ColorSetting(String name, Color value, boolean rainbow) {
        this.name = name;
        this.defaultValue = value;
        this.value = value;
        this.rainbow = rainbow;
    }

    @Override
    public String getValueTypeName() {
        return "Color";
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

    public boolean isRainbow() {
        return this.rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public Color getValueRainbow() {
        if (!this.isRainbow()) {
            return this.getValue();
        }

        AtomicInteger[] rainbow = RainbowColor.getRainbow();

        return new Color(rainbow[0].get(), rainbow[1].get(), rainbow[2].get(), this.value.getAlpha());
    }

    @Override
    public String getValueString(boolean color) {
        return (color ? ChatColor.AQUA.text() : "") + (this.rainbow ? "[Rainbow]" : "[" + this.getValue().getRed() + ", " + this.getValue().getGreen() + ", " + this.getValue().getBlue() + ", " + this.getValue().getAlpha() + "]");
    }

    @Override
    public void setValue(Color value) {
        this.value = value;

        this.smallColorPickerObjects.forEach(SmallColorPickerObject::updateDefaultColor);
    }

    public void setValueStealth(Color value) {
        this.value = value;
    }

    public void addSmallColorPickerObjectToQueue(SmallColorPickerObject smallColorPickerObject) {
        this.smallColorPickerObjects.add(smallColorPickerObject);
    }

    @Override
    public void setValueFromString(String string) throws Exception {
        String[] values = string.split("_");

        if (values.length < 5) {
            if (values.length == 1 && values[0].equalsIgnoreCase("rainbow")) {
                this.rainbow = !this.rainbow;
                return;

            } else if (values.length < 4) {
                throw new Exception();

            } else {
                this.setValuesFromString(values[0], values[1], values[2], values[3], "false");
                return;
            }
        }

        this.setValuesFromString(values[0], values[1], values[2], values[3], values[4]);
    }

    private void setValuesFromString(String RED, String GREEN, String BLUE, String ALPHA, String RAINBOW) throws NumberFormatException {
        int RED_INT = Integer.parseInt(RED);
        int GREEN_INT = Integer.parseInt(GREEN);
        int BLUE_INT = Integer.parseInt(BLUE);
        int ALPHA_INT = Integer.parseInt(ALPHA);

        this.setValue(new Color(RED_INT, GREEN_INT, BLUE_INT, ALPHA_INT));
        this.rainbow = Boolean.parseBoolean(RAINBOW);
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
        String value = this.getValue().getRed() + "_" + this.getValue().getGreen() + "_" + this.getValue().getBlue() + "_" + this.getValue().getAlpha() + "_" + this.rainbow;

        jsonObject.add(this.getName(), new JsonPrimitive(value));
    }

    @Override
    public void reset() {
        this.rainbow = false;
        Setting.super.reset();
    }
}