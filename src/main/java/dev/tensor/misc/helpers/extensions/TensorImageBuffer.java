package dev.tensor.misc.helpers.extensions;

import net.minecraft.src.ImageBuffer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Hoosiers
 * @since 05-07-2026
 */

@SuppressWarnings("FieldCanBeLocal")
public final class TensorImageBuffer implements ImageBuffer {

    private final int width = 512;
    private final int height = 256;

    @Override
    public BufferedImage parseUserSkin(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return null;

        } else {
            BufferedImage scaledBufferedImage = new BufferedImage(this.width, this.height, 2);

            Graphics graphics = scaledBufferedImage.getGraphics();

            graphics.drawImage(bufferedImage, 0, 0, null);
            graphics.dispose();

            return scaledBufferedImage;
        }
    }
}
