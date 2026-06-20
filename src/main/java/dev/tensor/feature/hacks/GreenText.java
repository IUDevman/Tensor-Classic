package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.ChatSentEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Hack.Info(name = "GreenText", category = Category.Misc)
public final class GreenText extends Hack {

    @SuppressWarnings("unused")
    private final EventAction<ChatSentEvent> chatSentEventAction = this.addEventAction(new EventAction<>(ChatSentEvent.class, event -> {
        String message = event.ARGS;

        if (!message.startsWith(">") && !message.startsWith("/") && !message.startsWith(this.getHackManager().getHack(Commands.class).prefix.getValue())) {
            if (event.ARGS.length() + 1 > 100) {
                return;
            }

            event.ARGS = ">" + event.ARGS;
        }
    }));
}
