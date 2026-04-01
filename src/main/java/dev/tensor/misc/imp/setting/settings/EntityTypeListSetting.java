package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.entity.EntityTypeList;
import dev.tensor.misc.imp.setting.Setting;
import net.minecraft.src.Entity;

/**
 * @author Hoosiers
 * @since 03-31-2026
 */

public final class EntityTypeListSetting implements Setting<EntityTypeList> {

    private final String NAME;

    private final EntityTypeList VALUE;

    private final EntityTypeList FULL_VALUE;

    public EntityTypeListSetting(String NAME, EntityTypeList VALUE, EntityTypeList FULL_VALUE) {
        this.NAME = NAME;

        this.VALUE = VALUE;
        this.FULL_VALUE = FULL_VALUE;
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
        this.addEntity(entity.getClass().getSimpleName());
        this.hasChanged();
    }

    public void addEntity(String entity) {
        this.getValue().addToEntityList(entity);
        this.hasChanged();
    }

    public void removeEntity(Entity entity) {
        this.removeEntity(entity.getClass().getSimpleName());
        this.hasChanged();
    }

    public void removeEntity(String entity) {
        this.getValue().removeFromEntityList(entity);
        this.hasChanged();
    }

    public boolean containsEntity(Entity entity) {
        return this.containsEntity(entity.getClass().getSimpleName());
    }

    public boolean containsEntity(String entity) {
        return this.getValue().containsEntity(entity);
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
            this.getTensorLogger().info("Failed to load EntityTypeListSetting: " + this.getName() + "!");
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
        this.getValue().resetEntityList();
    }
}
