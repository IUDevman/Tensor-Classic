package dev.tensor.misc.gui.imp;

/**
 * @author Hoosiers
 * @since 03-31-2026
 */

public interface Hoverable {

    default boolean isHovered(int minX, int minY, int maxX, int maxY, double mouseX, double mouseY) {
        if (mouseX < minX || mouseX > maxX) {
            return false;
        } else return !(mouseY < minY) && !(mouseY > maxY);
    }
}
