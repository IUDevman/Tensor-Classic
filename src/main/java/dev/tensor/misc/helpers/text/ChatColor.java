package dev.tensor.misc.helpers.text;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public enum ChatColor {

    DARK_RED("§4", new Color(170, 0, 0)),
    RED("§c", new Color(255, 85, 85)),
    GOLD("§6", new Color(255, 170, 0)),
    YELLOW("§e", new Color(255, 255, 85)),
    DARK_GREEN("§2", new Color(0, 170, 0)),
    GREEN("§a", new Color(85, 255, 85)),
    AQUA("§b", new Color(85, 255, 255)),
    DARK_AQUA("§3", new Color(0, 170, 170)),
    BLUE("§9", new Color(85, 85, 255)),
    DARK_BLUE("§1", new Color(0, 0, 170)),
    PINK("§d", new Color(255, 85, 255)),
    PURPLE("§5", new Color(170, 0, 170)),
    LIGHT_GRAY("§7", new Color(170, 170, 170)),
    GRAY("§8", new Color(85, 85, 85)),
    BLACK("§0", new Color(0, 0, 0)),
    WHITE("§f", new Color(255, 255, 255)),
    DEFAULT("§f", new Color(255, 255, 255));

    private final String text;
    private final Color color;

    ChatColor(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    public String text() {
        return this.text;
    }

    public Color color() {
        return this.color;
    }
}
