package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.BlockBreakEvent;
import dev.tensor.backend.mixins.accessors.ItemToolAccessor;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.helpers.block.BlockList;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import net.minecraft.src.*;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

@Hack.Info(name = "AutoTool", category = Category.Player)
public final class AutoTool extends Hack {

    public final BooleanSetting swapBack = new BooleanSetting("Swap Back", false) {

        @Override
        public void setValue(Boolean value) {
            super.setValue(value);

            if (this.failsNullCheck()) {
                return;
            }

            AutoTool autoTool = this.getHackManager().getHack(AutoTool.class);

            if (autoTool != null && autoTool.isEnabled()) {
                autoTool.resetPrevSlot();
            }
        }
    };

    private final BlockList INSTANT_BLOCKS = new BlockList(BlockList.BREAK_INSTANT());

    private boolean swapped = false;
    private int prevItem = -1;
    private int status = 0;

    private int ticksSince = 0;

    @Override
    public void onEnable(boolean isNull) {
        this.resetPrevSlot();
    }

    @Override
    public void onDisable(boolean isNull) {
        this.resetPrevSlot();
    }

    @Override
    public void onClientTick() {

        if (this.status != 2) {
            this.ticksSince++;
            return;
        }

        if (this.ticksSince < 1) {
            this.ticksSince++;
            return;
        }

        this.ticksSince = 0;

        if (!this.swapBack.getValue()) {
            return;
        }

        if (this.swapped && this.prevItem != -1 && this.getInventory().currentItem != this.prevItem) {

            this.getInventory().currentItem = this.prevItem;
            this.resetPrevSlot();
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Packet14BlockDig packet = event.packet;

        if (packet.status == 2) {
            this.status = 2;
            return;
        }

        int blockId = this.getWorld().getBlockId(packet.xPosition, packet.yPosition, packet.zPosition);

        if (blockId == 0) {
            return;
        }

        Block block = Block.blocksList[blockId];

        if (block == Block.bedrock) {
            return;
        }

        if (this.INSTANT_BLOCKS.containsBlock(block)) {
            return;
        }

        int swapSlot = this.getInventory().currentItem;
        float currentStrength = 0;

        ItemStack currentItem = this.getInventory().getStackInSlot(swapSlot);

        if (currentItem != null) {
            if (currentItem.getItem() == Item.flintAndSteel && blockId == Block.tnt.blockID) {
                return;
            }

            if (currentItem.getItem() instanceof ItemTool) {
                currentStrength = currentItem.getItem().getStrVsBlock(currentItem, block);
            } else {
                currentStrength = currentItem.getStrVsBlock(block);
            }
        }

        int oldSlot = swapSlot;

        for (int i = 0; i < 9; i++) {

            ItemStack itemStack = this.getInventory().getStackInSlot(i);

            if (itemStack == null) {
                continue;
            }

            float strength;

            if (itemStack.getItem() instanceof ItemTool) {
                strength = itemStack.getItem().getStrVsBlock(itemStack, block);
            } else {
                strength = itemStack.getStrVsBlock(block);
            }

            if (itemStack.getItem() instanceof ItemPickaxe && this.isBlockOnBullshitList(blockId)) {
                ItemTool tool = (ItemTool) itemStack.getItem();

                strength = 10 + ((ItemToolAccessor) tool).tensor_getToolMaterial().getHarvestLevel();
            }

            if (strength > currentStrength) {
                currentStrength = strength;
                swapSlot = i;
            }
        }

        if (swapSlot != oldSlot) {
            this.ticksSince = 0;
            this.swapped = true;
            this.prevItem = oldSlot;
            this.getInventory().currentItem = swapSlot;
        }
    }

    private boolean isBlockOnBullshitList(int blockID) {
        if (blockID == Block.obsidian.blockID) {
            return true;
        } else if (blockID == Block.brick.blockID) {
            return true;
        } else if (blockID == Block.mobSpawner.blockID) {
            return true;
        } else if (blockID == Block.oreRedstone.blockID) {
            return true;
        } else return blockID == Block.oreRedstoneGlowing.blockID;
    }

    public void resetPrevSlot() {
        this.status = 0;
        this.ticksSince = 0;
        this.swapped = false;
        this.prevItem = -1;
    }
}
