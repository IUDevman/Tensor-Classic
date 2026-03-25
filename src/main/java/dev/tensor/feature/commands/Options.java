package dev.tensor.feature.commands;

import dev.tensor.feature.hacks.Commands;
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
        return new String[] {
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
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to set hack option (no hack name inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to set hack option (no hack name inputted)!");
            return;
        }

        Hack hack = this.getHackManager().getHack(firstEntry);

        if (hack == null) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to set hack option for hack '" + this.getColor(ChatColor.AQUA) + firstEntry + "' (no such hack found)!");
            return;
        }

        if (ARGS.length == 1) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to set hack option for '" + this.getColor(ChatColor.AQUA) + hack.NAME + "' (no option name inputted)!");
            return;
        }

        String secondEntry = ARGS[1];

        if (secondEntry == null || secondEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to set hack option for '" + this.getColor(ChatColor.AQUA) + hack.NAME + "' (no option name inputted)!");
            return;
        }

        if (secondEntry.equalsIgnoreCase("list")) {
            ArrayList<String> options = new ArrayList<>();

            options.add((hack.isEnabled() ? this.getColor(ChatColor.GREEN) : this.getColor(ChatColor.RED)) + "Enabled" + this.getColor(ChatColor.DEFAULT));
            options.add((hack.DRAWN ? this.getColor(ChatColor.GREEN) : this.getColor(ChatColor.RED)) + "Drawn" + this.getColor(ChatColor.DEFAULT));
            options.add((hack.MESSAGES ? this.getColor(ChatColor.GREEN) : this.getColor(ChatColor.RED)) + "Messages" + this.getColor(ChatColor.DEFAULT));
            options.add("Bind = [" + this.getColor(ChatColor.AQUA) + Keyboard.getKeyName(hack.BIND) + this.getColor(ChatColor.DEFAULT) + "]");

            this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.DEFAULT) + "' options list: " + options);
            return;
        }

        if (!this.acceptedOptions().contains(secondEntry.toLowerCase())) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to set hack option '" + this.getColor(ChatColor.AQUA) + secondEntry + "' for '" + this.getColor(ChatColor.AQUA) + hack.NAME + "' (invalid option name inputted)!");
            return;
        }

        if (ARGS.length == 2) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to set hack option '" + this.getColor(ChatColor.AQUA) + secondEntry + "' for '" + this.getColor(ChatColor.AQUA) + hack.NAME + "' (no option value inputted)!");
            return;
        }

        String thirdEntry = ARGS[2];

        if (thirdEntry == null || thirdEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to set hack option '" + this.getColor(ChatColor.AQUA) + secondEntry + "' for '" + this.getColor(ChatColor.AQUA) + hack.NAME + "' (no option value inputted)!");
            return;
        }

        try {
            switch (secondEntry.toLowerCase()) {
                case "enabled":
                    hack.setEnabled(Boolean.parseBoolean(thirdEntry));
                    this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Set option '" + this.getColor(ChatColor.AQUA) + "Enabled" + this.getColor(ChatColor.DEFAULT) + "' to '" + (hack.isEnabled() ? this.getColor(ChatColor.GREEN) + "true" : this.getColor(ChatColor.RED) + "false") + this.getColor(ChatColor.DEFAULT) + "' for hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.DEFAULT) + "'!");
                    break;
                case "drawn":
                    hack.DRAWN = Boolean.parseBoolean(thirdEntry);
                    this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Set option '" + this.getColor(ChatColor.AQUA) + "Drawn" + this.getColor(ChatColor.DEFAULT) + "' to '" + (hack.DRAWN ? this.getColor(ChatColor.GREEN) + "true" : this.getColor(ChatColor.RED) + "false") + this.getColor(ChatColor.DEFAULT) + "' for hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.DEFAULT) + "'!");
                    break;
                case "messages":
                    hack.MESSAGES = Boolean.parseBoolean(thirdEntry);
                    this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Set option '" + this.getColor(ChatColor.AQUA) + "Messages" + this.getColor(ChatColor.DEFAULT) + "' to '" + (hack.MESSAGES ? this.getColor(ChatColor.GREEN) + "true" : this.getColor(ChatColor.RED)  + "false") + this.getColor(ChatColor.DEFAULT) + "' for hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.DEFAULT) + "'!");
                    break;
                case "bind":
                    hack.BIND = Integer.parseInt(thirdEntry);
                    this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Set option '" + this.getColor(ChatColor.AQUA) + "Bind" + this.getColor(ChatColor.DEFAULT) + "' to '[" + this.getColor(ChatColor.AQUA) + Keyboard.getKeyName(hack.BIND) + this.getColor(ChatColor.DEFAULT) + "]' for hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.DEFAULT) + "'!");
                    break;
            }

        } catch (Exception ignored) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to set hack option '" + this.getColor(ChatColor.AQUA) + secondEntry + "' for '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.RED) + "' (invalid option value inputted)!");
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
