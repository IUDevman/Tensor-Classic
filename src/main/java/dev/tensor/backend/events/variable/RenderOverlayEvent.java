package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

public final class RenderOverlayEvent extends EventVariableCancellable<RenderOverlayEvent> {

    public Type type;

    @Override
    public RenderOverlayEvent withVariables(Object... variables) {
        this.type = (Type) variables[0];

        return super.withVariables(variables);
    }

    public enum Type {
        BLOCK,
        FIRE,
        PORTAL,
        PUMPKIN,
        VIGNETTE,
        WATER,
        VOMIT
    }
}
