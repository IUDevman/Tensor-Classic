package dev.tensor.feature.hacks.hud;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.render.RenderStatic;
import dev.tensor.misc.imp.setting.settings.ColorNoAlphaSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-05-2026
 */

@Hack.Info(name = "Slug", category = Category.HUD, description = "Displays the mod author.")
public final class Slug extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.Hud_Images_Only;
    }

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 369, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 0.50, 0.10, 2.00, 2);
    public final EnumSetting mode = new EnumSetting("Mode", Mode.None, new String[]{
            "This modifies slug behavior.",
            "None® Slug remains stationary.",
            "Rotate® Slug spins around.",
            "Circle® Slug moves around in a circle.",
            "VibeCoded® Weird thing AI made."
    });
    public final ColorNoAlphaSetting color = new ColorNoAlphaSetting("Color", Color.WHITE);

    private int texture = -1;

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

    @Override
    public void onClientTick() {
        if (this.rotation >= 360) {
            this.rotation = 0;
        } else {
            this.rotation += 20;
        }
    }

    @Override
    public void render() {
        if (this.texture == -1) {
            this.texture = this.setupTexture("slug.png", RenderStatic.SLUG_TEXTURE_ID);
        }

        if (texture == -1) {
            return;
        }

        this.prepareScale();

        this.getMinecraft().renderEngine.bindTexture(texture);

        GL11.glColor4f(this.color.getValueRainbow().getRed() / 255F, this.color.getValueRainbow().getGreen() / 255F, this.color.getValueRainbow().getBlue() / 255F, 1F);

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

        this.drawTexturedRectangle(0, 0, 256, 256, 1, 1);

        this.finishScale();
    }

    private enum Mode {
        None,
        Rotate,
        Circle,
        VibeCoded
    }
}
