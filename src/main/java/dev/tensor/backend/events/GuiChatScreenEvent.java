package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.Event;

/**
 * @author Hoosiers
 * @since 03-15-2026
 */

public final class GuiChatScreenEvent implements Event {

    public final String message;
    public final int minX;
    public final int maxX;
    public final int minY;
    public final int maxY;

    public GuiChatScreenEvent(String message, int minX, int maxX, int minY, int maxY) {
        this.message = message;

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
}
