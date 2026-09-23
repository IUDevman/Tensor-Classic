package dev.tensor.misc.gui.imp.description.imp;

import dev.tensor.misc.gui.imp.description.GUIDescription;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.setting.settings.EnumSetting;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 09-21-2026;
 */

public final class EnumSettingDescription implements GUIDescription {

    private final EnumSetting setting;

    private final ArrayList<String> descriptionArray = new ArrayList<>();
    private int maxTextLength = 0;

    public EnumSettingDescription(EnumSetting setting) {
        this.setting = setting;

        if (this.setting.getEnumDescription() == null || this.setting.getEnumDescription().length < 1) {
            return;
        }

        String mainDescription = ChatColor.YELLOW.text() + this.setting.getName() + ":" + ChatColor.WHITE.text() + " " + this.setting.getEnumDescription()[0];
        this.addDescription(mainDescription);

        for (int i = 1; i < this.setting.getEnumDescription().length; i++) {

            //dumb I know...
            String enumName = this.setting.getEnumDescription()[i].split("®")[0].replace(" ", "_");

            if (enumName.contains(":")) {
                enumName = enumName.replace(":", "$");
            }

            if (!enumName.equalsIgnoreCase(this.setting.getValue().name())) {
                continue;
            }

            String description = ChatColor.LIGHT_GRAY.text() + "> " + this.setting.getEnumDescription()[i].replace("®", ":");

            this.addDescription(description);
        }

        for (String toTest : this.descriptionArray) {
            int testLength = this.stringWidth(toTest);

            if (testLength > this.maxTextLength) {
                this.maxTextLength = testLength;
            }
        }
    }

    private void addDescription(String description) {
        if (this.stringWidth(description) <= this.getMaxTextLength()) {
            this.descriptionArray.add(description);
            return;
        }

        ArrayList<String> orderedStrings = new ArrayList<>();

        String[] split = description.split(" ");

        String toAdd = "";

        for (String chunk : split) {
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

        this.descriptionArray.addAll(orderedStrings);
    }

    @Override
    public Object getObject() {
        return this.setting;
    }

    @Override
    public void render(int posX, int posY, Color fillColor, Color outlineColor) {
        this.renderArray(posX, posY, fillColor, outlineColor, this.descriptionArray, this.maxTextLength);
    }
}
