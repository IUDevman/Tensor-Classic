package dev.tensor.misc.gui.imp.description.imp;

import dev.tensor.misc.gui.imp.description.ColorHoveredOver;
import dev.tensor.misc.gui.imp.description.GUIDescription;
import dev.tensor.misc.gui.imp.elements.settings.ColorSettingElement;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.setting.settings.ColorSetting;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 09-22-2026
 */

public final class ColorSettingDescription implements GUIDescription {

    private final ColorSettingElement element;
    private final ColorSetting color;

    private final boolean hasAlpha;

    private final ArrayList<String> queuedMessagesToSend = new ArrayList<>();

    private int maxTextLength;

    private ColorHoveredOver CURRENT = null;

    public ColorSettingDescription(ColorSettingElement element) {
        this.element = element;
        this.color = this.element.colorSetting;

        this.hasAlpha = !this.element.disabledAlpha;
    }

    @Override
    public Object getObject() {
        return this.color;
    }

    @Override
    public void render(int posX, int posY, Color fillColor, Color outlineColor) {
        this.loadMessageQueue(posY);

        if (this.queuedMessagesToSend.isEmpty() || this.CURRENT == null) {
            return;
        }

        this.renderArray(posX, posY, fillColor, outlineColor, this.queuedMessagesToSend, this.maxTextLength);
    }

    private void loadMessageQueue(int posY) {
        ColorHoveredOver NEW = this.element.getHoveredComponent(posY);

        if (CURRENT == NEW) {
            return;
        }

        if (NEW == null) {
            return;
        }

        this.CURRENT = NEW;
        this.queuedMessagesToSend.clear();
        this.maxTextLength = 0;

        String combinedDescription = ChatColor.YELLOW.text() + this.color.getName() + ":" + ChatColor.WHITE.text() + " " + this.color.getDescription() + NEW.getDescriptor(this.hasAlpha);

        this.addDescriptionToArray(combinedDescription, this.queuedMessagesToSend);

        for (String toTest : this.queuedMessagesToSend) {
            int testLength = this.stringWidth(toTest);

            if (testLength > this.maxTextLength) {
                this.maxTextLength = testLength;
            }
        }
    }
}
