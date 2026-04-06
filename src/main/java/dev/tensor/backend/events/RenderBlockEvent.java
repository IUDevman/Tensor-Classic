package dev.tensor.backend.events;

import dev.tensor.misc.helpers.block.BlockCoords;
import dev.tensor.misc.event.imp.Event;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public final class RenderBlockEvent implements Event {

    public final BlockCoords blockCoords;

    public RenderBlockEvent(BlockCoords blockCoords) {
        this.blockCoords = blockCoords;
    }
}
