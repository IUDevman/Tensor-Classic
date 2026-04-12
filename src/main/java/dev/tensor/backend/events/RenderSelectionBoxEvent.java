package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.EventCancellable;
import net.minecraft.src.MovingObjectPosition;

/**
 * @author Hoosiers
 * @since 04-11-2026
 */

public final class RenderSelectionBoxEvent extends EventCancellable {

    public final MovingObjectPosition positionToRender;

    public RenderSelectionBoxEvent(MovingObjectPosition positionToRender) {
        this.positionToRender = positionToRender;
    }
}
