package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.color;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.settings.buttons.ColorSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.DraggableObject;
import dev.tensor.misc.helpers.color.ColorPickerPoint;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 05-27-2026
 */

public final class BigColorPickerObject implements ButtonSettingObject, DraggableObject {

    public final ColorSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    private final ColorPickerPoint colorPickerPoint;

    private int screenX;
    private int screenY;

    public BigColorPickerObject(ColorSettingButton button, IntegerSetting x, IntegerSetting y, int offsetX, int offsetY, int width, int height) {
        this.button = button;
        this.x = x;
        this.y = y;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;

        this.colorPickerPoint = new ColorPickerPoint(124, this.button.smallColorPickerObject.defaultColor, this.button.colorSettingElement.colorSetting.getValue());
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
        if (!this.button.smallColorPickerObject.isDragging) {
            this.colorPickerPoint.updateAll(this.button.smallColorPickerObject.defaultColor, this.button.colorSettingElement.colorSetting.getValue());

            this.colorPickerPoint.updateColorFromDrag(this.screenX, this.screenY, this.getPosX() + 2, this.getPosY() + 2, this.button);
        } else {
            this.colorPickerPoint.updateFromSmallColorPickerDragging(this.button.smallColorPickerObject.defaultColor, this.button);
        }

        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight() - 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + 2, this.getPosX() + 2, this.getPosY() + this.getHeight() - 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 2, this.getPosY() + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 2, outLineColor.getRGB());

        this.drawLargeColorPickerRectangle(this.getPosX() + 2, this.getPosY() + 2, 124, 124, this.button.smallColorPickerObject.defaultColor.getDefaultColor());

        this.colorPickerPoint.renderPoint(this.getPosX() + 2, this.getPosY() + 2, Color.DARK_GRAY.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.colorPickerPoint.handleDraggingClick(this.getPosX() + 2, this.getPosY() + 2, (int) mouseX, (int) mouseY);
    }

    @Override
    public void onRenderDrag(int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
    }

    @Override
    public void resetDrag() {
        this.colorPickerPoint.resetDrag();
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

}
