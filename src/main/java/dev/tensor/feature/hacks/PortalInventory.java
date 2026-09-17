package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.PortalCloseGUIEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventConstantCancellable;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 04-04-2026
 */

@Hack.Info(name = "PortalInventory", category = Category.Player)
public final class PortalInventory extends Hack {

    @SuppressWarnings("unused")
    private final EventAction<PortalCloseGUIEvent> portalCloseGUIEventAction = this.addHackEventAction(new EventAction<>(PortalCloseGUIEvent.class, EventConstantCancellable::cancel));
}
