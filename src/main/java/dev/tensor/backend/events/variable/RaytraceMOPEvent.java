package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.Entity;

/**
 * @author Hoosiers
 * @since 09-09-2026
 */

public final class RaytraceMOPEvent extends EventVariableCancellable<RaytraceMOPEvent> {

    public Entity raytraceHit;

    @Override
    public RaytraceMOPEvent withVariables(Object... variables) {
        this.raytraceHit = (Entity) variables[0];

        return super.withVariables(variables);
    }
}
