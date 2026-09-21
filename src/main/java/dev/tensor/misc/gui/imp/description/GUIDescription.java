package dev.tensor.misc.gui.imp.description;

import dev.tensor.misc.imp.render.Render2D;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 09-20-2026
 */

public interface GUIDescription extends Render2D {

    Object getObject();

    void render(int posX, int posY, Color fillColor, Color outlineColor);

    default int getMaxTextLength() {
        return 300;
    }
}
