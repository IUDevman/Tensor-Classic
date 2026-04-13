package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.Event;

/**
 * @author Hoosiers
 * @since 04-12-2026
 */

public final class MouseEvent implements Event {

    public final int dWheel;
    public final boolean eventButtonState;
    public final int eventButton;

    public MouseEvent(int dWheel, boolean eventButtonState, int eventButton) {
        this.dWheel = dWheel;
        this.eventButtonState = eventButtonState;
        this.eventButton = eventButton;
    }
}
