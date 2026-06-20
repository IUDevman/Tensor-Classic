package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-01-2026
 */

public final class RenderSkyEvent extends EventVariableCancellable<RenderSkyEvent> {

    public Type type;

    private Color newColor;
    private boolean updated;

    @Override
    public RenderSkyEvent withVariables(Object... variables) {
        this.type = (Type) variables[0];

        this.newColor = new Color(255, 255, 255, 255);
        this.updated = false;

        return super.withVariables(variables);
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
        CLOUDS,
        FOG_SKY,
        FOG_FLUIDS
    }
}
