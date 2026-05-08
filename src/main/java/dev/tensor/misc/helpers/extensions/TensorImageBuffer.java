package dev.tensor.misc.helpers.extensions;

import net.minecraft.src.ImageBuffer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Hoosiers
 * @since 05-07-2026
 */

public final class TensorImageBuffer implements ImageBuffer {

    public static final int capeDefaultWidth = 512;
    public static final int capeDefaultHeight = 256;

    private final int width;
    private final int height;

    public TensorImageBuffer() {
        this.width = capeDefaultWidth;
        this.height = capeDefaultHeight;
    }

    public TensorImageBuffer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public BufferedImage parseUserSkin(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return null;
        }

        BufferedImage scaledBufferedImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);

        Graphics graphics = scaledBufferedImage.getGraphics();

        graphics.drawImage(bufferedImage, 0, 0, null);
        graphics.dispose();

        return scaledBufferedImage;
    }
}
