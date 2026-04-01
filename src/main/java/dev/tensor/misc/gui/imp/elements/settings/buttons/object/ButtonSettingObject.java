package dev.tensor.misc.gui.imp.elements.settings.buttons.object;

import dev.tensor.misc.gui.imp.Element;
import dev.tensor.misc.gui.imp.Hoverable;

/**
 * @author Hoosiers
 * @since 03-31-2026
 */

public interface ButtonSettingObject extends Element, Hoverable {

    boolean hovered();

    void setHovered(boolean hovered);

    void onHovered(int mouseX, int mouseY);
}
