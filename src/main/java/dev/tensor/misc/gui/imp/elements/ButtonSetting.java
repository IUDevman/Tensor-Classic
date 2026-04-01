package dev.tensor.misc.gui.imp.elements;

import dev.tensor.misc.gui.imp.Element;
import dev.tensor.misc.gui.imp.Hoverable;

/**
 * @author IUDevman
 * @since 05-04-2021
 */

public interface ButtonSetting extends Element, KeyPressElement, Hoverable {

    boolean isActive();

    void setActive(boolean active);

    void close();

    void onHovered(int mouseX, int mouseY);

    void handleScroll(int scroll);
}
