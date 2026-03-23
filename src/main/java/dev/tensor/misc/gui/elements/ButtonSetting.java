package dev.tensor.misc.gui.elements;

import dev.tensor.misc.gui.Element;

/**
 * @author IUDevman
 * @since 05-04-2021
 */

public interface ButtonSetting extends Element, KeyPressElement {

    boolean isActive();

    void setActive(boolean active);
}
