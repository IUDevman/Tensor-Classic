package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 03-21-2026
 */

public final class ChatReceivedEvent extends EventVariableCancellable<ChatReceivedEvent> {

    public String message;

    @Override
    public ChatReceivedEvent withVariables(Object... variables) {
        this.message = (String) variables[0];

        return super.withVariables(variables);
    }
}
