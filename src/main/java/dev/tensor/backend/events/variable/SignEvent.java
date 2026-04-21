package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.TileEntitySign;

/**
 * @author Hoosiers
 * @since 04-17-2026
 */

public final class SignEvent extends EventVariableCancellable {

    public TileEntitySign tileEntitySign;

    @Override
    public <T extends EventVariable> T withVariables(Object... args) {
        this.tileEntitySign = (TileEntitySign) args[0];

        return super.withVariables(args);
    }
}
