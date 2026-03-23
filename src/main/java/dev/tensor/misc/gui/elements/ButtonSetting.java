package dev.tensor.misc.gui.elements;

import dev.tensor.misc.gui.Element;

/**
 * @author IUDevman
 * @since 05-04-2021
 */

public interface ButtonSetting extends Element, KeyPressElement {

    boolean isActive();

    void setActive(boolean active);

    void close();

    void onHovered(int mouseX, int mouseY);

    default boolean isHovered(int minX, int minY, int maxX, int maxY, double mouseX, double mouseY) {
        if (mouseX < minX || mouseX > maxX) {
            return false;
        }

        else return !(mouseY < minY) && !(mouseY > maxY);
    }
}
