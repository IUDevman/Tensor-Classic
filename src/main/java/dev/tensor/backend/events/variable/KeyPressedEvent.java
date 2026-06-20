package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class KeyPressedEvent extends EventVariable<KeyPressedEvent> {

    public int KEY_CODE;

    @Override
    public KeyPressedEvent withVariables(Object... variables) {
        this.KEY_CODE = (int) variables[0];

        return super.withVariables(variables);
    }
}
