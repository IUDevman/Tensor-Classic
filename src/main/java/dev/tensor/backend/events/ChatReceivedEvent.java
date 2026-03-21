package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.Event;

/**
 * @author Hoosiers
 * @since 03-21-2026
 */

public final class ChatReceivedEvent implements Event {

    public final String message;

    public ChatReceivedEvent(String message) {
        this.message = message;
    }
}
