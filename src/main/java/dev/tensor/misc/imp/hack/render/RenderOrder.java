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
    BreadCrumbs(8),
    BreakProgress(9),
    BlockHighlight(10),
    Scaffold_or_Platform(11),
    Nuker(12),
    Search(13),

    Default(14),

    HUD_Shading_and_Images(1),
    HUD_Text(2),
    Hud_Images_Only(3),

    HUD_Default(4);

    private final int value;

    RenderOrder(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
