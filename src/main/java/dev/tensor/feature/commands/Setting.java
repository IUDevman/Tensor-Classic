package dev.tensor.feature.commands;

import dev.tensor.feature.hacks.Commands;
import dev.tensor.misc.imp.command.Command;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BlockListSetting;

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
        return new String[] {
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
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify setting for hack (no hack name inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify setting for hack (no hack name inputted)!");
            return;
        }

        Hack hack = this.getHackManager().getHack(firstEntry);

        if (hack == null) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to find hack '" + this.getColor(ChatColor.AQUA) + firstEntry + this.getColor(ChatColor.RED) + "' for settings!");
            return;
        }

        if (ARGS.length == 1) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to find settings for hack'" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.RED) + "' (no setting name or 'list' inputted)");
            return;
        }

        String secondEntry = ARGS[1];

        if (secondEntry == null || secondEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to find settings for hack'" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.RED) + "' (invalid setting name inputted)");
            return;
        }

        if (hack.getSettings().isEmpty()) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.DEFAULT) + "' does not have any settings!");
            return;
        }

        if (secondEntry.equalsIgnoreCase("list")) {
            ArrayList<String> settings = new ArrayList<>();

            hack.getSettings().forEach(setting -> settings.add(setting.getName() + " (" + this.getColor(ChatColor.AQUA) + setting.getValue().getClass().getSimpleName() + this.getColor(ChatColor.DEFAULT) + " value = " + setting.getValueString(true) + this.getColor(ChatColor.DEFAULT) + ")"));

            this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.DEFAULT) + "' settings list: " + settings);
            return;
        }

        dev.tensor.misc.imp.setting.Setting<?> setting = hack.getSettings().stream().filter(setting1 -> setting1.getName().equalsIgnoreCase(secondEntry.replace("_", " "))).findFirst().orElse(null);

        if (setting == null) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to find setting '" + this.getColor(ChatColor.AQUA) + secondEntry + this.getColor(ChatColor.RED) + "' for hack'" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.RED) + "' (setting not found for name)!");
            return;
        }

        if (setting instanceof BlockListSetting) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify setting '" + this.getColor(ChatColor.AQUA) + setting.getName() + this.getColor(ChatColor.RED) + "' for hack'" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.RED) + "' (can not modify BlockListSetting with command - use ClickGUI)!");
            return;
        }

        if (ARGS.length == 2) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify setting '" + this.getColor(ChatColor.AQUA) + setting.getName() + this.getColor(ChatColor.RED) + "' for hack'" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.RED) + "' (no setting value inputted)!");
            return;
        }

        String thirdEntry = ARGS[2];

        if (thirdEntry == null || thirdEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify setting '" + this.getColor(ChatColor.AQUA) + setting.getName() + this.getColor(ChatColor.RED) + "' for hack'" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.RED) + "' (no setting value inputted)!");
            return;
        }

        try {
            setting.setValueFromString(thirdEntry);

            this.sendClientMessageWithPrefix(this.getColor(ChatColor.DEFAULT) + this.getName()
                    + ": Hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + this.getColor(ChatColor.DEFAULT)
                    + "' setting '" + this.getColor(ChatColor.AQUA) + setting.getName() + this.getColor(ChatColor.DEFAULT)
                    + "' " + this.getColor(ChatColor.AQUA) + setting.getValue().getClass().getSimpleName()
                    + this.getColor(ChatColor.DEFAULT) + " value set to '" + setting.getValueString(true)
                    + this.getColor(ChatColor.DEFAULT) + "'!");

        } catch (Exception ignored) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to set hack '" + this.getColor(ChatColor.AQUA) + hack.NAME + "' setting '" + this.getColor(ChatColor.AQUA) + setting.getName() + this.getColor(ChatColor.RED) + "' (invalid setting value '" + this.getColor(ChatColor.AQUA) + thirdEntry + this.getColor(ChatColor.RED) + "' inputted)!");
        }
    }
}
