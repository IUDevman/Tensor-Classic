package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.color;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.settings.buttons.ColorSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.helpers.color.ColorValueCache;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 05-27-2026
 */

public final class SmallColorPickerObject implements ButtonSettingObject {

    private final ColorSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    public ColorValueCache defaultColorValueCache;

    private boolean draggingBigColorPicker = false;

    public SmallColorPickerObject(ColorSettingButton button, IntegerSetting x, IntegerSetting y, int offsetX, int offsetY, int width, int height) {
        this.button = button;
        this.x = x;
        this.y = y;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;

        this.button.colorSettingElement.colorSetting.addSmallColorPickerObjectToQueue(this);

        this.updateDefaultColor();
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

        this.drawSmallColorPickerRectangle(this.getPosX() + 2, this.getPosY() + 2, 28, 124);

        int barY = this.defaultColorValueCache.getDefaultBarHeight();

        if (barY > 122) {
            barY = 122;
        }

        this.drawRectangle(this.getPosX() + 2, this.getPosY() + 2 + barY, this.getPosX() + this.getWidth() - 2, this.getPosY() + 2 + barY + 2, Color.DARK_GRAY.getRGB());
    }

    public void setDraggingBigColorPicker(boolean draggingBigColorPicker) {
        this.draggingBigColorPicker = draggingBigColorPicker;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        //todo: dragging
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

    public void updateDefaultColor() {
        Color color = this.button.colorSettingElement.colorSetting.getValue();

        this.defaultColorValueCache = ColorValueCache.getCacheForColor(color, this.defaultColorValueCache == null ? 0 : this.defaultColorValueCache.getDefaultBarHeight());
    }
}
