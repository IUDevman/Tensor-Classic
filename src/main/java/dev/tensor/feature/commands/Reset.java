package dev.tensor.feature.commands;

import dev.tensor.misc.command.Command;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.Setting;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class Reset implements Command {

    @Override
    public String getName() {
        return "Reset";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[] {
                this.getName(),
                "ResetHack",
                "ResetSetting"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {hack name} {setting name}";
    }

    @Override
    public String getDescription() {
        return "Resets hacks or hack settings for the client. Do not input a setting name to reset the entire hack.";
    }

    @Override
    public void onCommand(String[] ARGS) {

        if (ARGS == null || ARGS.length == 0) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to reset hack or setting (no name inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to reset hack or setting (no name inputted)!");
            return;
        }

        Hack hack = this.getHackManager().getHack(firstEntry);

        if (hack == null)  {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to find hack '" + this.getColor(ChatColor.AQUA) + firstEntry + this.getColor(ChatColor.RED) + "' to reset!");
            return;
        }

        if (ARGS.length == 1) {

            hack.reset();
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Reset hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.DEFAULT) + "'!");
            return;
        }

        String secondEntry = ARGS[1];

        if (secondEntry == null || secondEntry.equalsIgnoreCase(" ")) {

            hack.reset();
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Reset hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.DEFAULT) + "'!");
            return;
        }

        Setting<?> setting = hack.getSettings().stream().filter(setting1 -> setting1.getName().equalsIgnoreCase(secondEntry)).findFirst().orElse(null);

        if (setting == null) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to find setting '" + this.getColor(ChatColor.AQUA) + secondEntry + this.getColor(ChatColor.RED) + "' to reset for hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.RED) + "'!");
            return;
        }

        setting.reset();
        this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Reset setting '" + this.getColor(ChatColor.AQUA) + setting.getName() + this.getColor(ChatColor.DEFAULT) + "' for hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.DEFAULT) + "'!");
    }
}
