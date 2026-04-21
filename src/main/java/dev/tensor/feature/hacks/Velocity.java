package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.VelocityEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;

/**
 * @author Hoosiers
 * @since 04-02-2026
 */

@Hack.Info(name = "Velocity", category = Category.Movement)
public final class Velocity extends Hack {

    public final EnumSetting mode = new EnumSetting("Mode", Mode.Custom);
    public final BooleanSetting block = new BooleanSetting("Block", true);
    public final BooleanSetting entity = new BooleanSetting("Entity", true);
    public final BooleanSetting fluid = new BooleanSetting("Fluid", false);
    public final BooleanSetting soulSand = new BooleanSetting("Soul Sand", false);

    @SuppressWarnings("unused")
    @EventTarget
    public void onVelocityEvent(VelocityEvent event) {
        if (this.mode.getValue().equals(Mode.All)) {
            event.cancel();
            return;
        }

        if (this.block.getValue() && event.type.equals(VelocityEvent.Type.Block)) {
            event.cancel();
            return;
        }

        if (this.entity.getValue() && event.type.equals(VelocityEvent.Type.Entity)) {
            event.cancel();
            return;
        }

        if (this.fluid.getValue() && event.type.equals(VelocityEvent.Type.Fluid)) {
            event.cancel();
            return;
        }

        if (this.soulSand.getValue() && event.type.equals(VelocityEvent.Type.SoulSand)) {
            event.cancel();
        }
    }

    private enum Mode {
        All,
        Custom
    }
}
