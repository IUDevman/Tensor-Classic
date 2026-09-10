package dev.tensor.feature.commands;

import dev.tensor.misc.helpers.text.ChatColor;
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
        return new String[]{
                this.getName(),
                "Friend",
                "FriendList",
                "FriendsList"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {list/add/remove} {player$name}";
    }

    @Override
    public String getDescription() {
        return "Returns the list of friends or adds/removes a player from the friends list";
    }

    @Override
    public void onCommand(String[] ARGS) {
        if (ARGS == null || ARGS.length == 0) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify friends list (no value inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify friends list (no value inputted)!");
            return;
        }

        if (firstEntry.equalsIgnoreCase("List")) {
            this.sendClientMessageWithHeader(this.getName() + " (" + ChatColor.AQUA.text() + this.getFriendManager().getFriends().size() + ChatColor.DEFAULT.text() + "): " + this.getFriendManager().getFriends());

        } else if (firstEntry.equalsIgnoreCase("Add") || firstEntry.equalsIgnoreCase("Remove")) {

            if (ARGS.length == 1) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify friends list (no name inputted)!");
                return;
            }

            String secondEntry = ARGS[1];

            if (secondEntry == null || secondEntry.equalsIgnoreCase(" ")) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify friends list (no name inputted)!");
                return;
            }

            if (firstEntry.equalsIgnoreCase("Add")) {

                this.getFriendManager().addFriend(secondEntry);
                this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Added friend '" + ChatColor.AQUA.text() + secondEntry + ChatColor.DEFAULT.text() + "' to the friends list!");

            } else {

                this.getFriendManager().removeFriend(secondEntry);
                this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Removed friend '" + ChatColor.AQUA.text() + secondEntry + ChatColor.DEFAULT.text() + "' from the friends list!");
            }

        } else {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify friends list (unknown value '" + ChatColor.AQUA.text() + firstEntry + ChatColor.RED.text() + "' inputted)!");
        }
    }
}
