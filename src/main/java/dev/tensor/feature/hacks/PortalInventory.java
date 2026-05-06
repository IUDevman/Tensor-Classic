package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.PortalCloseGUIEvent;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

@Hack.Info(name = "PortalInventory", category = Category.Player)
public final class PortalInventory extends Hack {

    @SuppressWarnings("unused")
    public void onPortalCloseGUIEvent(PortalCloseGUIEvent event) {
        event.cancel();
    }
}
