package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.setting.Setting;

import java.util.Arrays;

/**
 * @author Hoosiers
 * @since 05-20-2021
 */

public class EnumSetting implements Setting<Enum<?>> {

    private final String NAME;
    private final Enum<?> DEFAULT_VALUE;

    private Enum<?> VALUE;

    public EnumSetting(String NAME, Enum<?> VALUE) {
        this.NAME = NAME;
        this.DEFAULT_VALUE = VALUE;

        this.VALUE = VALUE;
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Override
    public Enum<?> getDefaultValue() {
        return this.DEFAULT_VALUE;
    }

    @Override
    public Enum<?> getValue() {
        return this.VALUE;
    }

    @Override
    public String getValueString(boolean color) {
        return (color ? ChatColor.AQUA.text() : "") + this.getValue().name();
    }

    @Override
    public void setValue(Enum<?> VALUE) {
        this.VALUE = VALUE;
    }

    @Override
    public void setValueFromString(String string) throws Exception {
        Enum<?>[] array = this.getValue().getDeclaringClass().getEnumConstants();

        Arrays.stream(array).forEach(anEnum -> {
            if (anEnum.name().equalsIgnoreCase(string)) {
                this.setValue(anEnum);
            }
        });
    }

    public Enum<?>[] getEnumConstants() {
        return this.getValue().getDeclaringClass().getEnumConstants();
    }

    public void increment() {
        Enum<?>[] array = getValue().getDeclaringClass().getEnumConstants();
        int index = getValue().ordinal() + 1;

        if (index >= array.length) index = 0;

        this.setValue(array[index]);
    }

    @Override
    public void load(JsonElement jsonElement) {
        try {
            this.setValueFromString(jsonElement.getAsString());
        } catch (Exception ignored) {
            this.getTensorLogger().info("Failed to load EnumSetting: " + this.getName() + "!");
        }
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add(this.getName(), new JsonPrimitive(this.getValue().toString()));
    }
}
