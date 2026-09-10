package dev.tensor.feature.commands;

import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.command.Command;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

public final class Commands implements Command {

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
    public String getRegex() {
        return "{name} {page}";
    }

    @Override
    public String getDescription() {
        return "Returns the list of available commands.";
    }

    @Override
    public void onCommand(String[] ARGS) {
        ArrayList<Command> COMMANDS = this.getCommandManager().getCommands();

        if (COMMANDS.isEmpty()) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to list commands (no commands found)!");
        }

        int pageToSend = 1;

        if (ARGS != null && ARGS.length >= 1) {
            String pageText = ARGS[0];

            if (pageText != null && !pageText.equalsIgnoreCase(" ")) {
                try {
                    pageToSend = Integer.parseInt(pageText);

                } catch (Exception ignored) {
                    this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to list commands (invalid page value inputted)!");
                    return;
                }

                if (pageToSend < 1) {
                    this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to list commands (page value less than 1)!");
                    return;
                }
            }
        }

        //King arraylist ?
        ArrayList<ArrayList<String>> COMMAND_PAGES = new ArrayList<>();

        int size = COMMANDS.size();

        double pages = size / 4d;
        int maxPages = (int) Math.ceil(pages);

        if (pageToSend > maxPages) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to list commands (page value greater than max page '" + ChatColor.AQUA.text() + maxPages + ChatColor.RED.text() + "'!");
            return;
        }

        for (int page = 1; page <= maxPages; page++) {

            ArrayList<String> COMMANDS_FOR_PAGE = new ArrayList<>();

            for (int i = (4 * (page - 1)); i < page * 4; i++) {

                if (i >= COMMANDS.size()) {
                    break;
                }

                Command command = COMMANDS.get(i);

                if (command == null) {
                    continue;
                }

                String commandString = ChatColor.YELLOW.text() + command.getName() +
                        ChatColor.DEFAULT.text() + ": " + command.getDescription() + ChatColor.RED.text() + " Regex: " +
                        command.getRegex().replace("$", " ").replace("{name}", this.getHackManager().getHack(dev.tensor.feature.hacks.Commands.class).prefix.getValue() + command.getName()) +
                        ChatColor.DEFAULT.text();

                COMMANDS_FOR_PAGE.add(commandString);
            }

            COMMAND_PAGES.add(COMMANDS_FOR_PAGE);
        }

        if (pageToSend - 1 > COMMAND_PAGES.size()) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to list commands (page value greater than max page '" + ChatColor.AQUA.text() + maxPages + ChatColor.RED.text() + "'!");
            return;
        }

        String MESSAGE_PRE = this.getName() + " (" + ChatColor.AQUA.text() +
                COMMANDS.size() + ChatColor.DEFAULT.text() + ") (" + ChatColor.YELLOW.text() +
                "Page " + pageToSend + "/" + maxPages + ChatColor.DEFAULT.text() + "): ";

        this.sendClientMessageWithHeader(MESSAGE_PRE + COMMAND_PAGES.get(pageToSend - 1));
    }
}
