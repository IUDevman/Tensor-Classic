package dev.tensor.misc.gui.imp.description.imp;

import dev.tensor.misc.gui.imp.description.GUIDescription;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.hack.Category;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 09-20-2026
 */

public final class CategoryDescription implements GUIDescription {

    private final Category category;
    private final String combinedDescription;

    private ArrayList<String> descriptionOverflow = null;
    private int maxTextLength = 0;

    public CategoryDescription(Category category) {
        this.category = category;

        this.combinedDescription = ChatColor.YELLOW.text() + this.category.name() + ":" + ChatColor.WHITE.text() + " " + this.category.getDescription();

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
                    toAdd = this.getPreviousChatColor(toAdd) + chunk;
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
        return this.category;
    }

    @Override
    public void render(int posX, int posY, Color fillColor, Color outlineColor) {
        this.renderSimple(posX, posY, fillColor, outlineColor, this.combinedDescription, this.descriptionOverflow, this.maxTextLength);
    }
}
