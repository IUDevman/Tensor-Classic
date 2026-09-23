package dev.tensor.misc.gui.imp.description.imp;

import dev.tensor.misc.gui.imp.description.GUIDescription;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.setting.NumberSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 09-22-2026
 */

public final class NumberSettingDescription implements GUIDescription {

    private final NumberSetting<?> setting;

    private final ArrayList<String> queuedMessages = new ArrayList<>();

    private int maxTextLength;

    public NumberSettingDescription(NumberSetting<?> setting) {
        this.setting = setting;

        String combinedDescription = ChatColor.YELLOW.text() + this.setting.getName() + ":" + ChatColor.WHITE.text() + " " + this.setting.getDescription() + this.getMinMaxMessage() + this.getUnitsMessage();

        this.addDescriptionToArray(combinedDescription, this.queuedMessages);

        for (String toTest : this.queuedMessages) {
            int testLength = this.stringWidth(toTest);

            if (testLength > this.maxTextLength) {
                this.maxTextLength = testLength;
            }
        }
    }

    @Override
    public Object getObject() {
        return this.setting;
    }

    @Override
    public void render(int posX, int posY, Color fillColor, Color outlineColor) {
        this.renderArray(posX, posY, fillColor, outlineColor, this.queuedMessages, this.maxTextLength);
    }

    private String getMinMaxMessage() {
        if (this.setting.getDescription().equalsIgnoreCase("None")) {
            return "";
        }

        String returnVal = "";

        if (this.setting instanceof IntegerSetting) {
            int min = ((IntegerSetting) this.setting).getMin();
            int max = ((IntegerSetting) this.setting).getMax();

            returnVal = " " + ChatColor.LIGHT_GRAY.text() + "Min = " + min + ", Max = " + max + ".";
        }

        if (this.setting instanceof DoubleSetting) {
            double minD = ((DoubleSetting) this.setting).getMin();
            double maxD = ((DoubleSetting) this.setting).getMax();

            returnVal = " " + ChatColor.LIGHT_GRAY.text() + "Min = " + ((DoubleSetting) this.setting).getFormattedFromValue(minD) + ", Max = " + ((DoubleSetting) this.setting).getFormattedFromValue(maxD) + ", decimals =" + ((DoubleSetting) this.setting).getDecimal() + ".";
        }

        return returnVal;
    }

    private String getUnitsMessage() {
        if (this.setting.getUnits().equalsIgnoreCase("None")) {
            return "";
        }

        return " " + ChatColor.YELLOW.text() + "Units are in " + this.setting.getUnits() + "!";
    }
}
