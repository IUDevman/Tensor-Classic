package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.EventCancellable;

/**
 * @author Hoosiers
 * @since 03-21-2026
 */

public final class ChatReceivedEvent extends EventCancellable {

    public String message;

    public ChatReceivedEvent(String message) {
        this.message = message;
    }
}
