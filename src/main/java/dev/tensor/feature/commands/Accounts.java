package dev.tensor.feature.commands;

import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.account.Account;
import dev.tensor.misc.imp.command.Command;

import java.util.Arrays;

/**
 * @author Hoosiers
 * @since 05-24-2026
 */

public final class Accounts implements Command {

    @Override
    public String getName() {
        return "Accounts";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName(),
                "Account",
                "SetAccount",
                "Alt",
                "Alts",
                "SetAlt"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {list/add/remove/active} {username} {current/server/null} {password/null}";
    }

    @Override
    public String getDescription() {
        return "Add, remove, set active, or list AltAccounts.";
    }

    @Override
    public void onCommand(String[] ARGS) {

        if (ARGS == null || ARGS.length == 0) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify AltAccount list (no arguments inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify AltAccount list (no arguments inputted)!");
            return;
        }

        if (Arrays.stream(this.acceptedFirstEntries).filter(s -> s.equalsIgnoreCase(firstEntry)).findFirst().orElse(null) == null) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify AltAccount list (unknown argument inputted)!");
            return;
        }

        if (firstEntry.equalsIgnoreCase("list")) {
            //todo
            return;
        }

        if (ARGS.length < 2) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify AltAccount list (no username inputted)!");
            return;
        }

        String secondEntry = ARGS[1];

        if (secondEntry.isEmpty() || secondEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify AltAccount list (no username inputted)!");
            return;
        }

        if (secondEntry.length() > Account.MAX_USERNAME_LENGTH) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify AltAccount list (username longer than 16 characters)!");
            return;
        }

        String thirdEntry = "";

        if (ARGS.length >= 3) {
            String server = ARGS[2];

            if (server.equalsIgnoreCase("current")) {
                thirdEntry = this.getWaypointServerName();

            } else {
                thirdEntry = server;
            }
        }

        String fourthEntry = "";

        if (ARGS.length >= 4) {
            String password = ARGS[3];

            if (password.length() > Account.MAX_PASSWORD_LENGTH) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify AltAccount list (password longer than 32 characters)!");
                return;
            }

            fourthEntry = password;
        }

        this.handleAccount(firstEntry, secondEntry, thirdEntry, fourthEntry);
    }

    private void handleAccount(String action, String username, String server, String password) {
        if (action.equalsIgnoreCase("add")) {
            Account account = this.getAccountManager().getAccount(username);
            boolean hasServer = !server.isEmpty() && !server.equalsIgnoreCase(" ");

            if (account != null && !hasServer) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to add account (account already exists)!");

            } else if (account == null) {

                if (hasServer) {
                    //create account  with server
                } else {
                    //create account  no server
                }

            } else {
                boolean isUpdated = !account.getPasswordForServer(server).equalsIgnoreCase(Account.DEFAULT_PASSWORD);

                if (isUpdated) {
                    //update password
                }

                //add password for new server
            }

        } else if (action.equalsIgnoreCase("remove")) {
            Account account = this.getAccountManager().getAccount(username);

            if (account == null) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to remove account (no account found for username '" + ChatColor.AQUA.text() + username + ChatColor.DEFAULT.text() + "')!");
                return;
            }

            boolean hasServer = !server.isEmpty() && !server.equalsIgnoreCase(" ");

            if (hasServer) {
                //remove server
            } else {
                //remove account
            }

        } else if (action.equalsIgnoreCase("active")) {
            Account account = this.getAccountManager().getAccount(username);

            if (account == null) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set active account (no account found for username '" + ChatColor.AQUA.text() + username + ChatColor.DEFAULT.text() + "')!");
            }

            //set active;
        }
    }

    private final String[] acceptedFirstEntries = new String[]{
            "add",
            "remove",
            "active",
            "list"
    };
}
