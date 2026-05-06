package dev.tensor.misc.event.imp;

/**
 * @author Hoosiers
 * @since 04-28-2026
 */

public enum EventPriority {

    Highest(0),
    High(1),
    Medium(2),
    Low(3),
    Lowest(4);

    private final byte priority;

    EventPriority(int priority) {
        this.priority = (byte) priority;
    }

    public byte value() {
        return this.priority;
    }
}
