package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

public final class RenderOverlayEvent extends EventVariableCancellable {

    public Type type;

    @Override
    public <T extends EventVariable> T withVariables(Object... args) {
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
