package dev.tensor.feature.commands;

import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.command.SimpleCallableCommand;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class Hacks extends SimpleCallableCommand {

    @Override
    public String getName() {
        return "Hacks";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName(),
                "HackList",
                "Hack",
                "Module",
                "Modules",
                "ModuleList"
        };
    }

    @Override
    public String getDescription() {
        return "Returns the list of available hacks (" + ChatColor.GREEN.text() + "Enabled" + ChatColor.DEFAULT.text() + "/" + ChatColor.RED.text() + "Disabled" + ChatColor.DEFAULT.text() + ").";
    }

    @Override
    public void onCommandTriggered() {

        String MESSAGE_PRE = this.getName() + " (" + ChatColor.AQUA.text() + this.getHackManager().getHacks().size() + ChatColor.DEFAULT.text() + "): ";

        ArrayList<String> HACKS = new ArrayList<>();

        this.getHackManager().getHacks().forEach(hack -> HACKS.add((hack.isEnabled() ? ChatColor.GREEN.text() : ChatColor.RED.text()) + hack.NAME + ChatColor.DEFAULT.text()));

        this.sendClientMessageWithHeader(MESSAGE_PRE + HACKS);
    }
}
