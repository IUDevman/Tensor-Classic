package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.block.BlockList;
import dev.tensor.misc.helpers.block.BlockPos;
import dev.tensor.misc.helpers.rotation.RotationPriority;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.*;
import net.minecraft.src.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 04-19-2026
 */

@Hack.Info(name = "Platform", category = Category.Player)
public final class Platform extends Render3DHack {

    @Override
    public int getRenderOrder() {
        return RenderOrder.SCAFFOLD_PLATFORM;
    }

    public final EnumSetting mode = new EnumSetting("Mode", Mode.Instant);
    public final IntegerSetting delay = new IntegerSetting("Tick Delay", 2, 1, 20);
    public final EnumSetting bptMode = new EnumSetting("Blocks Per Tick", BPTMode.All);
    public final IntegerSetting customBPT = new IntegerSetting("Custom BPT", 5, 2, 50);
    public final DoubleSetting range = new DoubleSetting("Range", 4.0, 1.0, 10.0, 1);
    public final IntegerSetting depth = new IntegerSetting("Depth", 1, 1, 3);
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

    private int delayTicks = 0;

    private final ArrayList<BlockPos> blocksToPlace = new ArrayList<>();

    @Override
    public void onEnable(boolean isNull) {
        this.delayTicks = this.delay.getValue();
    }

    @Override
    public void onDisable(boolean isNull) {
        this.blocksToPlace.clear();
    }

    @Override
    public void onClientTick() {
        this.blocksToPlace.clear();

        double playerX = this.getPlayer().posX;
        double playerY = this.getPlayer().posY;
        double playerZ = this.getPlayer().posZ;

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        if (freecam != null && freecam.isEnabled()) {

            if (this.freecamMode.getValue().equals(Freecam.Stop)) {
                return;
            }

            if (this.freecamMode.getValue().equals(Freecam.Player)) {
                playerX = freecam.oldX;
                playerY = freecam.oldY;
                playerZ = freecam.oldZ;
            }
        }

        ArrayList<BlockPos> blocksToPlaceQueue = this.getBlocksToPlace(playerX, playerY, playerZ);

        this.blocksToPlace.addAll(blocksToPlaceQueue);

        if (this.mode.getValue().equals(Mode.Delay) && this.delayTicks < this.delay.getValue()) {
            this.delayTicks++;
            return;
        } else {
            this.delayTicks = 0;
        }

        final double finalPlayerX = playerX;
        final double finalPlayerY = playerY;
        final double finalPlayerZ = playerZ;
        this.blocksToPlace.sort(Comparator.comparing(blockCoords -> this.getDistance(blockCoords.x - finalPlayerX, blockCoords.y - finalPlayerY, blockCoords.z - finalPlayerZ)));

        int blocksToPlace = 1;

        if (this.bptMode.getValue().equals(BPTMode.All)) {
            blocksToPlace = this.blocksToPlace.size();
        } else if (this.bptMode.getValue().equals(BPTMode.Custom)) {
            blocksToPlace = this.customBPT.getValue() + 1;
        }

        if (blocksToPlace > this.blocksToPlace.size()) {
            blocksToPlace = this.blocksToPlace.size();
        }

        for (int i = 0; i < blocksToPlace; i++) {
            this.placeBlock(this.blocksToPlace.get(i));
        }
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        if (!this.esp.getValue()) {
            return;
        }

        this.blocksToPlace.forEach(this::renderForBlockPos);
    }

    private ArrayList<BlockPos> getBlocksToPlace(double playerX, double playerY, double playerZ) {
        ArrayList<BlockPos> blockPos = new ArrayList<>();

        BlockPos playerCoords = new BlockPos(MathHelper.floor_double(playerX), MathHelper.floor_double(playerY) - 2, MathHelper.floor_double(playerZ));

        for (int x = (int) (playerCoords.x - this.range.getValue()); x <= playerCoords.x + this.range.getValue(); x++) {

            for (int y = playerCoords.y - this.depth.getValue() + 1; y <= playerCoords.y; y++) {

                for (int z = (int) (playerCoords.z - this.range.getValue()); z <= playerCoords.z + this.range.getValue(); z++) {

                    double distance = this.getDistance(x - playerX, y - playerY, z - playerZ);

                    if (distance > this.range.getValue()) {
                        continue;
                    }

                    int blockID = this.getWorld().getBlockId(x, y, z);

                    if (blockID == 0) {
                        blockPos.add(new BlockPos(x, y, z));
                        continue;
                    }

                    if (this.airOnly.getValue()) {
                        continue;
                    }

                    if (blockID == Block.waterMoving.blockID || blockID == Block.waterStill.blockID || blockID == Block.lavaMoving.blockID || blockID == Block.lavaStill.blockID || blockID == Block.fire.blockID || blockID == Block.snow.blockID) {
                        blockPos.add(new BlockPos(x, y, z));
                    }
                }
            }
        }

        return blockPos;
    }

    private void placeBlock(BlockPos coords) {
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

        int[][] directions = this.getPlaceDirections(coords.x, coords.y, coords.z);

        for (int[] direction : directions) {
            int dirX = direction[0];
            int dirY = direction[1];
            int dirZ = direction[2];
            int dirSide = direction[3];

            if (!this.getWorld().canBlockBePlacedAt(block.blockID, dirX, dirY, dirZ, true, dirSide)) {
                this.getMinecraft().playerController.sendPlaceBlock(this.getPlayer(), this.getWorld(), itemStack, dirX, dirY, dirZ, dirSide);

                if (this.rotate.getValue()) {
                    this.getRotationManager().addToRotationQueue(this.pitchAndYawTo(dirX, dirY, dirZ), RotationPriority.PLATFORM);
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

    private void renderForBlockPos(BlockPos blockPos) {
        double rx = blockPos.x - RenderManager.renderPosX;
        double ry = blockPos.y - RenderManager.renderPosY;
        double rz = blockPos.z - RenderManager.renderPosZ;

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

    private enum Mode {
        Delay,
        Instant
    }

    private enum BPTMode {
        Single,
        All,
        Custom
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
