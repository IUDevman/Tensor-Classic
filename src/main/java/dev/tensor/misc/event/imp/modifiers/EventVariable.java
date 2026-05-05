package dev.tensor.misc.event.imp.modifiers;

import dev.tensor.misc.event.imp.Event;

/**
 * @author Hoosiers
 * @since 04-20-2026
 */

public abstract class EventVariable<T> extends Event<T> {

    @SuppressWarnings("unchecked")
    public T withVariables(Object... variables) {
        return (T) this;
    }
}
