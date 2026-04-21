package dev.tensor.misc.event.imp.modifiers;

import dev.tensor.misc.event.imp.Event;

/**
 * @author Hoosiers
 * @since 04-20-2026
 */

public interface EventVariable extends Event {

    @SuppressWarnings("unchecked")
    default <T extends EventVariable> T withVariables(Object... variables) {
        return (T) this;
    }
}
