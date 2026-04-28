package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 04-16-2026
 */

public final class BlockClickToPlaceEvent extends EventVariableCancellable {

    public int count;

    @Override
    public <T extends EventVariable> T withVariables(Object... variables) {
        this.count = (int) variables[0];

        return super.withVariables(variables);
    }
}
