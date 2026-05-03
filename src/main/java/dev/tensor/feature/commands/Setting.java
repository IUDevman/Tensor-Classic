package dev.tensor.feature.commands;

import dev.tensor.feature.hacks.Commands;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.command.Command;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BlockListSetting;
import dev.tensor.misc.imp.setting.settings.EntityTypeListSetting;
import dev.tensor.misc.imp.setting.settings.WaypointListSetting;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

public final class Setting implements Command {

    @Override
    public String getName() {
        return "Setting";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName(),
                "HackSetting",
                "HackSettings",
                "HacksSetting",
                "HacksSettings",
                "HackSet",
                "HacksSet",
                "Set"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {hack} {list/setting_name} {value;$use$_$for$spaces$and$r_g_b_a$for$colors}";
    }

    @Override
    public String getDescription() {
        return "Sets setting values (or gives list) for specified hack. Use " + this.getHackManager().getHack(Commands.class).prefix.getValue() + "Options to change hack options.";
    }

    @Override
    public void onCommand(String[] ARGS) {

        if (ARGS == null || ARGS.length == 0) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify setting for hack (no hack name inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify setting for hack (no hack name inputted)!");
            return;
        }

        Hack hack = this.getHackManager().getHack(firstEntry);

        if (hack == null) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to find hack '" + ChatColor.AQUA.text() + firstEntry + ChatColor.RED.text() + "' for settings!");
            return;
        }

        if (ARGS.length == 1) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to find settings for hack'" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (no setting name or 'list' inputted)");
            return;
        }

        String secondEntry = ARGS[1];

        if (secondEntry == null || secondEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to find settings for hack'" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (invalid setting name inputted)");
            return;
        }

        if (hack.getSettings().isEmpty()) {
            this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Hack '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.DEFAULT.text() + "' does not have any settings!");
            return;
        }

        if (secondEntry.equalsIgnoreCase("list")) {
            ArrayList<String> settings = new ArrayList<>();

            hack.getSettings().forEach(setting -> settings.add(setting.getName() + " (" + ChatColor.AQUA.text() + setting.getValue().getClass().getSimpleName() + ChatColor.DEFAULT.text() + " value = " + setting.getValueString(true) + ChatColor.DEFAULT.text() + ")"));

            this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName() + ": Hack '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.DEFAULT.text() + "' settings list: " + settings);
            return;
        }

        dev.tensor.misc.imp.setting.Setting<?> setting = hack.getSettings().stream().filter(setting1 -> setting1.getName().equalsIgnoreCase(secondEntry.replace("_", " "))).findFirst().orElse(null);

        if (setting == null) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to find setting '" + ChatColor.AQUA.text() + secondEntry + ChatColor.RED.text() + "' for hack'" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (setting not found for name)!");
            return;
        }

        if (setting instanceof BlockListSetting) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify setting '" + ChatColor.AQUA.text() + setting.getName() + ChatColor.RED.text() + "' for hack'" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (can not modify BlockListSetting with command - use ClickGUI)!");
            return;
        }

        if (setting instanceof EntityTypeListSetting) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify setting '" + ChatColor.AQUA.text() + setting.getName() + ChatColor.RED.text() + "' for hack'" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (can not modify EntityTypeListSetting with command - use ClickGUI)!");
            return;
        }

        if (setting instanceof WaypointListSetting) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify setting '" + ChatColor.AQUA.text() + setting.getName() + ChatColor.RED.text() + "' for hack'" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (can not modify WaypointListSetting with command - use ClickGUI)!");
            return;
        }

        if (ARGS.length == 2) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify setting '" + ChatColor.AQUA.text() + setting.getName() + ChatColor.RED.text() + "' for hack'" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (no setting value inputted)!");
            return;
        }

        String thirdEntry = ARGS[2];

        if (thirdEntry == null || thirdEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to modify setting '" + ChatColor.AQUA.text() + setting.getName() + ChatColor.RED.text() + "' for hack'" + ChatColor.AQUA.text() + hack.NAME + ChatColor.RED.text() + "' (no setting value inputted)!");
            return;
        }

        try {
            setting.setValueFromString(thirdEntry);

            this.sendClientMessageWithHeader(ChatColor.DEFAULT.text() + this.getName()
                    + ": Hack '" + ChatColor.AQUA.text() + hack.NAME + ChatColor.DEFAULT.text()
                    + "' setting '" + ChatColor.AQUA.text() + setting.getName() + ChatColor.DEFAULT.text()
                    + "' " + ChatColor.AQUA.text() + setting.getValue().getClass().getSimpleName()
                    + ChatColor.DEFAULT.text() + " value set to '" + setting.getValueString(true)
                    + ChatColor.DEFAULT.text() + "'!");

        } catch (Exception ignored) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to set hack '" + ChatColor.AQUA.text() + hack.NAME + "' setting '" + ChatColor.AQUA.text() + setting.getName() + ChatColor.RED.text() + "' (invalid setting value '" + ChatColor.AQUA.text() + thirdEntry + ChatColor.RED.text() + "' inputted)!");
        }
    }
}
