package dev.tensor.misc.helpers.rotation;

/**
 * @author Hoosiers
 * @since 04-22-2026
 */

public enum RotationPriority {
    Platform(10),
    Scaffold(20),
    Nuker(30),
    AntiFireball(35),
    KillAura(40);

    public static final int PLUGIN_PRIORITY_DEFAULT = 25;

    private final int value;

    RotationPriority(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
