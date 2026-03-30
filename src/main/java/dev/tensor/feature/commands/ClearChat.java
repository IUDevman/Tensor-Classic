package dev.tensor.feature.commands;

import dev.tensor.misc.imp.command.SimpleCallableCommand;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

public final class ClearChat extends SimpleCallableCommand {

    @Override
    public String getName() {
        return "ClearChat";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName(),
                "Clear"
        };
    }

    @Override
    public String getDescription() {
        return "Clears the chat.";
    }

    @Override
    public void onCommandTriggered() {
        this.getMinecraft().ingameGUI.clearChatMessages();
    }
}
