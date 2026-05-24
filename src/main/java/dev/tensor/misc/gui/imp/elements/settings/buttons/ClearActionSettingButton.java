package dev.tensor.misc.gui.imp.elements.settings.buttons;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.ButtonSetting;
import dev.tensor.misc.imp.setting.settings.GeneralButtonSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 05-24-2026
 */

public final class ClearActionSettingButton implements ButtonSetting {

    private final GeneralButtonSetting button;

    public ClearActionSettingButton(GeneralButtonSetting button) {
        this.button = button;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setActive(boolean active) {
        if (active) {
            this.button.onTriggerChange();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void onHovered(int mouseX, int mouseY) {

    }

    @Override
    public void handleScroll(int scroll) {

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
