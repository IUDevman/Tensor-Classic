package dev.tensor.misc.imp.hack;

import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

/**
 * @author Hoosiers
 * @since 03-24-2026
 */

public interface HUDObject extends Globals {

    IntegerSetting getMinX();

    IntegerSetting getMinY();

    int getWidth();

    int getHeight();

    void render();

    default int getMaxX() {
       return 5000;
    }

    default int getMaxY() {
        return 2000;
    }
}
