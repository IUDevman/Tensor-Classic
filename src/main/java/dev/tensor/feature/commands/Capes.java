package dev.tensor.feature.commands;

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
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify capes list (no value inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify capes list (no value inputted)!");
            return;
        }

        if (firstEntry.equalsIgnoreCase("List")) {
            this.sendClientMessageWithHeader(this.getName() + " (" + this.getColor(ChatColor.AQUA) + this.getCapeManager().getCapedPlayers().size() + this.getColor(ChatColor.DEFAULT) + "): " + this.getCapeManager().getCapedPlayers());

        } else if (firstEntry.equalsIgnoreCase("Add") || firstEntry.equalsIgnoreCase("Remove")) {

            if (ARGS.length == 1) {
                this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify capes list (no name inputted)!");
                return;
            }

            String secondEntry = ARGS[1];

            if (secondEntry == null || secondEntry.equalsIgnoreCase(" ")) {
                this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify capes list (no name inputted)!");
                return;
            }

            if (firstEntry.equalsIgnoreCase("Add")) {

                this.getCapeManager().addCapedPlayer(secondEntry);
                this.sendClientMessageWithHeader(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Added player '" + this.getColor(ChatColor.AQUA) + secondEntry + this.getColor(ChatColor.DEFAULT) + "' to the capes list!");

            } else {

                this.getCapeManager().removeCapedPlayer(secondEntry);
                this.sendClientMessageWithHeader(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Removed player '" + this.getColor(ChatColor.AQUA) + secondEntry + this.getColor(ChatColor.DEFAULT) + "' from the capes list!");
            }

        } else {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify capes list (unknown value '" + this.getColor(ChatColor.AQUA) + firstEntry + this.getColor(ChatColor.RED) + "' inputted)!");
        }
    }
}
