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

    private final WaypointRender render;

    private boolean enabled = false;

    public Waypoint(String name, int x, int y, int z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;

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

    public WaypointRender getRender() {
        return this.render;
    }
}

