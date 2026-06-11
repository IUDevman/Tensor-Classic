package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 06-10-2026
 */

public final class SignCollisionEvent extends EventVariableCancellable<SignCollisionEvent> {

    public int signY;

    @Override
    public SignCollisionEvent withVariables(Object... args) {
        this.signY = (int) args[0];

        return super.withVariables(args);
    }
}
