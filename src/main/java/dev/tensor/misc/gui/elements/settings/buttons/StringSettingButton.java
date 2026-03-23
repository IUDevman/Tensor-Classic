package dev.tensor.misc.gui.elements.settings.buttons;

import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.elements.ButtonSetting;
import dev.tensor.misc.imp.setting.settings.StringSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-23-2026
 */

public final class StringSettingButton implements ButtonSetting {

    public static final int height = 200;

    private final TensorGUI tensorGUI;
    private final ClickGUI clickGUI;
    private final StringSetting stringSetting;

    private boolean searching;
    private boolean active;

    public StringSettingButton(TensorGUI tensorGUI, ClickGUI clickGUI, StringSetting stringSetting) {
        this.tensorGUI = tensorGUI;
        this.clickGUI = clickGUI;
        this.stringSetting = stringSetting;
    }

    @Override
    public int getWidth() {
        return this.tensorGUI.guiWidth;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getPosX() {
        return this.clickGUI.x.getValue();
    }

    @Override
    public int getPosY() {
        return this.clickGUI.y.getValue();
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor, Color textColor, Color enabledTextColor) {
        if (!this.isActive()) {
            return;
        }

        //temp:
        this.draw2DRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), fillColor.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {

    }

    @Override
    public void onKeyPressed(int key) {

    }

    @Override
    public boolean isSearching() {
        return this.searching;
    }

    @Override
    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
