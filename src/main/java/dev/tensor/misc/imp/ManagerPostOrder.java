package dev.tensor.misc.imp;

/**
 * @author Hoosiers
 * @since 09-23-2026
 */

public enum ManagerPostOrder {

    DEFAULT(0),
    HACK_MANAGER(1),
    DEEPL_MANAGER(2);

    private final int value;

    ManagerPostOrder(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
