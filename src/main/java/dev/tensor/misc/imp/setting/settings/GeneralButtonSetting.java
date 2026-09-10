package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.tensor.misc.imp.setting.Setting;

/**
 * @author Hoosiers
 * @since 05-20-2026
 */

public class GeneralButtonSetting implements Setting<GeneralButtonSetting.ButtonType> {

    private final String NAME;
    private final ButtonType TYPE;

    public GeneralButtonSetting(String NAME, ButtonType TYPE) {
        this.NAME = NAME;
        this.TYPE = TYPE;
    }

    @Override
    public String getValueTypeName() {
        return "Button";
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Override
    public ButtonType getValue() {
        return this.TYPE;
    }

    @Deprecated
    @Override
    public ButtonType getDefaultValue() {
        return null;
    }

    @Deprecated
    @Override
    public String getValueString(boolean color) {
        return "";
    }

    @Deprecated
    @Override
    public void setValue(ButtonType VALUE) {

    }

    @Deprecated
    @Override
    public void setValueFromString(String string) throws Exception {

    }

    @Deprecated
    @Override
    public void load(JsonElement jsonElement) {

    }

    @Deprecated
    @Override
    public void save(JsonObject jsonObject) {

    }

    @Deprecated
    @Override
    public boolean hasCustomConfig() {
        return true;
    }

    public void onChange() {

    }

    public enum ButtonType {
        ACCOUNTS,
        CLICK_ACTION
    }
}
