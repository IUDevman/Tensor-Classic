package dev.tensor.feature.commands;

import dev.tensor.misc.imp.command.Command;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

public final class Toggle implements Command {

    @Override
    public String getName() {
        return "Toggle";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName(),
                "SetEnabled",
                "Enable"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {hack}";
    }

    @Override
    public String getDescription() {
        return "Toggles the specified hack.";
    }

    @Override
    public void onCommand(String[] ARGS) {

        if (ARGS == null || ARGS.length == 0) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to toggle hack (no name inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to toggle hack (no name inputted)!");
            return;
        }

        Hack hack = this.getHackManager().getHack(firstEntry);

        if (hack == null) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to find hack '" + this.getColor(ChatColor.AQUA) + firstEntry + this.getColor(ChatColor.RED) + "' to toggle!");
            return;
        }

        hack.toggle();

        String enabled = (hack.isEnabled() ? this.getColor(ChatColor.GREEN) + "Enabled" : this.getColor(ChatColor.RED) + "Disabled");
        this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Toggled hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.DEFAULT) + "' to " + enabled + this.getColor(ChatColor.DEFAULT) + "!");
    }
}
