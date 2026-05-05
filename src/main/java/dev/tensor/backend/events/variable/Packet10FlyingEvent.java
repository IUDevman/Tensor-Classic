package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.Packet10Flying;

/**
 * @author Hoosiers
 * @since 03-26-2026
 */

public final class Packet10FlyingEvent extends EventVariableCancellable<Packet10FlyingEvent> {

    public Packet10Flying packet;

    @Override
    public Packet10FlyingEvent withVariables(Object... args) {
        this.packet = (Packet10Flying) args[0];

        return super.withVariables(args);
    }
}
