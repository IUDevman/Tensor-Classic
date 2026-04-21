package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.MovingObjectPosition;

/**
 * @author Hoosiers
 * @since 04-13-2026
 */

public final class RenderBlockBreakEvent extends EventVariableCancellable {

    public MovingObjectPosition positionToRender;

    @Override
    public <T extends EventVariable> T withVariables(Object... args) {
        this.positionToRender = (MovingObjectPosition) args[0];

        return super.withVariables(args);
    }
}
