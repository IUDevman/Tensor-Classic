package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.Packet14BlockDig;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

public final class BlockBreakPacketEvent extends EventVariableCancellable<BlockBreakPacketEvent> {

    public Packet14BlockDig packet;

    @Override
    public BlockBreakPacketEvent withVariables(Object... variables) {
        this.packet = (Packet14BlockDig) variables[0];

        return super.withVariables(variables);
    }
}
