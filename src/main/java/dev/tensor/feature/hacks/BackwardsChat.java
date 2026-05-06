package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.ChatSentEvent;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 03-24-2026
 */

@Hack.Info(name = "BackwardsChat", category = Category.Misc)
public final class BackwardsChat extends Hack {

    @SuppressWarnings("unused")
    public void onChatSentEvent(ChatSentEvent event) {
        String message = event.ARGS;

        if (!message.startsWith("/") && !message.startsWith(this.getHackManager().getHack(Commands.class).prefix.getValue())) {

            boolean greenText = false;

            if (message.startsWith(">") && message.length() > 1) {
                message = message.substring(1);
                greenText = true;
            }

            StringBuilder builder = new StringBuilder(message);

            event.ARGS = (greenText ? ">" : "") + builder.reverse();
        }
    }
}
