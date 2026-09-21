package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.RenderSelectionBoxEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.block.BlockCoords;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLadder;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderManager;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-11-2026
 */

@Hack.Info(name = "BlockHighlight", category = Category.Render, description = "Custom render for blocks highlighted by your cursor.")
public final class BlockHighlight extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.BlockHighlight;
    }

    public final EnumSetting render = new EnumSetting("Render", Render.Outline);
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5);
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 100, 0, 255);
    public final ColorSetting color = new ColorSetting("Color", Color.RED);

    private BlockCoords positionToRender = null;

    @Override
    public void onDisable(boolean isNull) {
        this.positionToRender = null;
    }

    @Override
    public void onClientTick() {
        if (this.getMinecraft().objectMouseOver == null || !this.hasSameCoords(this.getMinecraft().objectMouseOver)) {
            this.positionToRender = null;
        }
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        if (this.positionToRender == null) {
            return;
        }

        this.positionToRender.block.setBlockBoundsBasedOnState(this.getWorld(), this.positionToRender.x, this.positionToRender.y, this.positionToRender.z);

        double rx = this.positionToRender.x - RenderManager.renderPosX + this.positionToRender.block.minX;
        double ry = this.positionToRender.y - RenderManager.renderPosY + this.positionToRender.block.minY;
        double rz = this.positionToRender.z - RenderManager.renderPosZ + this.positionToRender.block.minZ;

        double mx = (this.positionToRender.block.maxX - this.positionToRender.block.minX) + rx;
        double my = (this.positionToRender.block.maxY - this.positionToRender.block.minY) + ry;
        double mz = (this.positionToRender.block.maxZ - this.positionToRender.block.minZ) + rz;

        if (this.positionToRender.block instanceof BlockLadder) {
            int metadata = this.getWorld().getBlockMetadata(this.positionToRender.x, this.positionToRender.y, this.positionToRender.z);

            if (metadata == 2) {
                rx = this.positionToRender.x - RenderManager.renderPosX;
                ry = this.positionToRender.y - RenderManager.renderPosY;
                rz = this.positionToRender.z - RenderManager.renderPosZ + 0.9D;

                mx = 1D + rx;
                my = 1D + ry;
                mz = 0.1D + rz;

            } else if (metadata == 3) {
                rx = this.positionToRender.x - RenderManager.renderPosX;
                ry = this.positionToRender.y - RenderManager.renderPosY;
                rz = this.positionToRender.z - RenderManager.renderPosZ;

                mx = 1D + rx;
                my = 1D + ry;
                mz = 0.1D + rz;

            } else if (metadata == 4) {
                rx = this.positionToRender.x - RenderManager.renderPosX + 0.9D;
                ry = this.positionToRender.y - RenderManager.renderPosY;
                rz = this.positionToRender.z - RenderManager.renderPosZ;

                mx = 0.1D + rx;
                my = 1D + ry;
                mz = 1D + rz;

            } else if (metadata == 5) {
                rx = this.positionToRender.x - RenderManager.renderPosX;
                ry = this.positionToRender.y - RenderManager.renderPosY;
                rz = this.positionToRender.z - RenderManager.renderPosZ;

                mx = 0.1D + rx;
                my = 1D + ry;
                mz = 1D + rz;

            } else {
                rx = this.positionToRender.x - RenderManager.renderPosX;
                ry = this.positionToRender.y - RenderManager.renderPosY;
                rz = this.positionToRender.z - RenderManager.renderPosZ;

                mx = 1D + rx;
                my = 1D + ry;
                mz = 1D + rz;
            }
        }

        Color color = this.color.getValueRainbow();

        switch ((Render) this.render.getValue()) {
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
    private final EventAction<RenderSelectionBoxEvent> renderSelectionBoxEventAction = this.addHackEventAction(new EventAction<>(RenderSelectionBoxEvent.class, event -> {
        if (!event.isCancelled()) {
            int x = event.positionToRender.blockX;
            int y = event.positionToRender.blockY;
            int z = event.positionToRender.blockZ;

            int blockID = this.getWorld().getBlockId(x, y, z);

            if (blockID == 0) {
                return;
            }

            Block block = Block.blocksList[blockID];

            if (block == null) {
                return;
            }

            this.positionToRender = new BlockCoords(block, x, y, z);
            event.cancel();
        }
    }));

    private boolean hasSameCoords(MovingObjectPosition position) {
        if (this.positionToRender == null) {
            return false;
        }

        boolean isX = position.blockX == this.positionToRender.x;
        boolean isY = position.blockY == this.positionToRender.y;
        boolean isZ = position.blockZ == this.positionToRender.z;

        return isX && isY && isZ;
    }

    private enum Render {
        Outline,
        Fill,
        Both
    }
}
