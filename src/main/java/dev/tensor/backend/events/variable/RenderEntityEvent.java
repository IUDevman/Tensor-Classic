package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.Entity;

/**
 * @author Hoosiers
 * @since 05-14-2026
 */

public final class RenderEntityEvent extends EventVariableCancellable<RenderEntityEvent> {

    public Entity entity;

    @Override
    public RenderEntityEvent withVariables(Object... variables) {
        this.entity = (Entity) variables[0];

        return super.withVariables(variables);
    }
}
