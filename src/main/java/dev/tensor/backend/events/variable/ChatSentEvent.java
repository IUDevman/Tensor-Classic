package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class ChatSentEvent extends EventVariableCancellable<ChatSentEvent> {

    public String ARGS;

    @Override
    public ChatSentEvent withVariables(Object... args) {
        this.ARGS = (String) args[0];

        return super.withVariables(args);
    }
}
