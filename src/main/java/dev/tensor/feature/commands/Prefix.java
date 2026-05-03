package dev.tensor.feature.commands;

import dev.tensor.feature.hacks.Commands;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.command.Command;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class Prefix implements Command {

    @Override
    public String getName() {
        return "Prefix";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName(),
                "SetPrefix",
                "CommandPrefix"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {prefix$char}";
    }

    @Override
    public String getDescription() {
        return "Sets the client command prefix to the inputted char.";
    }

    @Override
    public void onCommand(String[] ARGS) {

        if (ARGS == null || ARGS.length == 0) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set prefix (no new prefix inputted)!");
            return;
        }

        String newPrefix = ARGS[0];

        if (newPrefix == null || newPrefix.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set prefix (no new prefix inputted)!");
            return;
        }

        Commands commands = this.getHackManager().getHack(Commands.class);

        String oldPrefix = commands.prefix.getValue();

        commands.prefix.setValue(newPrefix);

        if (!oldPrefix.equalsIgnoreCase(commands.prefix.getValue())) {
            this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Set prefix to '" + ChatColor.AQUA.text() + newPrefix + ChatColor.DEFAULT.text() + "'!");
        }
    }
}
