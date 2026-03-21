package dev.tensor.feature.commands;

import dev.tensor.TensorClassic;
import dev.tensor.feature.hacks.Commands;
import dev.tensor.misc.imp.command.SimpleCallableCommand;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

public final class Help extends SimpleCallableCommand {

    @Override
    public String getName() {
        return "Help";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[] {
                this.getName(),
                "ClientHelp",
                "ClientInfo"
        };
    }

    @Override
    public String getDescription() {
        return "Gives important information about the client.";
    }

    @Override
    public void onCommandTriggered() {

        String clientPrefix = this.getHackManager().getHack(Commands.class).prefix.getValue();

        //todo: include ClickGUI bind once we make it
        String helpString = this.getName() + ": Welcome to " + TensorClassic.MOD_NAME +
                " " +  TensorClassic.MOD_VERSION +"! The default command prefix is '" +
                this.getColor(ChatColor.AQUA) + clientPrefix +
                this.getColor(ChatColor.DEFAULT) + "'. Type " + this.getColor(ChatColor.AQUA) +
                clientPrefix + "Hacks" + this.getColor(ChatColor.DEFAULT) + " for the list of available hacks and " +
                this.getColor(ChatColor.AQUA) + clientPrefix + "Commands"  + this.getColor(ChatColor.DEFAULT) +
                " for the list of available commands!";

        this.sendClientMessageWithPrefix(helpString);
    }
}
