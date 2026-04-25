package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.variable.BlockBreakEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.helpers.block.BlockCoords;
import dev.tensor.misc.helpers.block.BlockList;
import dev.tensor.misc.helpers.rotation.RotationPriority;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.*;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Hoosiers
 * @since 04-15-2026
 */

@Hack.Info(name = "Nuker", category = Category.Player, bind = Keyboard.KEY_N)
public final class Nuker extends Render3DHack {

    @Override
    public int getRenderOrder() {
        return RenderOrder.NUKER;
    }

    public final EnumSetting mode = new EnumSetting("Mode", Mode.Vanilla);
    public final EnumSetting target = new EnumSetting("Target", Target.Click);
    public final BooleanSetting resetTarget = new BooleanSetting("Disable Resets Target", true);
    public final BlockListSetting customTargets = new BlockListSetting("Custom Targets", new BlockList(BlockList.CUSTOM()));
    public final BooleanSetting comparePrev = new BooleanSetting("Compare Previous", true);
    public final EnumSetting comparePrevMode = new EnumSetting("Previous Clearing Mode", ClearingMode.Advanced);
    public final BooleanSetting below = new BooleanSetting("Break Blocks Below", false);
    public final EnumSetting freecamMode = new EnumSetting("Freecam", Freecam.Player);
    public final DoubleSetting range = new DoubleSetting("Range", 4.0, 1.0, 10.0, 1);
    public final IntegerSetting delay = new IntegerSetting("Tick Delay", 4, 0, 20);
    public final EnumSetting bptMode = new EnumSetting("Blocks Per Tick", BPTMode.Single);
    public final IntegerSetting customBPT = new IntegerSetting("Custom BPT", 5, 2, 50);
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    public final BooleanSetting swing = new BooleanSetting("Swing", true);
    public final BooleanSetting esp = new BooleanSetting("ESP", true);
    public final EnumSetting renderMode = new EnumSetting("ESP Mode", RenderMode.Fill);
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5);
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 50, 0, 255);
    public final ColorSetting esp_color = new ColorSetting("ESP Color", new Color(255, 0, 0, 255));

    private final ArrayList<BlockCoords> blocksToMine = new ArrayList<>();
    private final ArrayList<BlockCoords> blocksBreakingVanilla = new ArrayList<>();
    private final ArrayList<BlockCoords> queueToRemove = new ArrayList<>();
    private final ArrayList<BlockCoords> renderBlocksToMine = new ArrayList<>();
    private final ArrayList<BlockCoords> prevBlocksMinedVanilla = new ArrayList<>();

    private final BlockList INSTANT_BLOCKS = new BlockList(BlockList.BREAK_INSTANT());
    private final BlockList BLOCKED_BLOCKS = new BlockList(BlockList.NUKER_BLOCKLIST());

    private int delayTicks = 0;

    private int customToggleID = 0;

    @Override
    public void onEnable(boolean isNull) {
        this.delayTicks = this.delay.getValue();
    }

    @Override
    public void onDisable(boolean isNull) {
        this.blocksToMine.clear();
        this.blocksBreakingVanilla.clear();
        this.queueToRemove.clear();
        this.renderBlocksToMine.clear();
        this.prevBlocksMinedVanilla.clear();

        if (this.resetTarget.getValue()) {
            this.customToggleID = 0;
        }
    }

    @Override
    public void onClientTick() {
        this.blocksToMine.clear();

        this.queueToRemove.forEach(this.blocksBreakingVanilla::remove);
        this.queueToRemove.clear();

        if (this.delayTicks >= this.delay.getValue()) {
            this.delayTicks = 0;

        } else {
            this.delayTicks++;
            return;
        }

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

        for (int x = (int) (playerX - this.range.getValue()); x <= playerX + this.range.getValue(); x++) {

            for (int y = (int) (playerY - (this.below.getValue() ? this.range.getValue() : 1)); y <= playerY + this.range.getValue(); y++) {

                for (int z = (int) (playerZ - this.range.getValue()); z <= playerZ + this.range.getValue(); z++) {

                    int blockId = this.getWorld().getBlockId(x, y, z);

                    if (blockId == 0) {
                        continue;
                    }

                    Block block = Block.blocksList[blockId];

                    if (block == null) {
                        continue;
                    }

                    if (this.isBlockOnBreakList(block)) {

                        double distance = this.getDistance(x - playerX, y - playerY, z - playerZ);

                        if (distance > this.range.getValue()) {
                            continue;
                        }

                        BlockCoords blockCoords = new BlockCoords(block, x, y, z);

                        if (this.mode.getValue().equals(Mode.Vanilla) && this.comparePrev.getValue() && !this.prevBlocksMinedVanilla.isEmpty()) {

                            AtomicBoolean hasPrev = new AtomicBoolean(false);

                            this.prevBlocksMinedVanilla.forEach(blockCoords1 -> {
                                if (!hasPrev.get() && blockCoords1.block == blockCoords.block) {
                                    if (blockCoords1.x == blockCoords.x) {
                                        if (blockCoords1.y == blockCoords.y) {
                                            if (blockCoords1.z == blockCoords.z) {
                                                hasPrev.set(true);
                                            }
                                        }
                                    }
                                }
                            });

                            if (hasPrev.get()) {
                                continue;
                            }
                        }

                        this.blocksToMine.add(blockCoords);
                    }
                }
            }
        }

        final double finalPlayerX = playerX;
        final double finalPlayerY = playerY;
        final double finalPlayerZ = playerZ;
        this.blocksToMine.sort(Comparator.comparing(blockCoords -> this.getDistance(blockCoords.x - finalPlayerX, blockCoords.y - finalPlayerY, blockCoords.z - finalPlayerZ)));

        if (!this.prevBlocksMinedVanilla.isEmpty()) {

            if (!this.comparePrev.getValue()) {
                this.prevBlocksMinedVanilla.clear();
            } else {
                switch ((ClearingMode) this.comparePrevMode.getValue()) {
                    case Lazy:
                        this.prevBlocksMinedVanilla.clear();
                        break;
                    case Advanced:
                        ArrayList<BlockCoords> queueToRemovePrev = new ArrayList<>();

                        this.prevBlocksMinedVanilla.forEach(blockCoords -> {

                            if (!this.queueToRemove.contains(blockCoords)) {
                                queueToRemovePrev.add(blockCoords);
                            }
                        });

                        this.prevBlocksMinedVanilla.removeAll(queueToRemovePrev);
                        break;
                }
            }
        }

        if (this.blocksToMine.isEmpty()) {
            this.renderBlocksToMine.clear();
            return;
        }

        if (this.mode.getValue().equals(Mode.Vanilla) && !this.blocksBreakingVanilla.isEmpty()) {
            this.renderBlocksToMine.clear();
            this.renderBlocksToMine.addAll(this.blocksToMine);
            return;
        }

        int blocksToBreak = 1;

        if (this.bptMode.getValue().equals(BPTMode.All)) {
            blocksToBreak = this.blocksToMine.size();
        } else if (this.bptMode.getValue().equals(BPTMode.Custom)) {
            blocksToBreak = this.customBPT.getValue() + 1;
        }

        if (blocksToBreak > this.blocksToMine.size()) {
            blocksToBreak = this.blocksToMine.size();
        }

        for (int i = 0; i < blocksToBreak; i++) {
            this.breakBlock(this.blocksToMine.get(i));
        }

        this.renderBlocksToMine.clear();
        this.renderBlocksToMine.addAll(this.blocksToMine);
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        if (!this.esp.getValue()) {
            return;
        }

        this.renderBlocksToMine.forEach(this::renderForBlock);

        this.blocksBreakingVanilla.forEach(blockCoords -> {

            if (!this.renderBlocksToMine.contains(blockCoords)) {
                this.renderForBlock(blockCoords);
            }
        });
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onChangeWorldEvent(ChangeWorldEvent event) {
        this.blocksToMine.clear();
        this.blocksBreakingVanilla.clear();
        this.queueToRemove.clear();
        this.renderBlocksToMine.clear();
        this.prevBlocksMinedVanilla.clear();
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (!this.target.getValue().equals(Target.Click)) {
            return;
        }

        if (event.packet.status != 0) {
            return;
        }

        int blockId = this.getWorld().getBlockId(event.packet.xPosition, event.packet.yPosition, event.packet.zPosition);

        if (this.customToggleID == blockId) {
            return;
        }

        Block block = Block.blocksList[blockId];

        if (block == null) {
            return;
        }

        if (this.BLOCKED_BLOCKS.containsBlock(block)) {
            return;
        }

        this.customToggleID = blockId;
    }

    private boolean isBlockOnBreakList(Block block) {

        if (block.blockID == 0) {
            return false;
        }

        switch ((Target) this.target.getValue()) {
            case All:
                return !this.BLOCKED_BLOCKS.containsBlock(block);
            case Instant:
                return this.INSTANT_BLOCKS.containsBlock(block);
            case Click:
                return !this.BLOCKED_BLOCKS.containsBlock(block) && this.customToggleID == block.blockID;
            case Custom:
                return !this.BLOCKED_BLOCKS.containsBlock(block) && this.customTargets.containsBlock(block);
        }

        return false;
    }

    private void breakBlock(BlockCoords blockCoords) {
        if (this.INSTANT_BLOCKS.containsBlock(blockCoords.block)) {
            this.sendPacket(new Packet14BlockDig(0, blockCoords.x, blockCoords.y, blockCoords.z, 0));
            return;
        }

        switch ((Mode) this.mode.getValue()) {
            case Vanilla:
                Thread vanilla = new Thread(() -> {

                    this.blocksBreakingVanilla.add(blockCoords);

                    float blockStrength = blockCoords.block.blockStrength(this.getPlayer());

                    if (blockStrength == 0) {
                        return;
                    }

                    double time = Math.ceil(1 / blockStrength);

                    double timeSeconds = time / 24;
                    double timeMS = timeSeconds * 1000;

                    if (this.rotate.getValue()) {
                        this.getRotationManager().addToRotationQueue(this.pitchAndYawTo(blockCoords.x, blockCoords.y, blockCoords.z), RotationPriority.NUKER);
                    }

                    this.sendPacket(new Packet14BlockDig(0, blockCoords.x, blockCoords.y, blockCoords.z, 0));

                    if (this.swing.getValue()) {
                        this.getPlayer().swingItem();
                    }

                    try {
                        Thread.sleep((long) timeMS);
                    } catch (Exception ignored) {

                    }

                    if (this.rotate.getValue()) {
                        this.getRotationManager().addToRotationQueue(this.pitchAndYawTo(blockCoords.x, blockCoords.y, blockCoords.z), RotationPriority.NUKER);
                    }

                    this.sendPacket(new Packet14BlockDig(2, blockCoords.x, blockCoords.y, blockCoords.z, 0));

                    if (this.swing.getValue()) {
                        this.getPlayer().swingItem();
                    }

                    this.prevBlocksMinedVanilla.add(blockCoords);
                    this.queueToRemove.add(blockCoords);
                });

                vanilla.start();

                break;
            case Instant:
                if (this.rotate.getValue()) {
                    this.getRotationManager().addToRotationQueue(this.pitchAndYawTo(blockCoords.x, blockCoords.y, blockCoords.z), RotationPriority.NUKER);
                }

                this.sendPacket(new Packet14BlockDig(0, blockCoords.x, blockCoords.y, blockCoords.z, 0));
                this.sendPacket(new Packet14BlockDig(2, blockCoords.x, blockCoords.y, blockCoords.z, 0));

                if (this.swing.getValue()) {
                    this.getPlayer().swingItem();
                }

                break;
        }
    }

    private void renderForBlock(BlockCoords blockCoords) {
        blockCoords.block.setBlockBoundsBasedOnState(this.getWorld(), blockCoords.x, blockCoords.y, blockCoords.z);

        double rx = blockCoords.x - RenderManager.renderPosX + blockCoords.block.minX;
        double ry = blockCoords.y - RenderManager.renderPosY + blockCoords.block.minY;
        double rz = blockCoords.z - RenderManager.renderPosZ + blockCoords.block.minZ;

        double dx = (blockCoords.block.maxX - blockCoords.block.minX);
        double dy = (blockCoords.block.maxY - blockCoords.block.minY);
        double dz = (blockCoords.block.maxZ - blockCoords.block.minZ);

        double mx = dx + rx;
        double my = dy + ry;
        double mz = dz + rz;

        if (blockCoords.block instanceof BlockLadder) {
            int metadata = this.getWorld().getBlockMetadata(blockCoords.x, blockCoords.y, blockCoords.z);

            if (metadata == 2) {
                rx = blockCoords.x - RenderManager.renderPosX;
                ry = blockCoords.y - RenderManager.renderPosY;
                rz = blockCoords.z - RenderManager.renderPosZ + 0.9D;

                dx = 1D;
                dy = 1D;
                dz = 0.1D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 3) {
                rx = blockCoords.x - RenderManager.renderPosX;
                ry = blockCoords.y - RenderManager.renderPosY;
                rz = blockCoords.z - RenderManager.renderPosZ;

                dx = 1D;
                dy = 1D;
                dz = 0.1D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 4) {
                rx = blockCoords.x - RenderManager.renderPosX + 0.9D;
                ry = blockCoords.y - RenderManager.renderPosY;
                rz = blockCoords.z - RenderManager.renderPosZ;

                dx = 0.1D;
                dy = 1D;
                dz = 1D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 5) {
                rx = blockCoords.x - RenderManager.renderPosX;
                ry = blockCoords.y - RenderManager.renderPosY;
                rz = blockCoords.z - RenderManager.renderPosZ;

                dx = 0.1D;
                dy = 1D;
                dz = 1D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else {
                rx = blockCoords.x - RenderManager.renderPosX;
                ry = blockCoords.y - RenderManager.renderPosY;
                rz = blockCoords.z - RenderManager.renderPosZ;

                dx = 1D;
                dy = 1D;
                dz = 1D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;
            }
        }

        if (blockCoords.block instanceof BlockTorch || blockCoords.block instanceof BlockRedstoneTorch) {
            int metadata = this.getWorld().getBlockMetadata(blockCoords.x, blockCoords.y, blockCoords.z);

            if (metadata == 1) {
                rx = blockCoords.x - RenderManager.renderPosX;
                ry = blockCoords.y - RenderManager.renderPosY + 0.2D;
                rz = blockCoords.z - RenderManager.renderPosZ + 0.35D;

                dx = 0.3D;
                dy = 0.6D;
                dz = 0.3D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 2) {
                rx = blockCoords.x - RenderManager.renderPosX + 0.7D;
                ry = blockCoords.y - RenderManager.renderPosY + 0.2D;
                rz = blockCoords.z - RenderManager.renderPosZ + 0.35D;

                dx = 0.3D;
                dy = 0.6D;
                dz = 0.3D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 3) {
                rx = blockCoords.x - RenderManager.renderPosX + 0.35D;
                ry = blockCoords.y - RenderManager.renderPosY + 0.2D;
                rz = blockCoords.z - RenderManager.renderPosZ;

                dx = 0.3D;
                dy = 0.6D;
                dz = 0.3D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 4) {
                rx = blockCoords.x - RenderManager.renderPosX + 0.35D;
                ry = blockCoords.y - RenderManager.renderPosY + 0.2D;
                rz = blockCoords.z - RenderManager.renderPosZ + 0.7D;

                dx = 0.3D;
                dy = 0.6D;
                dz = 0.3D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 5) {
                rx = blockCoords.x - RenderManager.renderPosX + 0.4D;
                ry = blockCoords.y - RenderManager.renderPosY;
                rz = blockCoords.z - RenderManager.renderPosZ + 0.4D;

                dx = 0.2D;
                dy = 0.6D;
                dz = 0.2D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;
            }
        }

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
        Vanilla,
        Instant
    }

    private enum Target {
        All,
        Click,
        Instant,
        Custom
    }

    private enum Freecam {
        Stop,
        Player,
        Camera
    }

    private enum BPTMode {
        Single,
        All,
        Custom
    }

    private enum RenderMode {
        Outline,
        Fill,
        Both
    }

    private enum ClearingMode {
        Lazy,
        Advanced
    }
}
