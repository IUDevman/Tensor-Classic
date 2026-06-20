package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.MovingObjectPosition;

/**
 * @author Hoosiers
 * @since 04-13-2026
 */

public final class RenderBlockBreakEvent extends EventVariableCancellable<RenderBlockBreakEvent> {

    public MovingObjectPosition positionToRender;

    @Override
    public RenderBlockBreakEvent withVariables(Object... variables) {
        this.positionToRender = (MovingObjectPosition) variables[0];

        return super.withVariables(variables);
    }
}
