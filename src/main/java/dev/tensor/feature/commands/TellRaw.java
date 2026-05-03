package dev.tensor.feature.commands;

import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.command.Command;
import net.minecraft.src.Packet3Chat;

/**
 * @author Hoosiers
 * @since 04-23-2026
 */

public final class TellRaw implements Command {

    @Override
    public String getName() {
        return "TellRaw";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName(),
                "SendRaw",
                "SendMessage",
                "Raw"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {message}";
    }

    @Override
    public String getDescription() {
        return "Sends a chat message that will bypass the CommandManager and Hacks.";
    }

    @Override
    public boolean shouldParseFullMessage() {
        return true;
    }

    @Override
    public void onCommand(String[] ARGS) {

    }

    @Override
    public void onCommand(String message) {
        if (message == null) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to send raw message (no message inputted)!");
            return;
        }

        this.sendPacketBypass(new Packet3Chat(message));
    }
}
