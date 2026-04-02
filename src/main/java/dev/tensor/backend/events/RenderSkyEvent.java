package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.EventCancellable;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-01-2026
 */

public final class RenderSkyEvent extends EventCancellable {

    public final Type type;

    private Color newColor = new Color(255, 255, 255, 255);
    private boolean updated = false;

    public RenderSkyEvent(Type type) {
        this.type = type;
    }

    public Color getNewColor() {
        return this.newColor;
    }

    public void setNewColor(Color newColor) {
        this.newColor = newColor;
        this.updated = true;
    }

    public boolean isUpdated() {
        return this.updated;
    }

    public enum Type {
        Clouds,
        Fog,
        Fog_Fluids
    }
}
