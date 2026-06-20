package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import net.minecraft.src.Packet52MultiBlockChange;
import net.minecraft.src.Packet53BlockChange;

/**
 * @author Hoosiers
 * @since 05-17-2026
 */

public final class ChunkChangeEvent extends EventVariable<ChunkChangeEvent> {

    public Type type;

    public Packet52MultiBlockChange packet52MultiBlockChange;
    public Packet53BlockChange packet53BlockChange;

    @Override
    public ChunkChangeEvent withVariables(Object... variables) {
        this.type = (Type) variables[0];

        if (this.type.equals(Type.SINGLE)) {
            this.packet53BlockChange = (Packet53BlockChange) variables[1];
            this.packet52MultiBlockChange = null;

        } else {
            this.packet53BlockChange = null;
            this.packet52MultiBlockChange = (Packet52MultiBlockChange) variables[1];
        }

        return super.withVariables(variables);
    }

    public enum Type {
        SINGLE,
        MULTI
    }
}
