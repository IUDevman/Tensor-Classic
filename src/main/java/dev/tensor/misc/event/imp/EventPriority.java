package dev.tensor.misc.event.imp;

/**
 * @author Hoosiers
 * @author DarkMagician6
 * @since 08-03-2013
 */

public enum EventPriority {

    Highest(0),
    High(1),
    Medium(2),
    Low(3),
    Lowest(4);

    public static final byte[] STANDARD_VALUES_ARRAY = new byte[]{
            Highest.getValue(),
            High.getValue(),
            Medium.getValue(),
            Low.getValue(),
            Lowest.getValue()
    };

    private final byte priority;

    EventPriority(int priority) {
        this.priority = (byte) priority;
    }

    public byte getValue() {
        return this.priority;
    }
}
