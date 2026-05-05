package dev.tensor.misc.event.imp;

import dev.tensor.misc.imp.Globals;

import java.util.function.Consumer;

/**
 * @author Hoosiers
 * @since 05-04-2026
 */

public abstract class Event<T> implements Globals {

    @SuppressWarnings("unchecked")
    public final T call() {
        this.getEventManager().call(this);

        return (T) this;
    }

    public final T call(Consumer<? super T> action) {
        T event = this.call();

        action.accept(event);

        return event;
    }

    public void reset() {

    }
}
