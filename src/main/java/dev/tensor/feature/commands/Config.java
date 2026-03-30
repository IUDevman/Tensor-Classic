package dev.tensor.feature.commands;

import dev.tensor.misc.imp.command.Command;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

public final class Config implements Command {

    @Override
    public String getName() {
        return "Config";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName(),
                "Configuration",
                "Profile"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {save/reload}";
    }

    @Override
    public String getDescription() {
        return "Saves or reloads the client config.";
    }

    @Override
    public void onCommand(String[] ARGS) {

        if (ARGS == null || ARGS.length == 0) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to save or reload config (no value inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to save or reload config (no value inputted)!");
            return;
        }

        if (firstEntry.equalsIgnoreCase("save")) {
            this.getTensor().saveManagers();
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Saved config!");

        } else if (firstEntry.equalsIgnoreCase("reload")) {
            this.getTensor().restartManagers(false);
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Reloaded config!");

        } else {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to save or reload config (unknown value inputted)!");
        }
    }
}
