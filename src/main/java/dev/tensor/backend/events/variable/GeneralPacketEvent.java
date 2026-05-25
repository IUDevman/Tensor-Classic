package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import net.minecraft.src.Packet;

/**
 * @author Hoosiers
 * @since 05-24-2026
 */

public final class GeneralPacketEvent extends EventVariableCancellable<GeneralPacketEvent> {

    public Type type;
    public Packet packet;

    @Override
    public GeneralPacketEvent withVariables(Object... args) {
        this.type = (Type) args[0];
        this.packet = (Packet) args[1];

        return super.withVariables(args);
    }

    public enum Type {
        OUTBOUND,
        INBOUND
    }
}
