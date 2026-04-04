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
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        float a = (float) (color >> 24 & 255) / 255.0F;

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
