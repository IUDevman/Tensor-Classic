package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.FluidBlockEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventConstantCancellable;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 04-05-2026
 */

@Hack.Info(name = "LiquidInteract", category = Category.Player)
public final class LiquidInteract extends Hack {

    @SuppressWarnings("unused")
    EventAction<FluidBlockEvent> fluidBlockEventAction = this.addEventAction(new EventAction<>(FluidBlockEvent.class, EventConstantCancellable::cancel));
}
