package dev.tensor.misc.gui.imp.description.imp;

import dev.tensor.misc.gui.imp.description.GUIDescription;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.hack.Hack;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 09-20-2026
 */

public final class HackDescription implements GUIDescription {

    private final Hack hack;
    private final String combinedDescription;

    private ArrayList<String> descriptionOverflow = null;
    private int maxTextLength = 0;

    public HackDescription(Hack hack) {
        this.hack = hack;

        this.combinedDescription = ChatColor.YELLOW.text() + this.hack.NAME + ":" + ChatColor.WHITE.text() + " " + this.hack.DESCRIPTION;

        if (this.stringWidth(this.combinedDescription) > this.getMaxTextLength()) {
            ArrayList<String> orderedStrings = new ArrayList<>();

            String[] split = this.combinedDescription.split(" ");

            String toAdd = "";

            for (int i = 0; i < split.length; i++) {
                String chunk = split[i];

                int lengthPre = toAdd.isEmpty() ? 0 : this.stringWidth(toAdd);
                int lengthChunk = this.stringWidth(chunk);

                if (lengthPre + lengthChunk + 1 <= this.getMaxTextLength()) {
                    toAdd = toAdd + " " + chunk;
                    continue;
                }

                if (lengthPre + lengthChunk + 1 > this.getMaxTextLength()) {
                    orderedStrings.add(toAdd);
                    toAdd = chunk;
                }
            }

            if (!toAdd.isEmpty()) {
                orderedStrings.add(toAdd);
            }

            this.descriptionOverflow = orderedStrings;

            for (String toTest : this.descriptionOverflow) {
                int testLength = this.stringWidth(toTest);

                if (testLength > this.maxTextLength) {
                    this.maxTextLength = testLength;
                }
            }
        }
    }

    @Override
    public Object getObject() {
        return this.hack;
    }

    @Override
    public void render(int posX, int posY, Color fillColor, Color outlineColor) {
        if (this.descriptionOverflow != null) {
            int minX = posX;
            int minY = posY;
            int width = this.maxTextLength + 6;
            int height = 4 + (this.descriptionOverflow.size() * 12);

            int maxX = minX + width;
            int maxY = minY + height;

            if (maxX > this.getMinecraft().currentScreen.width) {
                minX -= (maxX - this.getMinecraft().currentScreen.width);
            }

            if (maxY > this.getMinecraft().currentScreen.height) {
                minY -= (maxY - this.getMinecraft().currentScreen.height);
            }

            this.drawOutlinedRectangle(minX, minY, width, height, 1, fillColor.getRGB(), outlineColor.getRGB(), true);

            for (int i = 0; i < this.descriptionOverflow.size(); i++) {
                String toRender = this.descriptionOverflow.get(i);

                this.drawString(toRender, minX + 3, minY + 3 + (12 * i), Color.WHITE.getRGB());
            }

            return;
        }

        int minX = posX;
        int minY = posY;
        int width = this.stringWidth(this.combinedDescription) + 6;
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

        this.drawString(this.combinedDescription, minX + 3, minY + 3, Color.WHITE.getRGB());
    }
}
