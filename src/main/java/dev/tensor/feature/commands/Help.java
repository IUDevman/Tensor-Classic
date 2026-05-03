package dev.tensor.feature.commands;

import dev.tensor.TensorClassic;
import dev.tensor.feature.hacks.Commands;
import dev.tensor.misc.helpers.ChatColor;
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
        return new String[]{
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
                " " + TensorClassic.MOD_VERSION + "! The default command prefix is '" +
                ChatColor.AQUA.text() + clientPrefix +
                ChatColor.DEFAULT.text() + "'. Type " + ChatColor.AQUA.text() +
                clientPrefix + "Hacks" + ChatColor.DEFAULT.text() + " for the list of available hacks and " +
                ChatColor.AQUA.text() + clientPrefix + "Commands" + ChatColor.DEFAULT.text() +
                " for the list of available commands!";

        this.sendClientMessageWithHeader(helpString);
    }
}
