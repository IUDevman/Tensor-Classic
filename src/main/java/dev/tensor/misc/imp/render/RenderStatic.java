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

    public static final int SLUG_TEXTURE_ID = 69001;
    public static final int BACKGROUND_TEXTURE_1_ID = 69002;
    public static final int BACKGROUND_TEXTURE_2_ID = 69003;
    public static final int BACKGROUND_TEXTURE_3_ID = 69004;
    public static final int BACKGROUND_TEXTURE_4_ID = 69005;
    public static final int BACKGROUND_LOGO_ID = 69006;

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
