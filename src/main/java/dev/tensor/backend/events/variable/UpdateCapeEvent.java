package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 05-02-2026
 */

public final class UpdateCapeEvent extends EventVariableCancellable {

    public String username;
    public String playerCloakUrl;

    @Override
    public <T extends EventVariable> T withVariables(Object... args) {
        this.username = (String) args[0];
        this.playerCloakUrl = null;

        return super.withVariables(args);
    }
}
