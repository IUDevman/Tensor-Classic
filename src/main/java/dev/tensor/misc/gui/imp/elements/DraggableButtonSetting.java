package dev.tensor.misc.gui.imp.elements;

/**
 * @author Hoosiers
 * @since 05-30-2026
 */

public interface DraggableButtonSetting {

    void onRenderDrag(int screenX, int screenY);

    void onClickDrag(double mouseX, double mouseY);

    void resetDrag();
}
