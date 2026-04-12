package dev.tensor.feature.hacks;

import dev.tensor.backend.events.RenderSelectionBoxEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderManager;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-11-2026
 */

@Hack.Info(name = "BlockHighlight", category = Category.Render)
public final class BlockHighlight extends Render3DHack {

    @Override
    public int getRenderOrder() {
        return RenderOrder.BLOCKHIGHLIGHT;
    }

    public final EnumSetting render = new EnumSetting("Render", Render.Outline);
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5);
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 100, 0, 255);
    public final ColorSetting color = new ColorSetting("Color", Color.RED);

    private MovingObjectPosition positionToRender = null;

    @Override
    public void onDisable(boolean isNull) {
        this.positionToRender = null;
    }

    @Override
    public void onClientTick() {
        if (this.getMinecraft().objectMouseOver == null || this.getMinecraft().objectMouseOver != this.positionToRender) {
            this.positionToRender = null;
        }
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        if (this.positionToRender == null) {
            return;
        }

        double rx = positionToRender.blockX - RenderManager.renderPosX;
        double ry = positionToRender.blockY - RenderManager.renderPosY;
        double rz = positionToRender.blockZ - RenderManager.renderPosZ;

        Color color = this.color.getValue();

        switch ((Render) this.render.getValue()) {
            case Outline:
                this.drawOutlineBox(rx, ry, rz, 1, this.lineWidth.getValue(), color);
                break;
            case Fill:
                this.drawFillBox(rx, ry, rz, 1, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                break;
            case Both:
                this.drawFillBox(rx, ry, rz, 1, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                this.drawOutlineBox(rx, ry, rz, 1, this.lineWidth.getValue(), color);
                break;
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onRenderSelectionBoxEvent(RenderSelectionBoxEvent event) {
        if (!event.isCancelled()) {
            this.positionToRender = event.positionToRender;
            event.cancel();
        }
    }

    private enum Render {
        Outline,
        Fill,
        Both
    }
}
