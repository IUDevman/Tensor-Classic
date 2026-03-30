package dev.tensor.feature.commands;

import dev.tensor.misc.imp.command.SimpleCallableCommand;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

public final class Commands extends SimpleCallableCommand {
    @Override
    public String getName() {
        return "Commands";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName(),
                "CommandsList",
                "Command",
                "CommandList"
        };
    }

    @Override
    public String getDescription() {
        return "Returns the list of available commands.";
    }

    @Override
    public void onCommandTriggered() {

        String MESSAGE_PRE = this.getName() + " (" + this.getColor(ChatColor.AQUA) + this.getCommandManager().getCommands().size() + this.getColor(ChatColor.DEFAULT) + "): ";

        ArrayList<String> COMMANDS = new ArrayList<>();

        this.getCommandManager().getCommands().forEach(command -> {

            String commandString = this.getColor(ChatColor.YELLOW) + command.getName() +
                    this.getColor(ChatColor.DEFAULT) + ": " + command.getDescription() + this.getColor(ChatColor.RED) + " Regex: " +
                    command.getRegex().replace("$", " ").replace("{name}", this.getHackManager().getHack(dev.tensor.feature.hacks.Commands.class).prefix.getValue() + command.getName()) +
                    this.getColor(ChatColor.DEFAULT);

            COMMANDS.add(commandString);
        });

        this.sendClientMessageWithPrefix(MESSAGE_PRE + COMMANDS);
    }
}
