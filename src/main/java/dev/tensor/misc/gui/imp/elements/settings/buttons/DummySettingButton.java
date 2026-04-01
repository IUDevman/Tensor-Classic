package dev.tensor.misc.gui.imp.elements.settings.buttons;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.ButtonSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-27-2026
 */

public final class DummySettingButton implements ButtonSetting {

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setActive(boolean active) {

    }

    @Override
    public void close() {

    }

    @Override
    public void onHovered(int mouseX, int mouseY) {

    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getPosX() {
        return 0;
    }

    @Override
    public int getPosY() {
        return 0;
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {

    }

    @Override
    public void onClick(double mouseX, double mouseY) {

    }

    @Override
    public void handleScroll(int scroll) {

    }

    @Override
    public boolean isSearching() {
        return false;
    }

    @Override
    public void setSearching(boolean searching) {

    }

    @Override
    public void onKeyPressed(char letter, int key) {

    }
}
