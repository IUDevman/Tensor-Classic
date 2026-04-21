package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class ChatSentEvent extends EventVariableCancellable {

    public String ARGS;

    @Override
    public <T extends EventVariable> T withVariables(Object... args) {
        this.ARGS = (String) args[0];

        return super.withVariables(args);
    }
}
