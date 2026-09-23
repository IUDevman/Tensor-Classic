package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.helpers.entity.EntityTypeList;
import dev.tensor.misc.imp.setting.Setting;
import net.minecraft.src.Entity;

/**
 * @author Hoosiers
 * @since 03-31-2026
 */

public class EntityTypeListSetting implements Setting<EntityTypeList> {

    private final String NAME;

    private final EntityTypeList VALUE;

    private final EntityTypeList FULL_VALUE;

    private final String DESCRIPTION;

    public EntityTypeListSetting(String NAME, EntityTypeList VALUE, EntityTypeList FULL_VALUE, String DESCRIPTION) {
        this.NAME = NAME;

        this.VALUE = VALUE;
        this.FULL_VALUE = FULL_VALUE;

        this.DESCRIPTION = DESCRIPTION;
    }

    @Override
    public String getValueTypeName() {
        return "EntityTypeList";
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Deprecated
    @Override
    public EntityTypeList getDefaultValue() {
        return null;
    }

    @Override
    public EntityTypeList getValue() {
        return this.VALUE;
    }

    public EntityTypeList getFullValue() {
        return this.FULL_VALUE;
    }

    @Override
    public String getValueString(boolean color) {
        return "";
    }

    @Deprecated
    @Override
    public void setValue(EntityTypeList VALUE) {

    }

    @Override
    public void setValueFromString(String string) throws Exception {
        this.getValue().loadEntityListFromString(string);
    }

    public void addEntity(Entity entity) {
        this.getValue().addToEntityList(entity);
        this.hasChanged();
    }

    public void addEntity(Class<? extends Entity> entity) {
        this.getValue().addToEntityList(entity);
        this.hasChanged();
    }

    public void addEntity(String convertedName) {
        this.getValue().addToEntityList(convertedName);
        this.hasChanged();
    }

    public void removeEntity(Entity entity) {
        this.getValue().removeFromEntityList(entity);
        this.hasChanged();
    }

    public void removeEntity(Class<? extends Entity> entity) {
        this.getValue().removeFromEntityList(entity);
        this.hasChanged();
    }

    public void removeEntity(String convertedName) {
        this.getValue().removeFromEntityList(convertedName);
    }

    public boolean containsEntity(Entity entity) {
        return this.getValue().containsEntity(entity);
    }

    public boolean containsEntity(Class<? extends Entity> entity) {
        return this.getValue().containsEntity(entity);
    }

    public boolean containsEntity(String convertedName) {
        return this.getValue().containsEntity(convertedName);
    }

    public void hasChanged() {

    }

    @Override
    public void load(JsonElement jsonElement) {
        try {
            String value = jsonElement.getAsString();

            if (!value.equalsIgnoreCase("null")) {
                this.setValueFromString(value);
            }
        } catch (Exception ignored) {
            this.getTensorLogger().log("Failed to load EntityTypeListSetting: " + this.getName() + " for hack: " + this.getHackNameForSetting() + "!", TensorLogger.Level.ERROR);
        }
    }

    @Override
    public void save(JsonObject jsonObject) {
        String value = this.getValue().saveEntityListToString();

        if (value == null || value.isEmpty() || value.equals(" ") || value.equals("_")) {
            value = "null";
        }

        jsonObject.add(this.getName(), new JsonPrimitive(value));
    }

    @Override
    public void reset() {
        /* removed reset 04-08-26 */
    }

    @Override
    public String getDescription() {
        return this.DESCRIPTION;
    }
}
