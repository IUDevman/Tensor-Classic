package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.FluidInteractEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventConstantCancellable;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 04-05-2026
 */

@Hack.Info(name = "LiquidInteract", category = Category.Player, description = "Allows for you to place blocks on fluids.")
public final class LiquidInteract extends Hack {

    @SuppressWarnings("unused")
    private final EventAction<FluidInteractEvent> fluidInteractEventAction = this.addHackEventAction(new EventAction<>(FluidInteractEvent.class, EventConstantCancellable::cancel));
}
