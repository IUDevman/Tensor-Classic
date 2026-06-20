package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 05-02-2026
 */

public final class UpdateCapeEvent extends EventVariableCancellable<UpdateCapeEvent> {

    public String username;
    public String playerCloakUrl;

    @Override
    public UpdateCapeEvent withVariables(Object... variables) {
        this.username = (String) variables[0];
        this.playerCloakUrl = null;

        return super.withVariables(variables);
    }
}
