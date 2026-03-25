package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.EventCancellable;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class ChatSentEvent extends EventCancellable {

    public String ARGS;

    public ChatSentEvent(String ARGS) {
        this.ARGS = ARGS;
    }
}
