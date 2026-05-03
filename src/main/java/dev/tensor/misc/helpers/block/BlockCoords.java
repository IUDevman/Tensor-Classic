package dev.tensor.misc.helpers.block;

import net.minecraft.src.Block;
import net.minecraft.src.Frustrum;

/**
 * @author Hoosiers
 * @since 03-29-2026
 */

public final class BlockCoords {

    public final Block block;
    public final int x;
    public final int y;
    public final int z;
    private final int[] quickFrustrumBox;

    public BlockCoords(Block block, int x, int y, int z) {
        this.block = block;
        this.x = x;
        this.y = y;
        this.z = z;

        this.quickFrustrumBox = new int[]{x + 1, y + 1, z + 1};
    }

    public boolean isInFrustrum(Frustrum frustrum) {
        return frustrum.isBoxInFrustum(this.x, this.y, this.z, this.quickFrustrumBox[0], this.quickFrustrumBox[1], this.quickFrustrumBox[2]);
    }
}
