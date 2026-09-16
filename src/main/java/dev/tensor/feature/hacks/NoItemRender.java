package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.RenderItemEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventConstantCancellable;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 03-29-2026
 */

@Hack.Info(name = "NoItemRender", category = Category.Render)
public final class NoItemRender extends Hack {

    @SuppressWarnings("unused")
    private final EventAction<RenderItemEvent> renderItemEventAction = this.addHackEventAction(new EventAction<>(RenderItemEvent.class, EventConstantCancellable::cancel));
}
