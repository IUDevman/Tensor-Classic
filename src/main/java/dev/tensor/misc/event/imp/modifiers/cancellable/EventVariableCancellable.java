package dev.tensor.misc.event.imp.modifiers.cancellable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;

/**
 * @author Hoosiers
 * @since 04-20-2026
 */

public abstract class EventVariableCancellable<T> extends EventVariable<T> implements Cancellable {

    private boolean cancelled;

    @Override
    public final boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public final void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public final void cancel() {
        this.setCancelled(true);
    }

    @Override
    public final void reset() {
        this.setCancelled(false);
        super.reset();
    }
}
