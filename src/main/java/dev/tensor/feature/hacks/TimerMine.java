package dev.tensor.feature.hacks;

import dev.tensor.backend.events.BlockBreakEvent;
import dev.tensor.backend.mixins.accessors.MinecraftAccessor;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

@Hack.Info(name = "TimerMine", category = Category.Player)
public final class TimerMine extends Hack {

    public final IntegerSetting ticksPerSecond = new IntegerSetting("Ticks Per Sec.", 35, 20, 40);

    public final IntegerSetting shutOffAfter = new IntegerSetting("Shutoff Ticks", 20, 1, 80);

    @Override
    public void onDisable(boolean isNull) {
        ((MinecraftAccessor) this.getMinecraft()).getTimer().ticksPerSecond = this.getDefaultTPS();
    }

    private int ticksSinceBreakStart = 0;

    @Override
    public void onClientTick() {
        this.ticksSinceBreakStart++;

        if (this.ticksSinceBreakStart >= this.shutOffAfter.getValue()) {
            ((MinecraftAccessor) this.getMinecraft()).getTimer().ticksPerSecond = this.getDefaultTPS();
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onBlockBreakEvent(BlockBreakEvent event) {

        int status = event.packet.status;

        if (status == 0) {
            ((MinecraftAccessor) this.getMinecraft()).getTimer().ticksPerSecond = this.ticksPerSecond.getValue();
        } else {
            ((MinecraftAccessor) this.getMinecraft()).getTimer().ticksPerSecond = this.getDefaultTPS();
        }

        this.ticksSinceBreakStart = 0;
    }

    private float getDefaultTPS() {
        Timer timer = this.getHackManager().getHack(Timer.class);

        if (timer.isEnabled()) {
            return timer.ticksPerSecond.getValue();
        }

        return 20.0F;
    }
}
