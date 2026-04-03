package dev.tensor.feature.hacks;

import dev.tensor.backend.events.KnockbackEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 04-02-2026
 */

@Hack.Info(name = "AntiKnockback", category = Category.Combat)
public final class AntiKnockback extends Hack {

    @SuppressWarnings("unused")
    @EventTarget
    public void onKnockbackEvent(KnockbackEvent event) {
        event.cancel();
    }
}
