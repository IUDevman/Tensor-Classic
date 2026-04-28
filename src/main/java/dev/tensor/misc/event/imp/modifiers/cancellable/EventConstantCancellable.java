package dev.tensor.misc.event.imp.modifiers.cancellable;

import dev.tensor.misc.event.imp.modifiers.EventConstant;

/**
 * @author Hoosiers
 * @since 04-20-2026
 */

public class EventConstantCancellable implements EventConstant, Cancellable {

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
        EventConstant.super.reset();
    }
}
