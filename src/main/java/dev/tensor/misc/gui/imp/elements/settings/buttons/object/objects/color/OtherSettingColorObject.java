package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.color;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.Hoverable;
import dev.tensor.misc.gui.imp.elements.settings.buttons.ColorSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.ColorNoAlphaSetting;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 05-27-2026
 */

public final class OtherSettingColorObject implements ButtonSettingObject, Hoverable {

    private final ColorSettingButton button;
    private final Hack hack;
    private final ColorSetting colorSetting;
    private final boolean hasAlpha;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final IntegerSetting scrollY;
    private final int offsetY;
    private final int number;

    private boolean hovered;

    public OtherSettingColorObject(ColorSettingButton button, Hack hack, ColorSetting colorSetting, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int offsetY, int number) {
        this.button = button;
        this.hack = hack;
        this.colorSetting = colorSetting;
        this.hasAlpha = !(colorSetting instanceof ColorNoAlphaSetting);
        this.x = x;
        this.y = y;
        this.scrollY = scrollY;
        this.offsetY = offsetY;
        this.number = number;
    }

    @Override
    public int getWidth() {
        return this.button.tensorGUI.guiWidth;
    }

    @Override
    public int getHeight() {
        return 15;
    }

    @Override
    public int getPosX() {
        return this.x.getValue();
    }

    @Override
    public int getPosY() {
        return this.y.getValue() + this.scrollY.getValue() + this.offsetY + (this.number * this.getHeight());
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 2, this.hovered ? (this.isBlackNoAlpha(this.colorSetting.getValue()) ? this.colorSetting.getValue().brighter().brighter().getRGB() : this.colorSetting.getValue().darker().getRGB()) : this.colorSetting.getValue().getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight() - 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), outLineColor.getRGB());

        this.drawString(this.hack.NAME + " - " + this.colorSetting.getName() + " : (" + this.colorSetting.getValue().getRed() + ", " + this.colorSetting.getValue().getGreen() + ", " + this.colorSetting.getValue().getBlue() + ", " + (this.hasAlpha ? this.colorSetting.getValue().getAlpha() : "255") + ")", this.getPosX() + 2, this.getPosY() + 2, Color.WHITE.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.isHovered(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY)) {
            if (this.button.hasAlpha) {
                this.button.colorSettingElement.colorSetting.setValue(this.colorSetting.getValue());
            } else {
                Color noAlpha = new Color(this.colorSetting.getValue().getRed(), this.colorSetting.getValue().getGreen(), this.colorSetting.getValue().getBlue(), 255);

                this.button.colorSettingElement.colorSetting.setValue(noAlpha);
            }
        }
    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        this.hovered = this.isHovered(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY);
    }

    @Override
    public boolean hovered() {
        return this.hovered;
    }

    @Override
    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }
}
