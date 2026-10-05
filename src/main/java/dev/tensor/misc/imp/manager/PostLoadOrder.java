package dev.tensor.misc.imp.manager;

/**
 * @author Hoosiers
 * @since 09-23-2026
 */

public enum PostLoadOrder {

    DEFAULT(0),
    HACK_MANAGER(1),
    DEEPL_MANAGER(2);

    private final int value;

    PostLoadOrder(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
