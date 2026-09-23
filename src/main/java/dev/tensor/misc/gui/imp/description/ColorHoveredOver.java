package dev.tensor.misc.gui.imp.description;

import dev.tensor.misc.helpers.text.ChatColor;

/**
 * @author Hoosiers
 * @since 09-22-2026
 */

public enum ColorHoveredOver {

    TITLE(" " + ChatColor.YELLOW.text() + "Click to open the color picker!"),
    RED(" " + ChatColor.YELLOW.text() + "Click to modify the red value for this color!"),
    GREEN(" " + ChatColor.YELLOW.text() + "Click to modify the green value for this color!"),
    BLUE(" " + ChatColor.YELLOW.text() + "Click to modify the blue value for this color!"),
    ALPHA(" " + ChatColor.YELLOW.text() + "Click to modify the alpha value for this color!");

    private final String descriptor;

    ColorHoveredOver(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getDescriptor(boolean hasAlpha) {
        if (!hasAlpha && this.equals(ALPHA)) {
            return " " + ChatColor.RED.text() + "Changing alpha is not enabled for this setting.";
        }

        return this.descriptor;
    }
}
