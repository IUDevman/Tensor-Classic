package dev.tensor.misc.helpers;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public enum ChatColor {
    DARK_RED("§4"),
    RED("§c"),
    GOLD("§6"),
    YELLOW("§e"),
    DARK_GREEN("§2"),
    GREEN("§a"),
    AQUA("§b"),
    DARK_AQUA("§3"),
    BLUE("§9"),
    DARK_BLUE("§1"),
    PINK("§d"),
    PURPLE("§5"),
    LIGHT_GRAY("§7"),
    GRAY("§8"),
    DARK_GRAY("§9"),
    BLACK("§0"),
    WHITE("§f"),
    DEFAULT("§f");

    private final String text;

    ChatColor(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
