package dev.tensor.misc.imp.render;

import dev.tensor.misc.imp.Globals;
import net.minecraft.src.*;
import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-06-2026
 */

public interface Render2D extends Globals {

    default void drawRectangle(int minX, int minY, int maxX, int maxY, int color) {
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        float a = (color >> 24 & 255) / 255.0F;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glColor4f(r, g, b, a);

        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.addVertex(minX, maxY, 0.0F);
        Tessellator.instance.addVertex(maxX, maxY, 0.0F);
        Tessellator.instance.addVertex(maxX, minY, 0.0F);
        Tessellator.instance.addVertex(minX, minY, 0.0F);
        Tessellator.instance.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    default void drawGradientRectangleTopBottom(int minX, int minY, int maxX, int maxY, int topColor, int bottomColor) {
        float topR = (topColor >> 16 & 255) / 255.0F;
        float topG = (topColor >> 8 & 255) / 255.0F;
        float topB = (topColor & 255) / 255.0F;
        float topA = (topColor >> 24 & 255) / 255.0F;

        float bottomR = (bottomColor >> 16 & 255) / 255.0F;
        float bottomG = (bottomColor >> 8 & 255) / 255.0F;
        float bottomB = (bottomColor & 255) / 255.0F;
        float bottomA = (bottomColor >> 24 & 255) / 255.0F;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        Tessellator.instance.startDrawingQuads();

        Tessellator.instance.setColorRGBA_F(bottomR, bottomG, bottomB, bottomA);

        Tessellator.instance.addVertex(minX, maxY, 0.0F);
        Tessellator.instance.addVertex(maxX, maxY, 0.0F);

        Tessellator.instance.setColorRGBA_F(topR, topG, topB, topA);

        Tessellator.instance.addVertex(maxX, minY, 0.0F);
        Tessellator.instance.addVertex(minX, minY, 0.0F);

        Tessellator.instance.draw();

        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    default void drawSmallColorPickerRectangle(int startX, int startY, int width, int height) {
        float[][] colors = this.getColorGradient();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        Tessellator.instance.startDrawingQuads();

        double yDiff = (double) height / (colors.length - 1);

        for (int i = 0; i < colors.length - 1; i++)  {
            float[] top = colors[i];
            float[] bottom = colors[i + 1];

            Tessellator.instance.setColorRGBA_F(bottom[0], bottom[1], bottom[2], bottom[3]);

            float minY = (float) (startY + (yDiff * i));
            float maxY = (float) (startY + (yDiff * (i + 1)));

            Tessellator.instance.addVertex(startX, maxY, 0.0F);
            Tessellator.instance.addVertex(startX + width, maxY, 0.0F);

            Tessellator.instance.setColorRGBA_F(top[0], top[1], top[2], top[3]);

            Tessellator.instance.addVertex(startX + width, minY, 0.0F);
            Tessellator.instance.addVertex(startX, minY, 0.0F);
        }

        Tessellator.instance.draw();

        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    default float[][] getColorGradient() {
        return new float[][]{
                {1F, 0F, 0F, 1F},
                {1F, 1F, 0F, 1F},
                {0F, 1F, 0F, 1F},
                {0F, 1F, 1F, 1F},
                {0F, 0F, 1F, 1F},
                {1F, 0F, 1F, 1F},
                {1F, 0F, 0F, 1F}
        };
    }

    //TODO: The R, G, and B should be the color from the 2nd rainbow slider
    //We somehow have to approximate what color our already set value will land on the rainbow slider
    //Modifying that slider only effects this one
    //The point on this slider will determine the new value!
    //TODO: make the color approximation only happen on load or when value manually inputted!

    //Take R, G, and B values
    //Filter by size (highest values first).
    // Find default color with those two, set it on the small color picker
    // Account for the values on the large color picker

    //Farther to the left, add values
    //Farther down subtract values
    //Farther right add to values that are 0 by default
    default void drawLargeColorPickerRectangle(int startX, int startY, int width, int height, int red, int green, int blue) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        Tessellator.instance.startDrawingQuads();

        Tessellator.instance.setColorRGBA_F(0F, 0F, 0F, 1F);
        Tessellator.instance.addVertex(startX, startY + height, 0.0F);

        Tessellator.instance.setColorRGBA_F(0F, 0F, 0F, 1F);
        Tessellator.instance.addVertex(startX + width, startY + height, 0.0F);

        Tessellator.instance.setColorRGBA_F(red / 255F, green / 255F, blue / 255F, 1F);
        Tessellator.instance.addVertex(startX + width, startY, 0.0F);

        Tessellator.instance.setColorRGBA_F(1F, 1F, 1F, 1F);
        Tessellator.instance.addVertex(startX, startY, 0.0F);

        Tessellator.instance.draw();

        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    default void drawString(String text, int x, int y, int color) {
        this.getInGameGUI().drawString(this.getFontRenderer(), text, x, y, color);
    }

    default void drawCenteredString(String text, int x, int y, int color) {
        this.drawString(text, x - (this.stringWidth(text) / 2), y, color);
    }

    default int stringWidth(String string) {
        return this.getFontRenderer().getStringWidth(string);
    }

    default void startItemGL() {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);

        RenderHelper.enableStandardItemLighting();

        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    default void finishItemGL() {
        RenderHelper.disableStandardItemLighting();

        GL11.glDisable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }

    default void renderItem(ItemStack itemStack, int startX, int startY, int itemX, int itemY, double scale, double itemScale) {
        int itemID = itemStack.itemID;
        int itemIcon = itemStack.getIconIndex();

        if (itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemID].getRenderType())) {
            this.getRenderEngine().bindTexture(this.getRenderEngine().getTexture("/terrain.png"));

            GL11.glPushMatrix();

            GL11.glTranslatef((float) (startX), (float) (startY), -3.0F);
            GL11.glScalef((float) (10.0F * scale), (float) (10.0F * scale), (float) (10.0F * scale));

            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);

            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);

            int damageColor = Item.itemsList[itemID].getColorFromDamage(itemStack.getItemDamage());
            float r = (float) (damageColor >> 16 & 255) / 255.0F;
            float g = (float) (damageColor >> 8 & 255) / 255.0F;
            float b = (float) (damageColor & 255) / 255.0F;

            if (RenderStatic.tensor_renderItem.field_27004_a) {
                GL11.glColor4f(r, g, b, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);

            RenderStatic.tensor_renderBlocks.field_31088_b = RenderStatic.tensor_renderItem.field_27004_a;
            RenderStatic.tensor_renderBlocks.renderBlockOnInventory(Block.blocksList[itemID], itemStack.getItemDamage(), 1.0F);
            RenderStatic.tensor_renderBlocks.field_31088_b = true;

            GL11.glPopMatrix();

            GL11.glTranslatef((float) (startX + (itemX * scale)), (float) (startY - (itemY * scale)), -3.0F);
            GL11.glScalef((float) ((1.0F * scale) - itemScale), (float) ((1.0F * scale) - itemScale), 0F);

        } else if (itemIcon >= 0) {

            GL11.glTranslatef((float) (startX + (itemX * scale)), (float) (startY - (itemY * scale)), -3.0F);
            GL11.glScalef((float) ((1.0F * scale) - itemScale), (float) ((1.0F * scale) - itemScale), 0F);

            GL11.glDisable(GL11.GL_LIGHTING);

            if (itemID < 256) {
                this.getRenderEngine().bindTexture(this.getRenderEngine().getTexture("/terrain.png"));
            } else {
                this.getRenderEngine().bindTexture(this.getRenderEngine().getTexture("/gui/items.png"));
            }

            int damageColor = Item.itemsList[itemID].getColorFromDamage(itemStack.getItemDamage());
            float r = (float) (damageColor >> 16 & 255) / 255.0F;
            float g = (float) (damageColor >> 8 & 255) / 255.0F;
            float b = (float) (damageColor & 255) / 255.0F;

            if (RenderStatic.tensor_renderItem.field_27004_a) {
                GL11.glColor4f(r, g, b, 1.0F);
            }

            RenderStatic.tensor_renderItem.renderTexturedQuad(0, 0, itemIcon % 16 * 16, itemIcon / 16 * 16, 16, 16);

            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    default void renderItemOverlay(ItemStack itemStack, int startX, int startY, double scale) {
        if (itemStack == null) {
            return;
        }

        GL11.glTranslatef((float) (startX * scale), (float) (startY * scale), -3);

        if (itemStack.stackSize > 1) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            this.drawString(String.valueOf(itemStack.stackSize), startX + 19 - 2 - this.getFontRenderer().getStringWidth(String.valueOf(itemStack.stackSize)), startY + 6 + 3, Color.WHITE.getRGB());

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        if (itemStack.isItemDamaged()) {
            int minWidth = Math.round(13.0F - itemStack.getItemDamageForDisplay() * 13.0F / itemStack.getMaxDamage());
            int dWidth = Math.round(255.0F - itemStack.getItemDamageForDisplay() * 255.0F / itemStack.getMaxDamage());

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);

            int color0 = 0;
            int color1 = 255 - dWidth << 16 | dWidth << 8;
            int color2 = (255 - dWidth) / 4 << 16 | 16128;

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setColorOpaque_I(color0);
            Tessellator.instance.addVertex(startX + 2, startY + 13, 0.0F);
            Tessellator.instance.addVertex(startX + 2, startY + 15, 0.0F);
            Tessellator.instance.addVertex(startX + 15, startY + 15, 0.0F);
            Tessellator.instance.addVertex(startX + 15, startY + 13, 0.0F);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setColorOpaque_I(color2);
            Tessellator.instance.addVertex(startX + 2, startY + 13, 0.0F);
            Tessellator.instance.addVertex(startX + 2, startY + 14, 0.0F);
            Tessellator.instance.addVertex(startX + 14, startY + 14, 0.0F);
            Tessellator.instance.addVertex(startX + 14, startY + 13, 0.0F);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setColorOpaque_I(color1);
            Tessellator.instance.addVertex(startX + 2, startY + 13, 0.0F);
            Tessellator.instance.addVertex(startX + 2, startY + 14, 0.0F);
            Tessellator.instance.addVertex(startX + 2 + minWidth, startY + 14, 0.0F);
            Tessellator.instance.addVertex(startX + 2 + minWidth, startY + 13, 0.0F);
            Tessellator.instance.draw();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    default void renderItemPercentage(ItemStack itemStack, int startX, int startY) {
        if (itemStack == null) {
            return;
        }

        GL11.glScalef(0.5F, 0.5F, 0.0F);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        int dColor = Math.round(255.0F - itemStack.getItemDamage() * 255.0F / itemStack.getMaxDamage());
        int color = 255 - dColor << 16 | dColor << 8;

        double dDamage = (double) itemStack.getItemDamage() / itemStack.getMaxDamage();
        int percentage = (int) (100 - (dDamage * 100));

        String string = percentage + "%";
        this.drawString(string, startX + 19 - 2 - this.getFontRenderer().getStringWidth(string), startY + 6 + 3, new Color(color >> 16 & 255, color >> 8 & 255, color & 255).getRGB());

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    default boolean isBlackNoAlpha(Color color) {
        return color.getRed() == 0 && color.getGreen() == 0 && color.getBlue() == 0;
    }
}
