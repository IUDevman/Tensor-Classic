package dev.tensor.misc.imp.hack.render;

/**
 * @author Hoosiers
 * @since 04-06-2026
 */

public enum RenderOrder {
    Tracers(1),
    Nametags(2),
    WolfOwner(3),
    LogoutSpots(4),
    Waypoints(5),
    KillAura(6),
    EntityESP(7),
    BreakProgress(8),
    BlockHighlight(9),
    Scaffold_or_Platform(10),
    Nuker(11),
    Search(12),

    Default(130),

    HUD_Shading_and_Images(1),
    HUD_Text(2),
    Hud_Images_Only(3),

    HUD_Default(4);

    private final int value;

    RenderOrder(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
