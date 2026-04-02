package dev.tensor.feature.hacks;

import dev.tensor.backend.events.CloseInventoryEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 04-01-2026
 */

@Hack.Info(name = "XCarry", category = Category.Player)
public final class XCarry extends Hack {

    @SuppressWarnings("unused")
    @EventTarget
    public void onCloseInventoryEvent(CloseInventoryEvent event) {
        event.cancel();
    }
}
