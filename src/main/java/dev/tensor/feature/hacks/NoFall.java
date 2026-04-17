package dev.tensor.feature.hacks;

import dev.tensor.backend.events.Packet10FlyingEvent;
import dev.tensor.backend.mixins.accessors.EntityAccessor;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 03-26-2026
 */

@Hack.Info(name = "NoFall", category = Category.Player)
public final class NoFall extends Hack {

    @Override
    public void onClientTick() {
        if (((EntityAccessor) this.getPlayer()).tensor_getFallDistance() > 2.5F) {
            ((EntityAccessor) this.getPlayer()).tensor_setFallDistance(0F);
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onPacket10FlyingEvent(Packet10FlyingEvent event) {
        event.packet.onGround = true;
    }
}
