package dev.tensor.feature.commands;

import dev.tensor.misc.imp.command.Command;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

public final class Friends implements Command {

    @Override
    public String getName() {
        return "Friends";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[] {
                this.getName(),
                "Friend",
                "FriendList",
                "FriendsList"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {list/add/remove} {player name}";
    }

    @Override
    public String getDescription() {
        return "Returns the list of friends or adds/removes a player from the friends list";
    }

    @Override
    public void onCommand(String[] ARGS) {
        if (ARGS == null || ARGS.length == 0) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify friends list (no value inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify friends list (no value inputted)!");
            return;
        }

        if (firstEntry.equalsIgnoreCase("List")) {
            this.sendClientMessageWithPrefix(this.getName() + " (" + this.getColor(ChatColor.AQUA) + this.getFriendManager().getFriends().size() + this.getColor(ChatColor.DEFAULT) + "): " + this.getFriendManager().getFriends());

        } else if (firstEntry.equalsIgnoreCase("Add") || firstEntry.equalsIgnoreCase("Remove")) {

            if (ARGS.length == 1) {
                this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify friends list (no name inputted)!");
                return;
            }

            String secondEntry = ARGS[1];

            if (secondEntry == null || secondEntry.equalsIgnoreCase(" ")) {
                this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify friends list (no name inputted)!");
                return;
            }

            if (firstEntry.equalsIgnoreCase("Add")) {

                this.getFriendManager().addFriend(secondEntry);
                this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Added friend '" + this.getColor(ChatColor.AQUA) + secondEntry + this.getColor(ChatColor.DEFAULT) + "' to the friends list!");

            } else {

                this.getFriendManager().removeFriend(secondEntry);
                this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Removed friend '" + this.getColor(ChatColor.AQUA) + secondEntry + this.getColor(ChatColor.DEFAULT) + "' from the friends list!");
            }

        } else {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify friends list (unknown value '" + this.getColor(ChatColor.AQUA) + firstEntry + this.getColor(ChatColor.RED) + "' inputted)!");
        }
    }
}
