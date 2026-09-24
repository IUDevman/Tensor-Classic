package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.ChatSentEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Hack.Info(name = "GreenText", category = Category.Misc, description = "Puts a '>' in front of sent messages, making them appear green colored on many servers.")
public final class GreenText extends Hack {

    @SuppressWarnings("unused")
    private final EventAction<ChatSentEvent> chatSentEventAction = this.addHackEventAction(new EventAction<>(ChatSentEvent.class, event -> {
        if (event.isCancelled()) {
            return;
        }

        String message = event.ARGS;

        if (!message.startsWith(">") && !message.startsWith("/") && !message.startsWith(this.getHackManager().getHack(Commands.class).prefix.getValue())) {
            if (event.ARGS.length() + 1 > 100) {
                return;
            }

            event.ARGS = ">" + event.ARGS;
        }
    }));
}
