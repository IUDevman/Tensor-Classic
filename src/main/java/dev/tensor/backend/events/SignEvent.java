package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.EventCancellable;
import net.minecraft.src.TileEntitySign;

/**
 * @author Hoosiers
 * @since 04-17-2026
 */

public final class SignEvent extends EventCancellable {

    public final TileEntitySign tileEntitySign;

    public SignEvent(TileEntitySign tileEntitySign) {
        this.tileEntitySign = tileEntitySign;
    }
}
