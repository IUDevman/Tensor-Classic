package dev.tensor.misc.imp.hack.render;

/**
 * @author Hoosiers
 * @since 04-06-2026
 */

public enum RenderOrder {
    Tracers(10),
    Nametags(20),
    WolfOwner(30),
    Waypoints(40),
    KillAura(50),
    EntityESP(60),
    BreakProgress(70),
    BlockHighlight(80),
    Scaffold_or_Platform(90),
    Nuker(100),
    Search(110),

    Default(120),

    HUD_Shading_and_Images(10),
    HUD_Text(20),
    Hud_Images_Only(30),

    HUD_Default(40);

    private final int value;

    RenderOrder(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
