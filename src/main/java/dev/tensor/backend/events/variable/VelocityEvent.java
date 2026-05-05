package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 04-02-2026
 */

public final class VelocityEvent extends EventVariableCancellable<VelocityEvent> {

    public Type type;

    @Override
    public VelocityEvent withVariables(Object... args) {
        this.type = (Type) args[0];

        return super.withVariables(args);
    }

    public enum Type {
        Block,
        Entity,
        Fluid,
        SoulSand
    }
}
