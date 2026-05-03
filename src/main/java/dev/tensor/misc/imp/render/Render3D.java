package dev.tensor.misc.imp.render;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 04-06-2026
 */

public interface Render3D extends Render2D {

    default double translateOffset(double previousPos, double currentPos, float renderPartialTicks) {
        return (currentPos - previousPos) * renderPartialTicks;
    }

    default void startBlockGL() {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDepthMask(false);
    }

    default void finishBlockGL() {
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    default void drawOutlineBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int lineWidth, Color color) {
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);

        Tessellator.instance.startDrawing(GL11.GL_LINE_STRIP);
        Tessellator.instance.addVertex(minX, minY, minZ);
        Tessellator.instance.addVertex(maxX, minY, minZ);
        Tessellator.instance.addVertex(maxX, minY, maxZ);
        Tessellator.instance.addVertex(minX, minY, maxZ);
        Tessellator.instance.addVertex(minX, minY, minZ);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_LINE_STRIP);
        Tessellator.instance.addVertex(minX, maxY, minZ);
        Tessellator.instance.addVertex(maxX, maxY, minZ);
        Tessellator.instance.addVertex(maxX, maxY, maxZ);
        Tessellator.instance.addVertex(minX, maxY, maxZ);
        Tessellator.instance.addVertex(minX, maxY, minZ);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_LINES);
        Tessellator.instance.addVertex(minX, minY, minZ);
        Tessellator.instance.addVertex(minX, maxY, minZ);
        Tessellator.instance.addVertex(maxX, minY, minZ);
        Tessellator.instance.addVertex(maxX, maxY, minZ);
        Tessellator.instance.addVertex(maxX, minY, maxZ);
        Tessellator.instance.addVertex(maxX, maxY, maxZ);
        Tessellator.instance.addVertex(minX, minY, maxZ);
        Tessellator.instance.addVertex(minX, maxY, maxZ);
        Tessellator.instance.draw();
    }

    default void drawFillBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color color) {
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);

        Tessellator.instance.startDrawing(GL11.GL_QUADS);
        Tessellator.instance.addVertex(maxX, minY, maxZ);
        Tessellator.instance.addVertex(maxX, minY, minZ);
        Tessellator.instance.addVertex(maxX, maxY, minZ);
        Tessellator.instance.addVertex(maxX, maxY, maxZ);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_QUADS);
        Tessellator.instance.addVertex(minX, minY, minZ);
        Tessellator.instance.addVertex(minX, minY, maxZ);
        Tessellator.instance.addVertex(minX, maxY, maxZ);
        Tessellator.instance.addVertex(minX, maxY, minZ);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_QUADS);
        Tessellator.instance.addVertex(maxX, minY, minZ);
        Tessellator.instance.addVertex(minX, minY, minZ);
        Tessellator.instance.addVertex(minX, maxY, minZ);
        Tessellator.instance.addVertex(maxX, maxY, minZ);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_QUADS);
        Tessellator.instance.addVertex(minX, minY, maxZ);
        Tessellator.instance.addVertex(maxX, minY, maxZ);
        Tessellator.instance.addVertex(maxX, maxY, maxZ);
        Tessellator.instance.addVertex(minX, maxY, maxZ);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_QUADS);
        Tessellator.instance.addVertex(maxX, maxY, minZ);
        Tessellator.instance.addVertex(minX, maxY, minZ);
        Tessellator.instance.addVertex(minX, maxY, maxZ);
        Tessellator.instance.addVertex(maxX, maxY, maxZ);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_QUADS);
        Tessellator.instance.addVertex(maxX, minY, minZ);
        Tessellator.instance.addVertex(maxX, minY, maxZ);
        Tessellator.instance.addVertex(minX, minY, maxZ);
        Tessellator.instance.addVertex(minX, minY, minZ);
        Tessellator.instance.draw();
    }

    default void drawTracer(double targetX, double targetY, double targetZ, int lineWidth, Color color, int alpha) {
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha / 255F);

        Tessellator.instance.startDrawing(GL11.GL_LINES);
        Tessellator.instance.addVertex(0, 0, 0);
        Tessellator.instance.addVertex(targetX, targetY, targetZ);
        Tessellator.instance.draw();
    }

    default void drawBreadCrumbs(ArrayList<double[]> positions, double renderPosX, double renderPosY, double renderPosZ, int lineWidth, Color color) {
        if (positions.isEmpty()) {
            return;
        }

        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);

        Tessellator.instance.startDrawing(GL11.GL_LINE_STRIP);

        for (double[] position : positions) {
            if (position == null) {
                continue;
            }

            Tessellator.instance.addVertex(position[0] - renderPosX, position[1] - renderPosY, position[2] - renderPosZ);
        }

        Tessellator.instance.draw();
    }

    default void renderItem3D(ItemStack itemStack, int startX, int startY, double scale) {
        int itemID = itemStack.itemID;
        int itemIcon = itemStack.getIconIndex();

        if (itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemID].getRenderType())) {
            this.getRenderEngine().bindTexture(this.getRenderEngine().getTexture("/terrain.png"));

            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);

            GL11.glScalef((float) ((1.0F * scale)), (float) ((1.0F * scale)), 0F);
            GL11.glTranslatef((float) (startX), (float) (startY) + 2, -3.0F);
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

            GL11.glEnable(GL11.GL_LIGHTING);

            GL11.glPopMatrix();

            GL11.glScalef((float) ((1.0F * scale)), (float) ((1.0F * scale)), 0F);

        } else if (itemIcon >= 0) {

            GL11.glScalef((float) ((1.0F * scale)), (float) ((1.0F * scale)), 0F);

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

            RenderStatic.tensor_renderItem.renderTexturedQuad(startX, startY + (itemID < 256 ? -2 : 0), itemIcon % 16 * 16, itemIcon / 16 * 16, 16, 16);

            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    default void renderItemOverlay3D(ItemStack itemStack, int startX, int startY) {
        if (itemStack == null) {
            return;
        }

        if (itemStack.stackSize > 1) {
            GL11.glDisable(GL11.GL_LIGHTING);

            this.drawString(String.valueOf(itemStack.stackSize), startX + 19 - 2 - this.getFontRenderer().getStringWidth(String.valueOf(itemStack.stackSize)), startY + 6 + 3, Color.WHITE.getRGB());

            GL11.glEnable(GL11.GL_LIGHTING);
        }

        if (itemStack.isItemDamaged()) {
            int minWidth = Math.round(13.0F - itemStack.getItemDamageForDisplay() * 13.0F / itemStack.getMaxDamage());
            int dWidth = Math.round(255.0F - itemStack.getItemDamageForDisplay() * 255.0F / itemStack.getMaxDamage());

            GL11.glDisable(GL11.GL_LIGHTING);
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

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    default void renderItemPercentage3D(ItemStack itemStack, int startX, int startY, double scale) {
        if (itemStack == null) {
            return;
        }

        GL11.glPushMatrix();

        GL11.glScalef((float) ((1.0F * scale)), (float) ((1.0F * scale)), (float) ((1.0F * scale)));

        GL11.glDisable(GL11.GL_LIGHTING);

        int dColor = Math.round(255.0F - itemStack.getItemDamage() * 255.0F / itemStack.getMaxDamage());
        int color = 255 - dColor << 16 | dColor << 8;

        double dDamage = (double) itemStack.getItemDamage() / itemStack.getMaxDamage();
        int percentage = (int) (100 - (dDamage * 100));

        String string = percentage + "%";

        Color color1 = new Color(color >> 16 & 255, color >> 8 & 255, color & 255);

        if (!itemStack.getItem().isDamagable() && itemStack.stackSize > 1) {
            string = String.valueOf(itemStack.stackSize);
            color1 = Color.WHITE;
        } else if (!itemStack.getItem().isDamagable()) {
            string = "";
            color1 = Color.WHITE;
        }

        this.drawString(string, (int) ((startX + 19 - 2 - this.getFontRenderer().getStringWidth(string)) / scale), (int) ((startY + 6 + 3) / scale), color1.getRGB());

        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();
    }
}
