package dev.tensor.misc.gui.imp.description;

import dev.tensor.misc.imp.render.Render2D;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 09-20-2026
 */

public interface GUIDescription extends Render2D {

    Object getObject();

    void render(int posX, int posY, Color fillColor, Color outlineColor);

    default int getMaxTextLength() {
        return 300;
    }

    default int getCursorOffset() {
        return 10;
    }

    default void renderSimple(int posX, int posY, Color fillColor, Color outlineColor, String combinedDescription, ArrayList<String> descriptionOverflow, int maxTextLength) {
        posY += this.getCursorOffset();

        if (descriptionOverflow != null) {
            int minX = posX;
            int minY = posY;
            int width = maxTextLength + 6;
            int height = 4 + (descriptionOverflow.size() * 12);

            int maxX = minX + width;
            int maxY = minY + height;

            if (maxX > this.getMinecraft().currentScreen.width) {
                minX -= (maxX - this.getMinecraft().currentScreen.width);
            }

            if (maxY > this.getMinecraft().currentScreen.height) {
                minY -= (maxY - this.getMinecraft().currentScreen.height);
            }

            this.drawOutlinedRectangle(minX, minY, width, height, 1, fillColor.getRGB(), outlineColor.getRGB(), true);

            for (int i = 0; i < descriptionOverflow.size(); i++) {
                String toRender = descriptionOverflow.get(i);

                this.drawString(toRender, minX + 3, minY + 3 + (12 * i), Color.WHITE.getRGB());
            }

            return;
        }

        int minX = posX;
        int minY = posY;
        int width = this.stringWidth(combinedDescription) + 6;
        int height = 16;

        int maxX = minX + width;
        int maxY = minY + height;

        if (maxX > this.getMinecraft().currentScreen.width) {
            minX -= (maxX - this.getMinecraft().currentScreen.width);
        }

        if (maxY > this.getMinecraft().currentScreen.height) {
            minY -= (maxY - this.getMinecraft().currentScreen.height);
        }

        this.drawOutlinedRectangle(minX, minY, width, height, 1, fillColor.getRGB(), outlineColor.getRGB(), true);

        this.drawString(combinedDescription, minX + 3, minY + 3, Color.WHITE.getRGB());
    }

    default String getPreviousChatColor(String previous) {
        if (!previous.contains("§")) {
            return "";
        }

        String[] split = previous.split("§");

        String last = split[split.length - 1];

        return "§" + last.charAt(0);
    }
}
