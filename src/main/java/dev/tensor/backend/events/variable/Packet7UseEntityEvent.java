package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.Packet7UseEntity;

/**
 * @author Hoosiers
 * @since 04-29-2026
 */

public final class Packet7UseEntityEvent extends EventVariableCancellable {

    public Packet7UseEntity packet;

    @Override
    public <T extends EventVariable> T withVariables(Object... args) {
        this.packet = (Packet7UseEntity) args[0];

        return super.withVariables(args);
    }
}
