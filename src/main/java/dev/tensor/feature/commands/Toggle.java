package dev.tensor.feature.commands;

import dev.tensor.misc.helpers.ChatColor;
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
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to toggle hack (no name inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to toggle hack (no name inputted)!");
            return;
        }

        Hack hack = this.getHackManager().getHack(firstEntry);

        if (hack == null) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to find hack '" + ChatColor.AQUA.text() + firstEntry + ChatColor.RED.text() + "' to toggle!");
            return;
        }

        hack.toggle();

        String enabled = (hack.isEnabled() ? ChatColor.GREEN.text() + "Enabled" : ChatColor.RED.text() + "Disabled");
        this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Toggled hack '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.DEFAULT.text() + "' to " + enabled + ChatColor.DEFAULT.text() + "!");
    }
}
