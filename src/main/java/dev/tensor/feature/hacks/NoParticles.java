package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ParticleEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventConstantCancellable;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 04-07-2026
 */

@Hack.Info(name = "NoParticles", category = Category.Render)
public final class NoParticles extends Hack {

    @SuppressWarnings("unused")
    private final EventAction<ParticleEvent> particleEventAction = this.addHackEventAction(new EventAction<>(ParticleEvent.class, EventConstantCancellable::cancel));
}
