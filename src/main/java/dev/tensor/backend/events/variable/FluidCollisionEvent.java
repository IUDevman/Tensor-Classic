package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 04-05-2026
 */

public final class FluidCollisionEvent extends EventVariableCancellable<FluidCollisionEvent> {

    public int x;
    public int y;
    public int z;

    @Override
    public FluidCollisionEvent withVariables(Object... variables) {
        this.x = (int) variables[0];
        this.y = (int) variables[1];
        this.z = (int) variables[2];

        return super.withVariables(variables);
    }
}
