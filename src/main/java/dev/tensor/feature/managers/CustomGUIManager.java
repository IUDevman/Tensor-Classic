package dev.tensor.feature.managers;

import dev.tensor.misc.imp.Manager;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;

/**
 * @author Hoosiers
 * @since 03-24-2026
 */

public final class CustomGUIManager implements Manager {

    @Override
    public String getName() {
        return "CustomGUIManager";
    }

    @Override
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());
    }

    public void drawRectangle(int minX, int minY, int maxX, int maxY, int color) {
        float var12 = (float)(color >> 24 & 255) / 255.0F;
        float var7 = (float)(color >> 16 & 255) / 255.0F;
        float var8 = (float)(color >> 8 & 255) / 255.0F;
        float var9 = (float)(color & 255) / 255.0F;
        Tessellator var10 = Tessellator.instance;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(var7, var8, var9, var12);
        var10.startDrawingQuads();
        var10.addVertex(minX, maxY, 0.0F);
        var10.addVertex(maxX, maxY, 0.0F);
        var10.addVertex(maxX, minY, 0.0F);
        var10.addVertex(minX, minY, 0.0F);
        var10.draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public void drawString(String text, int x, int y, int color) {
        this.getInGameGUI().drawString(this.getFontRenderer(), text, x, y, color);
    }

    public void drawCenteredString(String text, int x, int y, int color) {
        this.drawString(text, x - (this.width(text) / 2), y, color);
    }

    private int width(String string) {
        return this.getFontRenderer().getStringWidth(string);
    }
}
