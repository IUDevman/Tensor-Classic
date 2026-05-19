package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;
import dev.tensor.misc.helpers.block.BlockCoords;
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
    public RenderBlockEvent withVariables(Object... args) {
        this.block = (Block) args[0];
        this.x = (int) args[1];
        this.y = (int) args[2];
        this.z = (int) args[3];

        return super.withVariables(args);
    }
}
