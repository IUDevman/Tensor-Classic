package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.EventCancellable;
import net.minecraft.src.Packet10Flying;

/**
 * @author Hoosiers
 * @since 03-26-2026
 */

public final class Packet10FlyingEvent extends EventCancellable {

    public final Packet10Flying packet;

    public Packet10FlyingEvent(Packet10Flying packet) {
        this.packet = packet;
    }
}
