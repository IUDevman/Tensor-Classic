package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public final class FOVModifierEvent extends EventVariableCancellable<FOVModifierEvent> {

    public float FOV;

    @Override
    public FOVModifierEvent withVariables(Object... args) {
        this.FOV = (float) args[0];

        return super.withVariables(args);
    }
}
