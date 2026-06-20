package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.SocketInitEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventConstantCancellable;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 04-18-2026
 */

@Hack.Info(name = "TCPNoDelay", category = Category.Misc)
public final class TCPNoDelay extends Hack {

    @Override
    public void onEnable(boolean isNull) {
        if (!isNull && this.isMultiplayer()) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + "TCPNoDelay: Relog to fully enable hack!");
        }
    }

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull && this.isMultiplayer()) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + "TCPNoDelay: Relog to fully disable hack!");
        }
    }

    @SuppressWarnings("unused")
    private final EventAction<SocketInitEvent> socketInitEventAction = this.addEventAction(new EventAction<>(SocketInitEvent.class, EventConstantCancellable::cancel));
}
