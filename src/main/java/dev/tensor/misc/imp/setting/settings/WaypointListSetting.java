package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.tensor.misc.helpers.waypoint.WaypointList;
import dev.tensor.misc.imp.setting.Setting;

/**
 * @author Hoosiers
 * @since 04-08-2026
 */

public class WaypointListSetting implements Setting<WaypointList> {

    private final String NAME;

    private final WaypointList VALUE;

    private final String UNIQUE;

    public WaypointListSetting(String NAME, WaypointList VALUE, String UNIQUE) {
        this.NAME = NAME;
        this.VALUE = VALUE;
        this.UNIQUE = UNIQUE;
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Deprecated
    @Override
    public WaypointList getDefaultValue() {
        return null;
    }

    @Override
    public WaypointList getValue() {
        return this.VALUE;
    }

    @Deprecated
    @Override
    public String getValueString(boolean color) {
        return "";
    }

    @Deprecated
    @Override
    public void setValue(WaypointList VALUE) {

    }

    @Deprecated
    @Override
    public void setValueFromString(String string) throws Exception {

    }

    @Override
    public void load(JsonElement jsonElement) {
        try {
            this.VALUE.loadWaypoints(UNIQUE);
        } catch (Exception e) {
            this.getTensorLogger().info("Failed to load waypoint list for setting : " + this.getName());
        }
    }

    @Override
    public void save(JsonObject jsonObject) {
        try {
            this.VALUE.saveWaypoints(UNIQUE);
        } catch (Exception e) {
            this.getTensorLogger().info("Failed to save waypoint list for setting : " + this.getName());
        }
    }

    @Override
    public void reset() {
        /* removed reset 04-08-26 */
    }

    @Override
    public boolean hasCustomConfig() {
        return true;
    }
}
