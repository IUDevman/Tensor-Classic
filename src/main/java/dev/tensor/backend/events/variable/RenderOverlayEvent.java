package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

public final class RenderOverlayEvent extends EventVariableCancellable<RenderOverlayEvent> {

    public Type type;

    @Override
    public RenderOverlayEvent withVariables(Object... args) {
        this.type = (Type) args[0];

        return super.withVariables(args);
    }

    public enum Type {
        blocks,
        fire,
        portals,
        pumpkin,
        vignette,
        water,
        vomit
    }
}
