package dev.tensor.feature.commands;

import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.command.Command;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 07-05-2026
 */

public final class Spammer implements Command {

    @Override
    public String getName() {
        return "Spammer";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName(),
                "SpammerFile",
                "Spam",
                "SpamFile"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {list/set/reset} {file_name}";
    }

    @Override
    public String getDescription() {
        return "Modify the spammer file parameters for the Spammer hack.";
    }

    @Override
    public void onCommand(String[] ARGS) {
        if (ARGS == null || ARGS.length < 1) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify spammer file list (no arguments inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.isEmpty() || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify spammer file list (no arguments inputted)!");
            return;
        }

        dev.tensor.feature.hacks.Spammer spammer = this.getHackManager().getHack(dev.tensor.feature.hacks.Spammer.class);

        if (spammer == null) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify spammer file list (spammer hack not found)!");
            return;
        }

        if (firstEntry.equalsIgnoreCase("list")) {
            ArrayList<String> files = new ArrayList<>();

            spammer.spammerList.getValue().getFileNames().forEach(file -> {

                String fileName = (spammer.spammerList.getValue().isCurrentSpammer(file) ? ChatColor.GREEN.text() : ChatColor.WHITE.text()) + file + ".txt" + ChatColor.WHITE.text();

                files.add(fileName);
            });

            this.sendClientMessageWithHeader(this.getName() + "File List (" + ChatColor.AQUA.text() + files.size() + ChatColor.DEFAULT.text() + "): " + files);

        } else if (firstEntry.equalsIgnoreCase("reset")) {
            spammer.refresh.onChange();
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": " + ChatColor.AQUA.text() + "Reset " + ChatColor.DEFAULT.text() + "spammer file list!");

        } else if (firstEntry.equalsIgnoreCase("set")) {
            if (ARGS.length < 2) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify spammer file list (no name inputted)!");
                return;
            }

            String secondEntry = ARGS[1];

            if (secondEntry == null || secondEntry.isEmpty() || secondEntry.equalsIgnoreCase(" ")) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify spammer file list (no name inputted)!");
                return;
            }

            int result = spammer.spammerList.getValue().setCurrentSpammer(secondEntry, false);

            switch (result) {
                case -1:
                    this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set spammer file (no name inputted)!");
                    break;
                case 0:
                    this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set spammer file (file already set)!");
                    break;
                case 1:
                    this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set spammer file (no files loaded)!");
                    break;
                case 2:
                case 3:
                    this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set spammer file (file not found)!");
                    break;
                case 4:
                    this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set spammer file (unknown error)!");
                    break;
                case 5:
                    this.sendClientMessageWithHeader(this.getName() + ": Set active spammer file to '" + ChatColor.AQUA.text() + spammer.spammerList.getValue().getCurrentSpammerFileName() + ".txt'" + ChatColor.DEFAULT.text() + "'!");
                    break;
            }

        } else {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify spammer file list (invalid argument inputted)!");
        }
    }
}
