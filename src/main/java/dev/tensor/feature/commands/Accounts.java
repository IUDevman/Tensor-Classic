package dev.tensor.feature.commands;

import dev.tensor.feature.hacks.AltAccounts;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.account.Account;
import dev.tensor.misc.imp.command.Command;

import java.util.ArrayList;
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
            ArrayList<String> accounts = new ArrayList<>();

            this.getAccountManager().getAccounts().forEach(account -> accounts.add(this.generateAccountString(account)));

            this.sendClientMessageWithHeader(this.getName() + " (" + ChatColor.AQUA.text() + accounts.size() + ChatColor.DEFAULT.text() + "): " + accounts);
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

    private String generateAccountString(Account account) {
        String toReturn = "";

        toReturn += "Username = " + ChatColor.YELLOW.text() + account.getUsername() + ChatColor.DEFAULT.text() + ", ";

        toReturn += "Session = " + (account.isSessionAccount() ? ChatColor.GREEN.text() + "True" : ChatColor.RED.text() + "False") + ChatColor.DEFAULT.text() + ", ";

        toReturn += "Active = " + (this.getAccountManager().isActiveAccount(account) ? ChatColor.GREEN.text() + "True" : ChatColor.RED.text() + "False") + ChatColor.DEFAULT.text() + ", ";

        ArrayList<String> passwords = new ArrayList<>();

        account.getServerPasswordsMap().forEach((s, s2) -> passwords.add(s + " - " + s2));

        toReturn += "Passwords = " + passwords + ".";

        return toReturn;
    }

    private void handleAccount(String action, String username, String server, String password) {
        if (action.equalsIgnoreCase("add")) {
            Account account = this.getAccountManager().getAccount(username);
            boolean hasServer = !server.isEmpty() && !server.equalsIgnoreCase(" ");
            boolean hasPassword = !password.isEmpty() && !password.equalsIgnoreCase(" ");

            if (account != null && !hasServer) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to add account (account already exists)!");

            } else if (account == null) {

                if (hasServer && hasPassword) {
                    Account toAdd = new Account(username);
                    toAdd.addPassword(server, password);

                    this.getAccountManager().addAccount(toAdd);
                    this.sendClientMessageWithHeader(this.getName() + ": Added account with username '" + ChatColor.AQUA.text() + username + ChatColor.DEFAULT.text() + "' and password '" + ChatColor.YELLOW.text() + password + ChatColor.DEFAULT.text() + "' for server '" + ChatColor.YELLOW.text() + server + ChatColor.DEFAULT.text() + "' to the AltAccount list!");

                } else {
                    Account toAdd = new Account(username);

                    this.getAccountManager().addAccount(toAdd);
                    this.sendClientMessageWithHeader(this.getName() + ": Added account with username '" + ChatColor.AQUA.text() + username + ChatColor.DEFAULT.text() + "' to the AltAccount list!");
                }

            } else {
                boolean isUpdated = !account.getPasswordForServer(server).equalsIgnoreCase(Account.DEFAULT_PASSWORD);

                if (isUpdated && hasPassword) {
                    account.addPassword(server, password);
                    this.sendClientMessageWithHeader(this.getName() + ": Updated server '" + ChatColor.YELLOW.text() + server + ChatColor.DEFAULT.text() + "' with password '" + ChatColor.AQUA.text() + password + ChatColor.DEFAULT.text() + "' for account '" + ChatColor.YELLOW.text() + username + ChatColor.DEFAULT.text() + "'!");
                    return;
                }

                account.addPassword(server, password);
                this.sendClientMessageWithHeader(this.getName() + ": Added password '" + ChatColor.AQUA.text() + password + ChatColor.DEFAULT.text() + "' for server '" + ChatColor.YELLOW.text() + server + ChatColor.DEFAULT.text() + "' for account '" + ChatColor.YELLOW.text() + username + ChatColor.DEFAULT.text() + "'!");
            }

        } else if (action.equalsIgnoreCase("remove")) {
            Account account = this.getAccountManager().getAccount(username);

            if (account == null) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to remove account (no account found for username '" + ChatColor.AQUA.text() + username + ChatColor.DEFAULT.text() + "')!");
                return;
            }

            boolean hasServer = !server.isEmpty() && !server.equalsIgnoreCase(" ");

            if (hasServer) {
                account.removePasswordForServer(server);
                this.sendClientMessageWithHeader(this.getName() + ": Deleted password for server '" + ChatColor.AQUA.text() + server + ChatColor.DEFAULT.text() + "' for account '" + ChatColor.YELLOW.text() + username + ChatColor.DEFAULT.text() + "'!");

            } else {
                if (account.isSessionAccount()) {
                    this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to remove account (is session account)!");
                    return;
                }

                if (this.getAccountManager().isActiveAccount(account)) {
                    this.triggerChangeMessage();
                }

                this.getAccountManager().deleteAccount(account);
                this.sendClientMessageWithHeader(this.getName() + ": Deleted account with username '" + ChatColor.AQUA.text() + username + ChatColor.DEFAULT.text() + "' from the AltAccount list!");
            }

        } else if (action.equalsIgnoreCase("active")) {
            Account account = this.getAccountManager().getAccount(username);

            if (account == null) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set active account (no account found for username '" + ChatColor.AQUA.text() + username + ChatColor.DEFAULT.text() + "')!");
                return;
            }

            this.getAccountManager().setActiveAccount(account);
            this.triggerChangeMessage();
            this.sendClientMessageWithHeader(this.getName() + ": Set account with username '" + ChatColor.AQUA.text() + username + ChatColor.DEFAULT.text() + "' as the active account!");
        }
    }

    private void triggerChangeMessage() {
        AltAccounts altAccounts = this.getHackManager().getHack(AltAccounts.class);

        if (altAccounts != null && altAccounts.isEnabled()) {
            altAccounts.buttonWindow.onTriggerChange();
        }
    }

    private final String[] acceptedFirstEntries = new String[]{
            "add",
            "remove",
            "active",
            "list"
    };
}
