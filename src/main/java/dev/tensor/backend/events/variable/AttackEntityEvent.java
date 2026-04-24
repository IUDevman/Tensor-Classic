package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import net.minecraft.src.Entity;

/**
 * @author Hoosiers
 * @since 04-23-2026
 */

public final class AttackEntityEvent implements EventVariable {

    public Entity entityHit;

    @Override
    public <T extends EventVariable> T withVariables(Object... args) {
        this.entityHit = (Entity) args[0];

        return EventVariable.super.withVariables(args);
    }
}
