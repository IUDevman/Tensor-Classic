package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.helpers.SpammerList;
import dev.tensor.misc.imp.setting.Setting;

/**
 * @author Hoosiers
 * @since 07-04-2026
 */

public class SpammerListSetting implements Setting<SpammerList> {

    private final String NAME;

    private final SpammerList LIST;

    private final String DESCRIPTION;

    public SpammerListSetting(String NAME, SpammerList LIST, String DESCRIPTION) {
        this.NAME = NAME;
        this.LIST = LIST;

        this.LIST.setSetting(this);

        this.DESCRIPTION = DESCRIPTION;
    }

    @Override
    public String getValueTypeName() {
        return "SpamerList";
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Deprecated
    @Override
    public SpammerList getDefaultValue() {
        return null;
    }

    @Override
    public SpammerList getValue() {
        return this.LIST;
    }

    @Deprecated
    @Override
    public String getValueString(boolean color) {
        return "";
    }

    @Deprecated
    @Override
    public void setValue(SpammerList VALUE) {

    }

    @Deprecated
    @Override
    public void setValueFromString(String string) throws Exception {

    }

    @Override
    public void load(JsonElement jsonElement) {
        if (this.getValue() == null) {
            return;
        }

        if (jsonElement == null) {
            this.getValue().setCurrentSpammer(null, true);
            return;
        }

        String element = jsonElement.getAsString();

        if (element == null || element.isEmpty() || element.equalsIgnoreCase(" ") || element.equalsIgnoreCase("N$o$n$e")) {
            this.getValue().setCurrentSpammer(null, true);
        }

        this.getValue().setCurrentSpammer(jsonElement.getAsString(), false);
    }

    @Override
    public void save(JsonObject jsonObject) {
        String toSave = "N$o$n$e";

        if (this.getValue() != null) {
            String value = this.getValue().getCurrentSpammerFileName();

            if (value != null) {
                toSave = value;
            }
        }

        jsonObject.add(this.getName(), new JsonPrimitive(toSave));
    }

    @Override
    public void reset() {

    }

    public void onChange() {

    }

    @Override
    public String getDescription() {
        return this.DESCRIPTION;
    }
}
