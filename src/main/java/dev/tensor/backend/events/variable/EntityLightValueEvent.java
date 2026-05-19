package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 05-14-2026
 */

public final class EntityLightValueEvent extends EventVariableCancellable<EntityLightValueEvent> {

    public int entityID;

    @Override
    public EntityLightValueEvent withVariables(Object... variables) {
        this.entityID = (int) variables[0];

        return super.withVariables(variables);
    }
}
