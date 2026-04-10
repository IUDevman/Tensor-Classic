package dev.tensor.misc.helpers.waypoint;

import dev.tensor.misc.imp.Globals;

/**
 * @author Hoosiers
 * @since 04-07-2026
 */

public final class Waypoint implements Globals {

    private final String name;

    private final int x;
    private final int y;
    private final int z;

    private final boolean overworld;
    private final String server;

    private final WaypointRender render;

    private boolean enabled = false;

    public Waypoint(String name, int x, int y, int z, boolean overworld, String server) {

        this.name = name;
        this.x = this.clampCoord(x, -30000000, 30000000);
        this.y = this.clampCoord(y, 0, 256);
        this.z = this.clampCoord(z, -30000000, 30000000);
        this.overworld = overworld;
        this.server = server;

        this.render = new WaypointRender(this);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle() {
        this.setEnabled(!this.isEnabled());
    }

    public String getName() {
        return this.name;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public boolean isOverworld() {
        return this.overworld;
    }

    public String getServer() {
        return this.server;
    }

    public WaypointRender getRender() {
        return this.render;
    }

    private int clampCoord(int value, int min, int max) {
        if (value > max) {
            value = max;
        } else if (value < min) {
            value = min;
        }

        return value;
    }
}

