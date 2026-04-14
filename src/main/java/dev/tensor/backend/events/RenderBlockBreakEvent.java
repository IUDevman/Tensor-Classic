package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.EventCancellable;
import net.minecraft.src.MovingObjectPosition;

/**
 * @author Hoosiers
 * @since 04-13-2026
 */

public final class RenderBlockBreakEvent extends EventCancellable {

    public final MovingObjectPosition positionToRender;

    public RenderBlockBreakEvent(MovingObjectPosition positionToRender) {
        this.positionToRender = positionToRender;
    }
}
