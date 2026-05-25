package dev.tensor.feature.commands;

import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.command.Command;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.Setting;
import dev.tensor.misc.imp.setting.settings.*;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class Reset implements Command {

    @Override
    public String getName() {
        return "Reset";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName(),
                "ResetHack",
                "ResetSetting"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {hack$name} {setting$name}";
    }

    @Override
    public String getDescription() {
        return "Resets hacks or hack settings for the client. Do not input a setting name to reset the entire hack.";
    }

    @Override
    public void onCommand(String[] ARGS) {

        if (ARGS == null || ARGS.length == 0) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to reset hack or setting (no name inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to reset hack or setting (no name inputted)!");
            return;
        }

        Hack hack = this.getHackManager().getHack(firstEntry);

        if (hack == null) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to find hack '" + ChatColor.AQUA.text() + firstEntry + ChatColor.RED.text() + "' to reset!");
            return;
        }

        if (ARGS.length == 1) {

            hack.reset();
            this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Reset hack '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.DEFAULT.text() + "'!");
            return;
        }

        String secondEntry = ARGS[1];

        if (secondEntry == null || secondEntry.equalsIgnoreCase(" ")) {

            hack.reset();
            this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Reset hack '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.DEFAULT.text() + "'!");
            return;
        }

        Setting<?> setting = hack.getSettings().stream().filter(setting1 -> setting1.getName().equalsIgnoreCase(secondEntry)).findFirst().orElse(null);

        if (setting == null) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to find setting '" + ChatColor.AQUA.text() + secondEntry + ChatColor.RED.text() + "' to reset for hack '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "'!");
            return;
        }

        if (setting instanceof BlockListSetting) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to reset setting '" + ChatColor.AQUA.text() + setting.getName() + ChatColor.RED.text() + "' for hack'" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (can not reset BlockListSetting with command - use ClickGUI)!");
            return;
        }

        if (setting instanceof EntityTypeListSetting) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to reset setting '" + ChatColor.AQUA.text() + setting.getName() + ChatColor.RED.text() + "' for hack'" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (can not reset EntityTypeListSetting with command - use ClickGUI)!");
            return;
        }

        if (setting instanceof WaypointListSetting) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to reset setting '" + ChatColor.AQUA.text() + setting.getName() + ChatColor.RED.text() + "' for hack'" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (can not reset WaypointListSetting with command - use ClickGUI)!");
            return;
        }

        if (setting instanceof GeneralButtonSetting) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to reset setting '" + ChatColor.AQUA.text() + setting.getName() + ChatColor.RED.text() + "' for hack'" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (can not reset ButtonSetting with command - use ClickGUI)!");
            return;
        }

        if (setting instanceof PacketTypeListSetting) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to reset setting '" + ChatColor.AQUA.text() + setting.getName() + ChatColor.RED.text() + "' for hack'" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (can not reset PacketTypeListSetting with command - use ClickGUI)!");
            return;
        }

        setting.reset();
        this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Reset setting '" + ChatColor.AQUA.text() + setting.getName() + ChatColor.DEFAULT.text() + "' for hack '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.DEFAULT.text() + "'!");
    }
}
