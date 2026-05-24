package dev.tensor.misc.gui.imp.elements;

import dev.tensor.misc.gui.imp.Element;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

/**
 * @author Hoosiers
 * @since 05-04-2021
 */

public abstract class SettingElement implements Element {

    public static final int width = 204;

    private final HackElement hackElement;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final IntegerSetting scrollY;
    private final int posX;
    private final int posY;

    public SettingElement(HackElement hackElement, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int posX, int posY) {
        this.hackElement = hackElement;
        this.x = x;
        this.y = y;
        this.scrollY = scrollY;
        this.posX = posX;
        this.posY = posY;
    }

    public final HackElement getHackElement() {
        return this.hackElement;
    }

    public final IntegerSetting getX() {
        return this.x;
    }

    public final IntegerSetting getY() {
        return this.y;
    }

    public final IntegerSetting getScrollY() {
        return this.scrollY;
    }

    public final int getXPos() {
        return this.posX;
    }

    public final int getYPos() {
        return this.posY;
    }
}