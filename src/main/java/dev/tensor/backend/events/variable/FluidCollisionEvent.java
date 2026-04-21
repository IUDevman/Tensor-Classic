package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 04-05-2026
 */

public final class FluidCollisionEvent extends EventVariableCancellable {

    public int x;
    public int y;
    public int z;

    @Override
    public <T extends EventVariable> T withVariables(Object... args) {
        this.x = (int) args[0];
        this.y = (int) args[1];
        this.z = (int) args[2];

        return super.withVariables(args);
    }
}
