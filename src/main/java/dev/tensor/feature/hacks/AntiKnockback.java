package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.KnockbackEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventConstantCancellable;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 04-02-2026
 */

@Hack.Info(name = "AntiKnockback", category = Category.Combat)
public final class AntiKnockback extends Hack {

    @SuppressWarnings("unused")
    private final EventAction<KnockbackEvent> knockbackEventAction = this.addHackEventAction(new EventAction<>(KnockbackEvent.class, EventConstantCancellable::cancel));
}
