package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.RenderOverlayEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

@Hack.Info(name = "NoOverlay", category = Category.Render)
public final class NoOverlay extends Hack {

    public final EnumSetting mode = new EnumSetting("Mode", Mode.Custom);
    public final BooleanSetting blocks = new BooleanSetting("Blocks", false);
    public final BooleanSetting fire = new BooleanSetting("Fire", false);
    public final BooleanSetting portals = new BooleanSetting("Portals", true);
    public final BooleanSetting portalVomit = new BooleanSetting("Portal Spin", true);
    public final BooleanSetting pumpkin = new BooleanSetting("Pumpkin", true);
    public final BooleanSetting vignette = new BooleanSetting("Vignette", false);
    public final BooleanSetting water = new BooleanSetting("Water", false);

    @SuppressWarnings("unused")
    private final EventAction<RenderOverlayEvent> renderOverlayEventAction = this.addEventAction(new EventAction<>(RenderOverlayEvent.class, event -> {
        if (this.mode.getValue().equals(Mode.All)) {
            event.cancel();
            return;
        }

        if (blocks.getValue() && event.type.equals(RenderOverlayEvent.Type.BLOCK)) {
            event.cancel();
            return;
        }

        if (fire.getValue() && event.type.equals(RenderOverlayEvent.Type.FIRE)) {
            event.cancel();
            return;
        }

        if (portals.getValue() && event.type.equals(RenderOverlayEvent.Type.PORTAL)) {
            event.cancel();
            return;
        }

        if (portalVomit.getValue() && event.type.equals(RenderOverlayEvent.Type.VOMIT)) {
            event.cancel();
            return;
        }

        if (pumpkin.getValue() && event.type.equals(RenderOverlayEvent.Type.PUMPKIN)) {
            event.cancel();
            return;
        }

        if (vignette.getValue() && event.type.equals(RenderOverlayEvent.Type.VIGNETTE)) {
            event.cancel();
        }

        if (water.getValue() && event.type.equals(RenderOverlayEvent.Type.WATER)) {
            event.cancel();
        }
    }));

    private enum Mode {
        All,
        Custom
    }
}
