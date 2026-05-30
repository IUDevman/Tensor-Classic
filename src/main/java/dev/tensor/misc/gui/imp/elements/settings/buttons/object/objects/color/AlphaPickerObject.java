package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.color;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.settings.buttons.ColorSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 05-27-2026
 */

public final class AlphaPickerObject implements ButtonSettingObject {

    private final ColorSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    public AlphaPickerObject(ColorSettingButton button, IntegerSetting x, IntegerSetting y, int offsetX, int offsetY, int width, int height) {
        this.button = button;
        this.x = x;
        this.y = y;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getPosX() {
        return this.x.getValue() + this.offsetX;
    }

    @Override
    public int getPosY() {
        return this.y.getValue() + this.offsetY;
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight() - 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + 2, this.getPosX() + 2, this.getPosY() + this.getHeight() - 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 2, this.getPosY() + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 2, outLineColor.getRGB());

        this.drawGradientRectangleTopBottom(this.getPosX() + 2, this.getPosY() + 2, this.getPosX() + this.getWidth() - 2, this.getPosY() + this.getHeight() - 2, Color.WHITE.getRGB(), Color.BLACK.getRGB());

        int alphaY = this.alphaToY(this.button.hasAlpha ? this.button.colorSettingElement.colorSetting.getValue().getAlpha() : 255);

        if (alphaY == 0) {
            alphaY = 2;
        }

        this.drawRectangle(this.getPosX() + 2, this.getPosY() + alphaY, this.getPosX() + this.getWidth() - 2, this.getPosY() + alphaY + 2, new Color(this.button.colorSettingElement.colorSetting.getValue().getRed(), this.button.colorSettingElement.colorSetting.getValue().getGreen(), this.button.colorSettingElement.colorSetting.getValue().getBlue(), 255).getRGB());

    }

    @Override
    public void onClick(double mouseX, double mouseY) {

    }

    @Override
    public void onHovered(int mouseX, int mouseY) {

    }

    @Override
    public boolean hovered() {
        return false;
    }

    @Override
    public void setHovered(boolean hovered) {

    }

    private int alphaToY(int alpha) {
        double diff = (double) alpha / 255;
        return (int) (diff * 124);
    }

    //height 124
    private int yToAlpha(int y, int startY) {
        int dY = startY - y;

        return (dY / 124) * 255;
    }

    public int getAlpha() {
        if (!this.button.hasAlpha) {
            return 255;
        }

        //todo
        return 255;
    }
}
