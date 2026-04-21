package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ParticleEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 04-07-2026
 */

@Hack.Info(name = "NoParticles", category = Category.Render)
public final class NoParticles extends Hack {

    @SuppressWarnings("unused")
    @EventTarget
    public void onParticleEvent(ParticleEvent event) {
        event.cancel();
    }
}
