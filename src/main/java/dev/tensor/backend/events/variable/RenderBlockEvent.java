package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import net.minecraft.src.Block;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public final class RenderBlockEvent extends EventVariable<RenderBlockEvent> {

    public Block block;
    public int x;
    public int y;
    public int z;

    @Override
    public RenderBlockEvent withVariables(Object... variables) {
        this.block = (Block) variables[0];
        this.x = (int) variables[1];
        this.y = (int) variables[2];
        this.z = (int) variables[3];

        return super.withVariables(variables);
    }
}
