package dev.tensor.feature.commands;

import dev.tensor.feature.hacks.Commands;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.command.Command;
import dev.tensor.misc.imp.hack.Hack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

public final class Options implements Command {

    @Override
    public String getName() {
        return "Options";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                "HackOption",
                "HackOptions",
                "HacksOption",
                "HacksOptions",
                "Option"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {hack$name} {enabled/drawn/messages/bind} {value}";
    }

    @Override
    public String getDescription() {
        return "Sets hack options (enabled/drawn/messages/bind) for specified hack. Use " + this.getHackManager().getHack(Commands.class).prefix.getValue() + "Setting to change hack settings.";
    }

    @Override
    public void onCommand(String[] ARGS) {

        if (ARGS == null || ARGS.length == 0) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set hack option (no hack name inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set hack option (no hack name inputted)!");
            return;
        }

        Hack hack = this.getHackManager().getHack(firstEntry);

        if (hack == null) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set hack option for hack '" + ChatColor.AQUA.text() + firstEntry + "' (no such hack found)!");
            return;
        }

        if (ARGS.length == 1) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set hack option for '" + ChatColor.AQUA.text() + hack.NAME + "' (no option name inputted)!");
            return;
        }

        String secondEntry = ARGS[1];

        if (secondEntry == null || secondEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set hack option for '" + ChatColor.AQUA.text() + hack.NAME + "' (no option name inputted)!");
            return;
        }

        if (secondEntry.equalsIgnoreCase("list")) {
            ArrayList<String> options = new ArrayList<>();

            options.add((hack.isEnabled() ? ChatColor.GREEN.text() : ChatColor.RED.text()) + "Enabled" + ChatColor.DEFAULT.text());
            options.add((hack.DRAWN ? ChatColor.GREEN.text() : ChatColor.RED.text()) + "Drawn" + ChatColor.DEFAULT.text());
            options.add((hack.MESSAGES ? ChatColor.GREEN.text() : ChatColor.RED.text()) + "Messages" + ChatColor.DEFAULT.text());
            options.add("Bind = [" + ChatColor.AQUA.text() + Keyboard.getKeyName(hack.BIND) + ChatColor.DEFAULT.text() + "]");

            this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Hack '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.DEFAULT.text() + "' options list: " + options);
            return;
        }

        if (!this.acceptedOptions().contains(secondEntry.toLowerCase())) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set hack option '" + ChatColor.AQUA.text() + secondEntry + "' for '" + ChatColor.AQUA.text() + hack.NAME + "' (invalid option name inputted)!");
            return;
        }

        if (ARGS.length == 2) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set hack option '" + ChatColor.AQUA.text() + secondEntry + "' for '" + ChatColor.AQUA.text() + hack.NAME + "' (no option value inputted)!");
            return;
        }

        String thirdEntry = ARGS[2];

        if (thirdEntry == null || thirdEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set hack option '" + ChatColor.AQUA.text() + secondEntry + "' for '" + ChatColor.AQUA.text() + hack.NAME + "' (no option value inputted)!");
            return;
        }

        try {
            switch (secondEntry.toLowerCase()) {
                case "enabled":
                    hack.setEnabled(Boolean.parseBoolean(thirdEntry));
                    this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Set option '" + ChatColor.AQUA.text() + "Enabled" + ChatColor.DEFAULT.text() + "' to '" + (hack.isEnabled() ? ChatColor.GREEN.text() + "true" : ChatColor.RED.text() + "false") + ChatColor.DEFAULT.text() + "' for hack '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.DEFAULT.text() + "'!");
                    break;
                case "drawn":
                    hack.DRAWN = Boolean.parseBoolean(thirdEntry);
                    this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Set option '" + ChatColor.AQUA.text() + "Drawn" + ChatColor.DEFAULT.text() + "' to '" + (hack.DRAWN ? ChatColor.GREEN.text() + "true" : ChatColor.RED.text() + "false") + ChatColor.DEFAULT.text() + "' for hack '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.DEFAULT.text() + "'!");
                    break;
                case "messages":
                    hack.MESSAGES = Boolean.parseBoolean(thirdEntry);
                    this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Set option '" + ChatColor.AQUA.text() + "Messages" + ChatColor.DEFAULT.text() + "' to '" + (hack.MESSAGES ? ChatColor.GREEN.text() + "true" : ChatColor.RED.text() + "false") + ChatColor.DEFAULT.text() + "' for hack '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.DEFAULT.text() + "'!");
                    break;
                case "bind":
                    hack.BIND = Integer.parseInt(thirdEntry);
                    this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Set option '" + ChatColor.AQUA.text() + "Bind" + ChatColor.DEFAULT.text() + "' to '[" + ChatColor.AQUA.text() + Keyboard.getKeyName(hack.BIND) + ChatColor.DEFAULT.text() + "]' for hack '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.DEFAULT.text() + "'!");
                    break;
            }

        } catch (Exception ignored) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set hack option '" + ChatColor.AQUA.text() + secondEntry + "' for '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (invalid option value inputted)!");
        }
    }

    private ArrayList<String> acceptedOptions() {
        ArrayList<String> options = new ArrayList<>();

        options.add("enabled");
        options.add("drawn");
        options.add("messages");
        options.add("bind");

        return options;
    }
}
