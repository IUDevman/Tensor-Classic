package dev.tensor.misc.gui.imp.description.imp;

import dev.tensor.misc.gui.imp.description.GUIDescription;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.setting.Setting;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 09-20-2026
 */

public class SimpleSettingDescription implements GUIDescription {

    private final Setting<?> setting;
    private final String combinedDescription;

    private ArrayList<String> descriptionOverflow = null;
    private int maxTextLength = 0;

    public SimpleSettingDescription(Setting<?> setting, boolean shouldClickForPanel) {
        this.setting = setting;

        this.combinedDescription = ChatColor.YELLOW.text() + this.setting.getName() + ":" + ChatColor.WHITE.text() + " " + this.setting.getDescription() + (shouldClickForPanel ? ChatColor.YELLOW.text() + " Click to open the setting panel!" : "");

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
        return this.setting;
    }

    @Override
    public void render(int posX, int posY, Color fillColor, Color outlineColor) {
        this.renderSimple(posX, posY, fillColor, outlineColor, this.combinedDescription, this.descriptionOverflow, this.maxTextLength);
    }
}
