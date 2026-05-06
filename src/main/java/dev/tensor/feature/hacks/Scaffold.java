package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.MovementClipEvent;
import dev.tensor.misc.helpers.block.BlockList;
import dev.tensor.misc.helpers.block.BlockPos;
import dev.tensor.misc.helpers.rotation.RotationPriority;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.*;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 04-17-2026
 */

@Hack.Info(name = "Scaffold", category = Category.Player)
public final class Scaffold extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.Scaffold_or_Platform;
    }

    public final BooleanSetting levitate = new BooleanSetting("Levitate", false);
    public final BooleanSetting safeWalk = new BooleanSetting("Safe Walk", true);
    public final EnumSetting freecamMode = new EnumSetting("Freecam", Freecam.Player);
    public final BooleanSetting airOnly = new BooleanSetting("Air Only", false);
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    public final BooleanSetting swing = new BooleanSetting("Swing", true);
    public final EnumSetting swap = new EnumSetting("Swap To", Swap.First);
    public final IntegerSetting swapDefined = new IntegerSetting("Defined Slot", 1, 0, 8);
    public final BlockListSetting swapList = new BlockListSetting("Filter Item List", new BlockList(BlockList.CUSTOM()));
    public final BlockListSetting ignoreList = new BlockListSetting("Ignore Item List", new BlockList(BlockList.SCAFFOLD_BLOCKLIST()));
    public final BooleanSetting swapBack = new BooleanSetting("Swap Back", true);
    public final BooleanSetting esp = new BooleanSetting("ESP", true);
    public final EnumSetting renderMode = new EnumSetting("ESP Mode", RenderMode.Fill);
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5);
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 50, 0, 255);
    public final ColorSetting esp_color = new ColorSetting("ESP Color", new Color(255, 0, 0, 255));

    private BlockPos blockPlacingAt = null;

    @Override
    public void onDisable(boolean isNull) {
        this.blockPlacingAt = null;
    }

    @Override
    public void onClientTick() {
        double playerX = this.getPlayer().posX;
        double playerY = this.getPlayer().posY;
        double playerZ = this.getPlayer().posZ;

        boolean shouldLevitate = false;

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        if (freecam != null && freecam.isEnabled()) {

            if (this.freecamMode.getValue().equals(Freecam.Stop)) {
                this.blockPlacingAt = null;
                return;
            }

            if (this.freecamMode.getValue().equals(Freecam.Player)) {
                playerX = freecam.oldX;
                playerY = freecam.oldY;
                playerZ = freecam.oldZ;
            }

        } else if (this.levitate.getValue() && this.getMinecraft().currentScreen == null && Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindJump.keyCode)) {
            shouldLevitate = true;
        }

        BlockPos coords = new BlockPos(MathHelper.floor_double(playerX), MathHelper.floor_double(playerY) - 2, MathHelper.floor_double(playerZ));

        int blockID = this.getWorld().getBlockId(coords.x, coords.y, coords.z);

        if (blockID == 0) {
            this.placeBlock(coords, shouldLevitate);
            return;
        }

        if (this.airOnly.getValue()) {
            this.blockPlacingAt = null;
            return;
        }

        if (blockID == Block.waterMoving.blockID || blockID == Block.waterStill.blockID || blockID == Block.lavaMoving.blockID || blockID == Block.lavaStill.blockID || blockID == Block.fire.blockID || blockID == Block.snow.blockID) {
            this.placeBlock(coords, shouldLevitate);
        }

        this.blockPlacingAt = null;
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        if (!this.esp.getValue()) {
            return;
        }

        if (this.blockPlacingAt == null) {
            return;
        }

        double rx = this.blockPlacingAt.x - RenderManager.renderPosX;
        double ry = this.blockPlacingAt.y - RenderManager.renderPosY;
        double rz = this.blockPlacingAt.z - RenderManager.renderPosZ;

        double mx = 1 + rx;
        double my = 1 + ry;
        double mz = 1 + rz;

        Color color = this.esp_color.getValue();

        switch ((RenderMode) this.renderMode.getValue()) {
            case Outline:
                this.drawOutlineBox(rx, ry, rz, mx, my, mz, this.lineWidth.getValue(), color);
                break;
            case Fill:
                this.drawFillBox(rx, ry, rz, mx, my, mz, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                break;
            case Both:
                this.drawFillBox(rx, ry, rz, mx, my, mz, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                this.drawOutlineBox(rx, ry, rz, mx, my, mz, this.lineWidth.getValue(), color);
                break;
        }
    }

    @SuppressWarnings("unused")
    public void onMovementClipEvent(MovementClipEvent event) {
        if (this.safeWalk.getValue()) {
            event.cancel();
        }
    }

    private void placeBlock(BlockPos coords, boolean shouldLevitate) {
        int prevItem = this.getInventory().currentItem;
        int swapSlot = this.getSwapSlot();
        boolean swapped = false;

        if (swapSlot != -1 && swapSlot != prevItem) {
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

        if (shouldLevitate) {
            this.getPlayer().motionY = 0.30D;
        }

        int[][] directions = this.getPlaceDirections(coords.x, coords.y, coords.z);

        for (int[] direction : directions) {
            int dirX = direction[0];
            int dirY = direction[1];
            int dirZ = direction[2];
            int dirSide = direction[3];

            if (!this.getWorld().canBlockBePlacedAt(block.blockID, dirX, dirY, dirZ, true, dirSide)) {
                this.blockPlacingAt = coords;

                this.getMinecraft().playerController.sendPlaceBlock(this.getPlayer(), this.getWorld(), itemStack, dirX, dirY, dirZ, dirSide);

                if (this.rotate.getValue()) {
                    this.getRotationManager().addToRotationQueue(this.getRotationTo(dirX, dirY, dirZ), RotationPriority.Scaffold);
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

    private enum Freecam {
        Stop,
        Player,
        Camera
    }

    private enum Swap {
        None,
        First,
        Defined,
        Filter
    }

    private enum RenderMode {
        Outline,
        Fill,
        Both
    }
}
