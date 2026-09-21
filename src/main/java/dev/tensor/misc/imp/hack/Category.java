package dev.tensor.misc.imp.hack;

/**
 * @author Hoosiers
 * @since 04-12-2021
 */

public enum Category {
    Combat("Hosts hacks that are related to fighting."),
    Movement("Hosts hacks that are modify how the player moves."),
    Player("Hosts hacks that related to the player's interaction and inventory."),
    Render("Hosts hacks that add renders to the world."),
    Misc("Hosts random hacks that don't fit well into other categories. Has a lot of chat-based hacks."),
    HUD("Hosts hacks that draw 2D renders on the screen."),
    Client("Hosts hacks that are global settings or relate to the main client.");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
