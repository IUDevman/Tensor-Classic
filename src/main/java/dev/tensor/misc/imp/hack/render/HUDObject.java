package dev.tensor.misc.imp.hack.render;

import dev.tensor.misc.imp.render.Render2D;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.ScaledResolution;
import org.lwjgl.opengl.GL11;

/**
 * @author Hoosiers
 * @since 03-24-2026
 */

/**
 * @scaling added on 04-04-2026
 * @ReadMe: Notice on creating HUD Objects:
 * 1. The HUDEditor uses getMinX(), getMinY(), getWidthScaled(), and getHeightScaled() to draw outlines in the editor, dragging, etc.
 * 2. FOR RENDERING in render(); DO NOT USE getWidthScaled() or getHeightScaled() - use getWidth() or getHeight() instead!
 * 3. Code your renders as if they do not have scaling (ex: if I want to get 1/2 of getWidth() (value = 100), I will divide getWidth() by 2
 * (result = 50) and not apply scaling. If you put scaling into your width or height render calls, it will not work and offset by 2x).
 * 4. To set up scaling, create a DoubleSetting for scale and link it to getScale().
 * Next, define your object width and height (without scaling), and then multiply by getScale() for the scaled width and height.
 * Start scaling by calling prepareScale() before your renders (box, text, etc.) and call finishScale() after your render.
 * Scaling will set your posX and posY start to 0,0. No need to call the x or y IntegerSettings.
 * I know scaling works for boxes/text, but Minecraft render functions for items and blocks are weird,
 * so if you use them I would suggest to decouple your object from the default scaling like how I did for Armor and Inventory.
 */

public interface HUDObject extends Render2D {

    IntegerSetting getMinX();

    IntegerSetting getMinY();

    DoubleSetting getScale();

    int getWidth();

    int getWidthScaled();

    int getHeight();

    int getHeightScaled();

    void render();

    default int getMaxX() {
        return 5000;
    }

    default int getMaxY() {
        return 2000;
    }

    default void prepareScale() {
        this.prepareScale(false);
    }

    default void prepareScale(boolean scissor) {
        GL11.glPushMatrix();

        if (scissor) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);

            int posX = this.getMinX().getValue();
            int posY = this.getMinY().getValue();
            int width1 = this.getWidthScaled();
            int height1 = this.getHeightScaled();

            int windowHeight = this.getMinecraft().displayHeight;

            ScaledResolution scaledResolution = new ScaledResolution(this.getMinecraft().gameSettings, this.getMinecraft().displayWidth, this.getMinecraft().displayHeight);
            double scaleX = (double) this.getMinecraft().displayWidth / (double) scaledResolution.getScaledWidth();
            double scaleY = (double) this.getMinecraft().displayHeight / (double) scaledResolution.getScaledHeight();

            int widthScaled = (int) (scaleX * width1);
            int heightScaled = (int) (scaleY * height1);
            int xScaled = (int) (posX * scaleX);
            int yScaled = (int) (posY * scaleY);

            GL11.glScissor(xScaled, windowHeight - yScaled - heightScaled, widthScaled, heightScaled);
        }

        double scale = this.getScale().getValue();

        GL11.glTranslatef(this.getMinX().getValue(), this.getMinY().getValue(), -3.0F);
        GL11.glScalef((float) scale, (float) scale, 0.0F);
        GL11.glTranslatef(0, 0, -3.0F);
    }

    default void finishScale() {
        this.finishScale(false);
    }

    default void finishScale(boolean scissor) {
        if (scissor) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }

        GL11.glPopMatrix();
    }

    default RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Default;
    }

    default int getRenderOrderInt() {
        return this.getRenderOrder().value();
    }
}
