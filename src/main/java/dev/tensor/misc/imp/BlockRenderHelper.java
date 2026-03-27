package dev.tensor.misc.imp;

/**
 * @author Hoosiers
 * @since 03-26-2026
 */

public final class BlockRenderHelper {

    public final int blockID;

    public final int x;
    public final int y;
    public final int z;

    public BlockRenderHelper(int blockID, int x, int y, int z) {
        this.blockID = blockID;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
