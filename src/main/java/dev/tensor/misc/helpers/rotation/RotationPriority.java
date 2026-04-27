package dev.tensor.misc.helpers.rotation;

/**
 * @author Hoosiers
 * @since 04-22-2026
 */

public enum RotationPriority {

    Platform(1),
    Scaffold(2),
    Nuker(3),
    KillAura(4);

    private final int value;

    RotationPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
