package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.Event;
import net.minecraft.src.Packet14BlockDig;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

public final class BlockBreakEvent implements Event {

    public final Packet14BlockDig packet;

    public BlockBreakEvent(Packet14BlockDig packet) {
        this.packet = packet;
    }
}
