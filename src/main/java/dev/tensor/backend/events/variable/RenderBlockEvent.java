package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import dev.tensor.misc.helpers.block.BlockCoords;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public final class RenderBlockEvent extends EventVariable<RenderBlockEvent> {

    public BlockCoords blockCoords;

    @Override
    public RenderBlockEvent withVariables(Object... args) {
        this.blockCoords = (BlockCoords) args[0];

        return super.withVariables(args);
    }
}
