package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class KeyPressedEvent implements EventVariable {

    public int KEY_CODE;

    @Override
    public <T extends EventVariable> T withVariables(Object... args) {
        this.KEY_CODE = (int) args[0];

        return EventVariable.super.withVariables(args);
    }
}
