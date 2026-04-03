package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.EventCancellable;

/**
 * @author Hoosiers
 * @since 04-02-2026
 */

public final class VelocityEvent extends EventCancellable {

    public final Type type;

    public VelocityEvent(Type type) {
        this.type = type;
    }

    public enum Type {
        Block,
        Entity,
        Fluid,
        SoulSand
    }
}
