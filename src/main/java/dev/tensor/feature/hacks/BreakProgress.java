package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.RenderBlockBreakEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.block.BlockCoords;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLadder;
import net.minecraft.src.RenderManager;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-14-2026
 */

@Hack.Info(name = "BreakProgress", category = Category.Render)
public final class BreakProgress extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.BreakProgress;
    }

    public final EnumSetting render = new EnumSetting("Render", Render.Fill);
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5);
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 100, 0, 255);
    public final BooleanSetting usePercentageColor = new BooleanSetting("Percentage Color", true);
    public final ColorSetting color = new ColorSetting("Color", Color.GREEN);

    private BlockCoords positionToRender = null;

    @Override
    public void onDisable(boolean isNull) {
        this.positionToRender = null;
    }

    @Override
    public void onClientTick() {
        if (this.positionToRender != null && this.getMinecraft().renderGlobal.damagePartialTime == 0F) {
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

        AxisAlignedBB scaled = AxisAlignedBB.getBoundingBox(rx, ry, rz, mx, my, mz);

        float scale = (this.getMinecraft().renderGlobal.damagePartialTime / 2.0F) + 0.5F;

        scaled = scaled.expand(-scale * (mx - rx), -scale * (my - ry), -scale * (mz - rz));

        Color color = this.color.getValue();

        if (this.usePercentageColor.getValue()) {
            color = this.getPercentageColor(this.getMinecraft().renderGlobal.damagePartialTime, 1F);
        }

        switch ((Render) this.render.getValue()) {
            case Outline:
                this.drawOutlineBox(scaled.minX, scaled.minY, scaled.minZ, scaled.maxX, scaled.maxY, scaled.maxZ, this.lineWidth.getValue(), color);
                break;
            case Fill:
                this.drawFillBox(scaled.minX, scaled.minY, scaled.minZ, scaled.maxX, scaled.maxY, scaled.maxZ, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                break;
            case Both:
                this.drawFillBox(scaled.minX, scaled.minY, scaled.minZ, scaled.maxX, scaled.maxY, scaled.maxZ, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                this.drawOutlineBox(scaled.minX, scaled.minY, scaled.minZ, scaled.maxX, scaled.maxY, scaled.maxZ, this.lineWidth.getValue(), color);
                break;
        }
    }

    @SuppressWarnings("unused")
    EventAction<RenderBlockBreakEvent> renderBlockBreakEventAction = this.addEventAction(new EventAction<>(RenderBlockBreakEvent.class, event -> {
        if (this.getMinecraft().renderGlobal.damagePartialTime == 0F) {
            return;
        }

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

    private Color getPercentageColor(float current, float max) {
        int dColor = Math.round(255.0F - (max - current) * 255.0F / max);
        int color = 255 - dColor << 16 | dColor << 8;

        return new Color(color >> 16 & 255, color >> 8 & 255, color & 255);
    }

    private enum Render {
        Outline,
        Fill,
        Both
    }
}
