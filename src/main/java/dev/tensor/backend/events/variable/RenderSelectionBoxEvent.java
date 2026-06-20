package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.MovingObjectPosition;

/**
 * @author Hoosiers
 * @since 04-11-2026
 */

public final class RenderSelectionBoxEvent extends EventVariableCancellable<RenderSelectionBoxEvent> {

    public MovingObjectPosition positionToRender;

    @Override
    public RenderSelectionBoxEvent withVariables(Object... variables) {
        this.positionToRender = (MovingObjectPosition) variables[0];

        return super.withVariables(variables);
    }
}
