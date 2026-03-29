package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.EventCancellable;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

public final class RenderOverlayEvent extends EventCancellable {

    public final Type type;

    public RenderOverlayEvent(Type type) {
        this.type = type;
    }

    public enum Type {
        blocks,
        fire,
        portals,
        pumpkin,
        vignette,
        water
    }
}
