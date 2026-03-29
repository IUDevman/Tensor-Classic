package dev.tensor.feature.hacks;

import dev.tensor.backend.events.ViewBobEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

@Hack.Info(name = "NoViewBob", category = Category.Render)
public final class NoViewBob extends Hack {

    @SuppressWarnings("unused")
    @EventTarget
    public void onViewBobEvent(ViewBobEvent event) {
        event.cancel();
    }
}
