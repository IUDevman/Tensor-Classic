package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

public final class LightValueEvent extends EventVariableCancellable<LightValueEvent> {

    public boolean darkMode;

    @Override
    public LightValueEvent withVariables(Object... args) {
        this.darkMode = (boolean) args[0];

        return super.withVariables(args);
    }
}
