package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import net.minecraft.src.Chunk;

/**
 * @author Hoosiers
 * @since 09-19-2026
 */

public final class LoadChunkEvent extends EventVariable<LoadChunkEvent> {

    public Chunk chunk;

    @Override
    public LoadChunkEvent withVariables(Object... variables) {
        this.chunk = (Chunk) variables[0];

        return super.withVariables(variables);
    }
}
