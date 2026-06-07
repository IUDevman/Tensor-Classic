package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.color;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.settings.buttons.ColorSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.DraggableObject;
import dev.tensor.misc.helpers.color.SliderDefaultColor;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 05-27-2026
 */

public final class SmallColorPickerObject implements ButtonSettingObject, DraggableObject {

    private final ColorSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    public SliderDefaultColor defaultColor;

    public boolean isDragging = false;

    private int barY;

    private int dragOffsetY;

    private int dragYToSet;

    private int screenY;

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
        if (this.isDragging) {
            int startY = this.getPosY() + 2;
            int old = this.barY;

            this.dragYToSet = this.screenY + this.dragOffsetY;

            if (this.dragYToSet <= startY + 124 && this.dragYToSet >= startY && this.dragOffsetY != 0) {
                old = this.dragYToSet - startY;
            } else {
                this.dragYToSet = old + startY;
            }

            this.barY = old;

            this.updateDefaultColor(SliderDefaultColor.getDefaultColorFromBarHeight(this.barY));
        }

        this.startFlatGL(false);
        this.prepRectangle(outLineColor.getRGB());

        this.genRectangleVertices(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + 2);
        this.genRectangleVertices(this.getPosX(), this.getPosY() + this.getHeight() - 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight());
        this.genRectangleVertices(this.getPosX(), this.getPosY() + 2, this.getPosX() + 2, this.getPosY() + this.getHeight() - 2);
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 2, this.getPosY() + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 2);

        this.finishRectangle();
        this.finishFlatGL(false);

        this.drawSmallColorPickerRectangle(this.getPosX() + 2, this.getPosY() + 2, 28, 124);

        int barY = this.defaultColor.getDefaultBarHeight();

        if (barY > 122) {
            barY = 122;
        }

        this.drawRectangle(this.getPosX() + 2, this.getPosY() + 2 + barY, this.getPosX() + this.getWidth() - 2, this.getPosY() + 2 + barY + 2, Color.DARK_GRAY.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        int boxStartY = this.getPosY() + 2;

        if (this.isHovered(this.getPosX() + 2, boxStartY + this.barY - 3, this.getPosX() + this.getWidth() - 2, boxStartY + this.barY + 3, mouseX, mouseY)) {
            this.isDragging = true;

            this.dragYToSet = boxStartY + this.barY;

            int newDragOffsetY = (int) (this.dragYToSet - mouseY);

            if (newDragOffsetY + boxStartY + 2 <= 0) {
                newDragOffsetY = this.dragYToSet;
            }

            this.dragOffsetY = newDragOffsetY;

        } else {
            this.isDragging = false;
        }
    }

    @Override
    public void onRenderDrag(int screenX, int screenY) {
        this.screenY = screenY;
    }

    @Override
    public void resetDrag() {
        this.isDragging = false;
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

        this.updateDefaultColor(color);
    }

    private void updateDefaultColor(Color color) {
        this.defaultColor = SliderDefaultColor.getDefaultColorCache(color, this.defaultColor == null ? 0 : this.defaultColor.getDefaultBarHeight());
        this.barY = this.defaultColor.getDefaultBarHeight();
    }
}
