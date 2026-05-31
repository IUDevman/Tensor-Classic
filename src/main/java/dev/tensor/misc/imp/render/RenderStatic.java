package dev.tensor.misc.imp.render;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Frustrum;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderItem;

/**
 * @author Hoosiers
 * @since 05-03-2026
 */

public final class RenderStatic {

    public static final RenderItem tensor_renderItem = new RenderItem();
    public static final RenderBlocks tensor_renderBlocks = new RenderBlocks();

    private static final Frustrum tensor_playerFrustrum = new Frustrum();

    public static void setFrustrumPosition(double x, double y, double z) {
        tensor_playerFrustrum.setPosition(x, y, z);
    }

    public static boolean isBoundingBoxInFrustum(AxisAlignedBB boundingBox) {
        return tensor_playerFrustrum.isBoundingBoxInFrustum(boundingBox);
    }

    public static boolean isBoxInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return tensor_playerFrustrum.isBoxInFrustum(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
