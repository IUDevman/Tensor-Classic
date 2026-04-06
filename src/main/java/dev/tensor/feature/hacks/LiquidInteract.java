package dev.tensor.feature.hacks;

import dev.tensor.backend.events.FluidBlockEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 04-05-2026
 */

@Hack.Info(name = "LiquidInteract", category = Category.Player)
public final class LiquidInteract extends Hack {

    @SuppressWarnings("unused")
    @EventTarget
    public void onFluidBlockEvent(FluidBlockEvent event) {
        event.cancel();
    }
}
