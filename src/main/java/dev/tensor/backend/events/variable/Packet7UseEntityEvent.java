package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.Packet7UseEntity;

/**
 * @author Hoosiers
 * @since 04-29-2026
 */

public final class Packet7UseEntityEvent extends EventVariableCancellable<Packet7UseEntityEvent> {

    public Packet7UseEntity packet;

    @Override
    public Packet7UseEntityEvent withVariables(Object... variables) {
        this.packet = (Packet7UseEntity) variables[0];

        return super.withVariables(variables);
    }
}
