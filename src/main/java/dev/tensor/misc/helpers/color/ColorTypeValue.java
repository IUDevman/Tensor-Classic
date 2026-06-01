package dev.tensor.misc.helpers.color;

/**
 * @author Hoosiers
 * @since 05-29-2026
 */

public final class ColorTypeValue {

    public final Type type;
    public final int value;

    public ColorTypeValue(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    public enum Type {
        RED,
        GREEN,
        BLUE
    }
}
