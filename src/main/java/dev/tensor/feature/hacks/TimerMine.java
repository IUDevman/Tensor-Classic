package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.BlockBreakEvent;
import dev.tensor.backend.mixins.accessors.MinecraftAccessor;
import dev.tensor.misc.helpers.block.BlockList;
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

    private final BlockList INSTANT_BLOCKS = new BlockList(BlockList.BREAK_INSTANT());
    private final BlockList BLOCKED_BLOCKS = new BlockList(BlockList.NUKER_BLOCKLIST());

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull) {
            ((MinecraftAccessor) this.getMinecraft()).tensor_getTimer().ticksPerSecond = this.getDefaultTPS();
        }
    }

    private int ticksSinceBreakStart = 0;

    @Override
    public void onClientTick() {
        this.ticksSinceBreakStart++;

        if (this.ticksSinceBreakStart >= this.shutOffAfter.getValue()) {
            ((MinecraftAccessor) this.getMinecraft()).tensor_getTimer().ticksPerSecond = this.getDefaultTPS();
        }
    }

    @SuppressWarnings("unused")
    public void onBlockBreakEvent(BlockBreakEvent event) {
        int status = event.packet.status;

        if (status == 0) {
            int blockID = this.getWorld().getBlockId(event.packet.xPosition, event.packet.yPosition, event.packet.zPosition);

            if (this.INSTANT_BLOCKS.containsBlock(blockID) || this.BLOCKED_BLOCKS.containsBlock(blockID)) {
                return;
            }

            ((MinecraftAccessor) this.getMinecraft()).tensor_getTimer().ticksPerSecond = this.ticksPerSecond.getValue();
        } else {
            ((MinecraftAccessor) this.getMinecraft()).tensor_getTimer().ticksPerSecond = this.getDefaultTPS();
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
