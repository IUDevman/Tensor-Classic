package dev.tensor.misc.gui.imp.elements.settings.buttons.object;

/**
 * @author Hoosiers
 * @since 05-30-2026
 */

public interface DraggableObject {

    void onRenderDrag(int screenX, int screenY);

    void resetDrag();
}
