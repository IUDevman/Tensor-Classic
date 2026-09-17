package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;

/**
 * @author Hoosiers
 * @since 09-17-2026
 */

public final class DamageOrAttackPlayerEvent extends EventVariable<DamageOrAttackPlayerEvent> {

    public Type type;

    @Override
    public DamageOrAttackPlayerEvent withVariables(Object... variables) {
        this.type = (Type) variables[0];

        return super.withVariables(variables);
    }

    public enum Type {
        Damage,
        Attack
    }
}
