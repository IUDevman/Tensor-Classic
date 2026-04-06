package dev.tensor.feature.hacks.hud;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.HUDObject;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.ColorSettingNoAlpha;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-05-2026
 */

@Hack.Info(name = "Slug", category = Category.HUD)
public final class Slug extends Hack implements HUDObject {

    public final IntegerSetting x = new IntegerSetting("X", 100, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 100, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 0.50, 0.10, 2.00, 2);
    public final EnumSetting mode = new EnumSetting("Mode", Mode.None);
    public final ColorSettingNoAlpha color = new ColorSettingNoAlpha("Color", Color.WHITE);

    @Override
    public IntegerSetting getMinX() {
        return this.x;
    }

    @Override
    public IntegerSetting getMinY() {
        return this.y;
    }

    @Override
    public DoubleSetting getScale() {
        return this.scale;
    }

    @Override
    public int getWidth() {
        return 256;
    }

    @Override
    public int getHeight() {
        return 256;
    }

    @Override
    public int getWidthScaled() {
        return (int) (this.getWidth() * this.scale.getValue());
    }

    @Override
    public int getHeightScaled() {
        return (int) (this.getHeight() * this.scale.getValue());
    }

    private int rotation = 0;

    public void onClientTick() {
        if (this.rotation >= 360) {
            this.rotation = 0;
        } else {
            this.rotation += 20;
        }
    }

    @Override
    public void render() {
        this.prepareScale();

        GL11.glColor4f(this.color.getValue().getRed() / 255F, this.color.getValue().getGreen() / 255F, this.color.getValue().getBlue() / 255F, 1F);

        this.getMinecraft().renderEngine.bindTexture(this.getMinecraft().renderEngine.getTexture("/assets/tensorclassic/slug.png"));

        if (this.mode.getValue().equals(Mode.VibeCoded)) {
            GL11.glTranslatef(128F, 128F, -0F);
            GL11.glRotatef(this.rotation, 0F, 0F, 0F);
            GL11.glTranslatef(128, 0F, 0F);

        } else if (this.mode.getValue().equals(Mode.Circle)) {
            GL11.glTranslatef(128F, 128F, 0F);
            GL11.glRotatef(-this.rotation, 0.0F, 0.0F, 1.0f);

        } else if (this.mode.getValue().equals(Mode.Rotate)) {
            GL11.glTranslatef(128F, 128F, 0F);
            GL11.glRotatef(-this.rotation, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-128F, -128F, 0.0F);
        }

        this.drawTexturedRectangle(0, 0, 0, 0, 256, 256);

        this.finishScale();
    }

    public void drawTexturedRectangle(int minX, int minY, int offsetX, int offsetY, int imageWidth, int imageHeight) {
        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.addVertexWithUV(minX, minY + imageHeight, 0, (float) (offsetX) * 0.00390625F, (float) (offsetY + imageHeight) * 0.00390625F);
        Tessellator.instance.addVertexWithUV(minX + imageWidth, minY + imageHeight, 0, (float) (offsetX + imageWidth) * 0.00390625F, (float) (offsetY + imageHeight) * 0.00390625F);
        Tessellator.instance.addVertexWithUV(minX + imageWidth, minY, 0, (float) (offsetX + imageWidth) * 0.00390625F, (float) (offsetY) * 0.00390625F);
        Tessellator.instance.addVertexWithUV(minX, minY, 0, (float) (offsetX) * 0.00390625F, (float) (offsetY) * 0.00390625F);
        Tessellator.instance.draw();
    }

    private enum Mode {
        None,
        Rotate,
        Circle,
        VibeCoded
    }
}
