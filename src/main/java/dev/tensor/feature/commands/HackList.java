package dev.tensor.feature.commands;

import dev.tensor.misc.command.SimpleCallableCommand;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class HackList extends SimpleCallableCommand {

    @Override
    public String getName() {
        return "HackList";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[] {
                this.getName(),
                "Hacks",
                "Hack",
                "Module",
                "Modules",
                "ModuleList"
        };
    }

    @Override
    public String getRegex() {
        return "{name}";
    }

    @Override
    public String getDescription() {
        return "Returns the list of available hacks (" + this.getColor(ChatColor.GREEN) + "Enabled" + this.getColor(ChatColor.DEFAULT) + "/" + this.getColor(ChatColor.RED) + "Disabled" + this.getColor(ChatColor.DEFAULT) + ").";
    }

    @Override
    public void onCommandTriggered() {

        String MESSAGE_PRE = this.getName() + " (" + this.getColor(ChatColor.AQUA) + this.getHackManager().getHacks().size() + this.getColor(ChatColor.DEFAULT) + "): ";

        ArrayList<String> HACKS = new ArrayList<>();

        this.getHackManager().getHacks().forEach(hack -> HACKS.add((hack.isEnabled() ? this.getColor(ChatColor.GREEN) : this.getColor(ChatColor.RED)) + hack.NAME + this.getColor(ChatColor.DEFAULT)));

        this.sendClientMessageWithPrefix(MESSAGE_PRE + HACKS);
    }
}
