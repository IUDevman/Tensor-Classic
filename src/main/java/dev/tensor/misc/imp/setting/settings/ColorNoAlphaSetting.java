package dev.tensor.misc.imp.setting.settings;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-01-2026
 */

public class ColorNoAlphaSetting extends ColorSetting {

    public ColorNoAlphaSetting(String NAME, Color VALUE, String DESCRIPTION) {
        super(NAME, VALUE, DESCRIPTION);
    }

    public ColorNoAlphaSetting(String NAME, Color VALUE, boolean RAINBOW, String DESCRIPTION) {
        super(NAME, VALUE, RAINBOW, DESCRIPTION);
    }
}
