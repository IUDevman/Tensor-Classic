package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 04-11-2026
 */

public final class UpdateClientPlayerActionEvent extends EventVariableCancellable<UpdateClientPlayerActionEvent> {

    public boolean shouldUpdateInventory = false;

    @Override
    public UpdateClientPlayerActionEvent withVariables(Object... variables) {
        this.shouldUpdateInventory = (boolean) variables[0];

        return super.withVariables(variables);
    }
}
