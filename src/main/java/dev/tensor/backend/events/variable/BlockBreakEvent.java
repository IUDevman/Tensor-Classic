package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.Packet14BlockDig;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

public final class BlockBreakEvent extends EventVariableCancellable {

    public Packet14BlockDig packet;

    @Override
    public <T extends EventVariable> T withVariables(Object... args) {
        this.packet = (Packet14BlockDig) args[0];

        return super.withVariables(args);
    }
}
