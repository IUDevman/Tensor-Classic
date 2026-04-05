package dev.tensor.feature.hacks;

import dev.tensor.backend.events.PortalCloseGUIEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

@Hack.Info(name = "PortalInventory", category = Category.Player)
public final class PortalInventory extends Hack {

    @SuppressWarnings("unused")
    @EventTarget
    public void onPortalCloseGUIEvent(PortalCloseGUIEvent event) {
        event.cancel();
    }
}
