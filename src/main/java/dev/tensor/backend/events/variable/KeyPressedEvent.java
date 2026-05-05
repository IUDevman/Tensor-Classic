package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class KeyPressedEvent extends EventVariable<KeyPressedEvent> {

    public int KEY_CODE;

    @Override
    public KeyPressedEvent withVariables(Object... args) {
        this.KEY_CODE = (int) args[0];

        return super.withVariables(args);
    }
}
