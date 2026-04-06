package dev.tensor.misc.helpers.block;

import net.minecraft.src.Block;

/**
 * @author Hoosiers
 * @since 03-29-2026
 */

public final class BlockCoords {

    public final Block block;
    public final int x;
    public final int y;
    public final int z;

    public BlockCoords(Block block, int x, int y, int z) {
        this.block = block;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
