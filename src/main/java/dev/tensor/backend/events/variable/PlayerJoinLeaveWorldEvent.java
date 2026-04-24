package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.EntityOtherPlayerMP;

/**
 * @author Hoosiers
 * @since 04-24-2026
 */

public final class PlayerJoinLeaveWorldEvent extends EventVariableCancellable {

    public Type type;
    public EntityOtherPlayerMP otherPlayerMP;

    @Override
    public <T extends EventVariable> T withVariables(Object... args) {
        this.type = (Type) args[0];
        this.otherPlayerMP = (EntityOtherPlayerMP) args[1];

        return super.withVariables(args);
    }

    public enum Type {
        Join,
        Kill_or_Leave
    }
}
