package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ViewBobEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventConstantCancellable;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

@Hack.Info(name = "NoViewBob", category = Category.Render)
public final class NoViewBob extends Hack {

    @SuppressWarnings("unused")
    private final EventAction<ViewBobEvent> viewBobEventAction = this.addEventAction(new EventAction<>(ViewBobEvent.class, EventConstantCancellable::cancel));
}
