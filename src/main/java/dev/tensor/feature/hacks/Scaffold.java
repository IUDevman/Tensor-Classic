package dev.tensor.feature.hacks;

import dev.tensor.backend.events.Packet10FlyingEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.helpers.block.BlockList;
import dev.tensor.misc.helpers.block.BlockPos;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BlockListSetting;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 04-17-2026
 */

@Hack.Info(name = "Scaffold", category = Category.Player)
public final class Scaffold extends Hack {

    public final BooleanSetting airOnly = new BooleanSetting("Air Only", false);
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    public final BooleanSetting swing = new BooleanSetting("Swing", true);
    public final EnumSetting swap = new EnumSetting("Swap To", Swap.First);
    public final IntegerSetting swapDefined = new IntegerSetting("Defined Slot", 1, 0, 8);
    public final BlockListSetting swapList = new BlockListSetting("Filter Item List", new BlockList(BlockList.CUSTOM()));
    public final BlockListSetting ignoreList = new BlockListSetting("Ignore Item List", new BlockList(BlockList.SCAFFOLD_BLOCKLIST()));
    public final BooleanSetting swapBack = new BooleanSetting("Swap Back", true);

    private boolean rotated = false;
    private float pitch = 0;
    private float yaw = 0;

    @Override
    public void onDisable(boolean isNull) {
        this.rotated = false;
        this.pitch = 0;
        this.yaw = 0;
    }

    @Override
    public void onClientTick() {
        BlockPos coords = new BlockPos(MathHelper.floor_double(this.getPlayer().posX), MathHelper.floor_double(this.getPlayer().posY) - 2, MathHelper.floor_double(this.getPlayer().posZ));

        int blockID = this.getWorld().getBlockId(coords.x, coords.y, coords.z);

        if (blockID == 0) {
            this.placeBlock(coords);
            return;
        }

        if (this.airOnly.getValue()) {
            return;
        }

        if (blockID == Block.waterMoving.blockID || blockID == Block.waterStill.blockID || blockID == Block.lavaMoving.blockID || blockID == Block.lavaStill.blockID || blockID == Block.fire.blockID || blockID == Block.snow.blockID) {
            this.placeBlock(coords);
        }
    }

    @SuppressWarnings("unused")
    @EventTarget()
    public void onPacket10FlyingEvent(Packet10FlyingEvent event) {
        if (this.rotate.getValue() && this.rotated) {
            event.packet.pitch = this.pitch;
            event.packet.yaw = this.yaw;
            this.pitch = 0;
            this.yaw = 0;
            this.rotated = false;
        }
    }

    private void placeBlock(BlockPos coords) {

        int prevItem = this.getInventory().currentItem;
        int swapSlot = this.getSwapSlot();
        boolean swapped = false;

        if (swapSlot != -1 && swapSlot != prevItem) {
            this.getTensorLogger().info("swapped");
            this.getInventory().currentItem = swapSlot;
            swapped = true;
        }

        ItemStack itemStack = this.getInventory().getCurrentItem();

        if (itemStack == null) {
            return;
        }

        if (!(itemStack.getItem() instanceof ItemBlock)) {
            return;
        }

        Block block = Block.blocksList[itemStack.itemID];

        if (block == null) {
            return;
        }

        if (this.ignoreList.containsBlock(block)) {
            return;
        }

        int[][] directions = this.getDirections(coords.x, coords.y, coords.z);

        for (int[] direction : directions) {
            int dirX = direction[0];
            int dirY = direction[1];
            int dirZ = direction[2];
            int dirSide = direction[3];

            if (!this.getWorld().canBlockBePlacedAt(block.blockID, dirX, dirY, dirZ, true, dirSide)) {
                this.getMinecraft().playerController.sendPlaceBlock(this.getPlayer(), this.getWorld(), itemStack, dirX, dirY, dirZ, dirSide);

                if (this.rotate.getValue()) {
                    float[] pitchAndYaw = this.pitchAndYawTo(dirX, dirY, dirZ);

                    this.pitch = pitchAndYaw[0];
                    this.yaw = pitchAndYaw[1];
                    this.rotated = true;
                }

                if (this.swing.getValue()) {
                    this.getPlayer().swingItem();
                }

                if (swapped && this.swapBack.getValue()) {
                    this.getInventory().currentItem = prevItem;
                }

                return;
            }
        }
    }

    private int getSwapSlot() {
        switch ((Swap) this.swap.getValue()) {
            case None:
                return -1;
            case First:
                AtomicInteger swapSlot = new AtomicInteger(-1);

                for (int i = 0; i < 9; i++) {
                    ItemStack itemStack = this.getInventory().getStackInSlot(i);

                    if (itemStack == null) {
                        continue;
                    }

                    if (!(itemStack.getItem() instanceof ItemBlock)) {
                        continue;
                    }

                    Block block = Block.blocksList[itemStack.itemID];

                    if (block == null) {
                        continue;
                    }

                    if (this.ignoreList.containsBlock(block)) {
                        continue;
                    }

                    if (swapSlot.get() == -1) {
                        swapSlot.set(i);
                        break;
                    }
                }

                return swapSlot.get();
            case Defined:
                ItemStack itemStack = this.getInventory().getStackInSlot(this.swapDefined.getValue());

                if (itemStack == null) {
                    return -1;
                }

                if (!(itemStack.getItem() instanceof ItemBlock)) {
                    return -1;
                }

                Block block = Block.blocksList[itemStack.itemID];

                if (block == null) {
                    return -1;
                }

                if (this.ignoreList.containsBlock(block)) {
                    return -1;
                }

                return this.swapDefined.getValue();
            case Filter:
                AtomicInteger swapSlot1 = new AtomicInteger(-1);

                for (int i = 0; i < 9; i++) {
                    ItemStack itemStack1 = this.getInventory().getStackInSlot(i);

                    if (itemStack1 == null) {
                        continue;
                    }

                    if (!(itemStack1.getItem() instanceof ItemBlock)) {
                        continue;
                    }

                    Block block1 = Block.blocksList[itemStack1.itemID];

                    if (block1 == null) {
                        continue;
                    }

                    if (this.ignoreList.containsBlock(block1)) {
                        continue;
                    }

                    if (!this.swapList.containsBlock(block1)) {
                        continue;
                    }

                    if (swapSlot1.get() == -1) {
                        swapSlot1.set(i);
                        break;
                    }
                }

                return swapSlot1.get();
        }

        return -1;
    }

    private int[][] getDirections(int startX, int startY, int startZ) {
        return new int[][]{
                {startX, startY + 1, startZ, 0},
                {startX, startY - 1, startZ, 1},
                {startX, startY, startZ + 1, 2},
                {startX, startY, startZ - 1, 3},
                {startX + 1, startY, startZ, 4},
                {startX - 1, startY, startZ, 5}
        };
    }

    private enum Swap {
        None,
        First,
        Defined,
        Filter
    }
}
