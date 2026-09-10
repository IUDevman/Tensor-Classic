package dev.tensor.feature.commands;

import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.command.Command;

/**
 * @author Hoosiers
 * @since 05-03-2026
 */

public final class Capes implements Command {

    @Override
    public String getName() {
        return "Capes";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName(),
                "Cape",
                "CapeList",
                "CapesList"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {list/add/remove} {player$name}";
    }

    @Override
    public String getDescription() {
        return "Returns the capes list or adds/removes a player from the capes list";
    }

    @Override
    public void onCommand(String[] ARGS) {
        if (ARGS == null || ARGS.length == 0) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify capes list (no value inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify capes list (no value inputted)!");
            return;
        }

        if (firstEntry.equalsIgnoreCase("List")) {
            this.sendClientMessageWithHeader(this.getName() + " (" + ChatColor.AQUA.text() + this.getCapeManager().getCapedPlayers().size() + ChatColor.DEFAULT.text() + "): " + this.getCapeManager().getCapedPlayers());

        } else if (firstEntry.equalsIgnoreCase("Add") || firstEntry.equalsIgnoreCase("Remove")) {

            if (ARGS.length == 1) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify capes list (no name inputted)!");
                return;
            }

            String secondEntry = ARGS[1];

            if (secondEntry == null || secondEntry.equalsIgnoreCase(" ")) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify capes list (no name inputted)!");
                return;
            }

            if (firstEntry.equalsIgnoreCase("Add")) {

                this.getCapeManager().addCapedPlayer(secondEntry);
                this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Added player '" + ChatColor.AQUA.text() + secondEntry + ChatColor.DEFAULT.text() + "' to the capes list!");

            } else {

                this.getCapeManager().removeCapedPlayer(secondEntry);
                this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Removed player '" + ChatColor.AQUA.text() + secondEntry + ChatColor.DEFAULT.text() + "' from the capes list!");
            }

        } else {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify capes list (unknown value '" + ChatColor.AQUA.text() + firstEntry + ChatColor.RED.text() + "' inputted)!");
        }
    }
}
