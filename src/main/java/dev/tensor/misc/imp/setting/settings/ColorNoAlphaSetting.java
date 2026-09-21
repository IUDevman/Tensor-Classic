package dev.tensor.misc.imp.setting.settings;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-01-2026
 */

public class ColorNoAlphaSetting extends ColorSetting {

    public ColorNoAlphaSetting(String NAME, Color VALUE) {
        super(NAME, VALUE);
    }

    public ColorNoAlphaSetting(String NAME, Color VALUE, boolean RAINBOW) {
        super(NAME, VALUE, RAINBOW);
    }

    @Override
    public String getDescription() {
        return "Penis 1 " + this.getName();
    }
}
