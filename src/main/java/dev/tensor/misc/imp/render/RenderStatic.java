package dev.tensor.misc.imp.render;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Frustrum;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderItem;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Hoosiers
 * @since 05-03-2026
 */

public final class RenderStatic {

    public static final RenderItem tensor_renderItem = new RenderItem();
    public static final RenderBlocks tensor_renderBlocks = new RenderBlocks();

    private static final Frustrum tensor_playerFrustrum = new Frustrum();

    private static final ConcurrentHashMap<Integer, float[]> colorCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, float[]> noAlphaColorCache = new ConcurrentHashMap<>();

    public static void setFrustrumPosition(double x, double y, double z) {
        tensor_playerFrustrum.setPosition(x, y, z);
    }

    public static boolean isBoundingBoxInFrustum(AxisAlignedBB boundingBox) {
        return tensor_playerFrustrum.isBoundingBoxInFrustum(boundingBox);
    }

    public static boolean isBoxInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return tensor_playerFrustrum.isBoxInFrustum(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static float[] getCachedColors(Color color, boolean hasAlpha) {
        return getCachedColors(color.getRGB(), hasAlpha);
    }

    public static float[] getCachedColors(int color, boolean hasAlpha) {
        if (hasAlpha) {
            if (colorCache.containsKey(color)) {
                return colorCache.get(color);
            }

            float[] toReturn = getColor(color, true);

            colorCache.put(color, toReturn);

            return toReturn;
        }

        if (noAlphaColorCache.containsKey(color)) {
            return noAlphaColorCache.get(color);
        }

        float[] toReturn = getColor(color, false);

        noAlphaColorCache.put(color, toReturn);

        return toReturn;
    }

    private static float[] getColor(int color, boolean hasAlpha) {
        return new float[]{(color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, hasAlpha ? (color >> 24 & 255) / 255.0F : 1F};
    }
}
