package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.CloseInventoryEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventConstantCancellable;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 04-01-2026
 */

@Hack.Info(name = "XCarry", category = Category.Player)
public final class XCarry extends Hack {

    @SuppressWarnings("unused")
    private final EventAction<CloseInventoryEvent> closeInventoryEventAction = this.addHackEventAction(new EventAction<>(CloseInventoryEvent.class, EventConstantCancellable::cancel));
}
