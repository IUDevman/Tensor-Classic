package dev.tensor.feature.hacks;

import dev.tensor.backend.mixins.accessors.MinecraftAccessor;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

@Hack.Info(name = "Timer", category = Category.Movement, description = "Speeds up the client tick rate.")
public final class Timer extends Hack {

    public final IntegerSetting ticksPerSecond = new IntegerSetting("Ticks Per Sec.", 25, 20, 40);

    @Override
    public void onEnable(boolean isNull) {
        ((MinecraftAccessor) this.getMinecraft()).tensor_getTimer().ticksPerSecond = this.ticksPerSecond.getValue();
    }

    @Override
    public void onDisable(boolean isNull) {
        ((MinecraftAccessor) this.getMinecraft()).tensor_getTimer().ticksPerSecond = 20.0F;
    }

    @Override
    public void onClientTick() {
        ((MinecraftAccessor) this.getMinecraft()).tensor_getTimer().ticksPerSecond = this.ticksPerSecond.getValue();
    }
}
