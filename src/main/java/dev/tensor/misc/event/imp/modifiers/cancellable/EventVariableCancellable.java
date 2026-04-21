package dev.tensor.misc.event.imp.modifiers.cancellable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;

/**
 * @author Hoosiers
 * @since 04-20-2026
 */

public class EventVariableCancellable implements EventVariable, Cancellable {

    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public void cancel() {
        this.setCancelled(true);
    }

    @Override
    public void reset() {
        this.setCancelled(false);
        EventVariable.super.reset();
    }
}
