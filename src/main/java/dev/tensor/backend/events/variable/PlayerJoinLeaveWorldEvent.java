package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.EntityOtherPlayerMP;

/**
 * @author Hoosiers
 * @since 04-24-2026
 */

public final class PlayerJoinLeaveWorldEvent extends EventVariableCancellable<PlayerJoinLeaveWorldEvent> {

    public Type type;
    public EntityOtherPlayerMP otherPlayerMP;

    @Override
    public PlayerJoinLeaveWorldEvent withVariables(Object... variables) {
        this.type = (Type) variables[0];
        this.otherPlayerMP = (EntityOtherPlayerMP) variables[1];

        return super.withVariables(variables);
    }

    public enum Type {
        JOIN,
        KILL_OR_LEAVE
    }
}
