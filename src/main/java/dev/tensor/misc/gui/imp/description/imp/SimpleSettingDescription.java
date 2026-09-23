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

public final class SimpleSettingDescription implements GUIDescription {

    private final Setting<?> setting;
    private final String combinedDescription;

    private ArrayList<String> descriptionOverflow = null;
    private int maxTextLength = 0;

    public SimpleSettingDescription(Setting<?> setting, DescriptionModifier modifier) {
        this.setting = setting;

        this.combinedDescription = ChatColor.YELLOW.text() + this.setting.getName() + ":" + ChatColor.WHITE.text() + " " + this.setting.getDescription() + modifier.getModifier();

        if (this.stringWidth(this.combinedDescription) > this.getMaxTextLength()) {
            this.descriptionOverflow = new ArrayList<>();

            this.addDescriptionToArray(this.combinedDescription, this.descriptionOverflow);

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

    public enum DescriptionModifier {
        NONE(""),
        OPEN_PANEL(" " + ChatColor.YELLOW.text() + "Click to open the setting panel!"),
        CLICK_ACTION(" " + ChatColor.YELLOW.text() + "Click to run the action!"),
        COLOR_RGBA(" " + ChatColor.YELLOW.text() + "Click the top to open the color picker or below to modify individual values!"),
        COLOR_RGB(" " + ChatColor.LIGHT_GRAY.text() + "Changing alpha is not enabled for this setting." + COLOR_RGBA.getModifier());

        private final String modifier;

        DescriptionModifier(String modifier) {
            this.modifier = modifier;
        }

        public String getModifier() {
            return this.modifier;
        }
    }
}
