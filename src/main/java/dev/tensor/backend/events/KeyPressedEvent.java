package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.Event;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class KeyPressedEvent implements Event {

    public final int KEY_CODE;

    public KeyPressedEvent(int KEY_CODE) {
        this.KEY_CODE = KEY_CODE;
    }
}
