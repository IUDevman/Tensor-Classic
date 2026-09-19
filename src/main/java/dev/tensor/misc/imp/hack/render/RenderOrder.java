package dev.tensor.misc.imp.hack.render;

/**
 * @author Hoosiers
 * @since 04-06-2026
 */

public enum RenderOrder {
    Tracers(10),
    Nametags(20),
    EpsteinESP(25),
    WolfOwner(30),
    LogoutSpots(40),
    Waypoints(50),
    KillAura(60),
    EntityESP(70),
    BreadCrumbs(80),
    BreakProgress(90),
    BlockHighlight(100),
    Scaffold_or_Platform(110),
    Nuker(120),
    Search(130),
    SlimeChunks(140),
    NewChunks(150),

    Default(160),

    HUD_Shading_and_Images(10),
    HUD_Text(20),
    Hud_Images_Only(30),

    HUD_Default(40);

    public static final int DEFAULT_3D = 150;
    public static final int DEFAULT_2D = 40;

    private final int value;

    RenderOrder(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
