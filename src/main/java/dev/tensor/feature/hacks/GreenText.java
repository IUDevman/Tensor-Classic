package dev.tensor.feature.hacks;

import dev.tensor.backend.events.ChatSentEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import net.minecraft.src.Packet3Chat;

@Hack.Info(name = "GreenText", category = Category.Misc)
public final class GreenText extends Hack {

    @SuppressWarnings("unused")
    @EventTarget
    public void onChatSentEvent(ChatSentEvent event) {

        String message = event.ARGS;

        if (!message.startsWith(">") && !message.startsWith("/") && !message.startsWith(this.getHackManager().getHack(Commands.class).prefix.getValue())) {
            message = ">" + message;
            this.sendPacket(new Packet3Chat(message));
            event.cancel();
        }
    }
}
