package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.color;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.settings.buttons.ColorSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.DraggableObject;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 05-27-2026
 */

public final class AlphaPickerObject implements ButtonSettingObject, DraggableObject {

    private final ColorSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    private boolean isDragging = false;

    private int pointY;

    private int dragOffsetY;

    private int dragYToSet;

    private int screenY;

    public AlphaPickerObject(ColorSettingButton button, IntegerSetting x, IntegerSetting y, int offsetX, int offsetY, int width, int height) {
        this.button = button;
        this.x = x;
        this.y = y;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;

        if (this.button.hasAlpha) {
            this.pointY = this.getYFromAlpha(this.button.colorSettingElement.colorSetting.getValue().getAlpha());
        }
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
            int old = this.pointY;

            this.dragYToSet = this.screenY + this.dragOffsetY;

            if (this.dragYToSet <= startY + 124 && this.dragYToSet >= startY && this.dragOffsetY != 0) {
                old = this.dragYToSet - startY;
            } else {
                this.dragYToSet = old + startY;
            }

            this.pointY = old;

            Color oldColor = this.button.colorSettingElement.colorSetting.getValue();
            this.button.setNewColorStealth(new Color(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), this.getAlphaFromY(this.pointY)), true);
        } else {
            this.pointY = this.getYFromAlpha(this.button.colorSettingElement.colorSetting.getValue().getAlpha());
        }

        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight() - 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + 2, this.getPosX() + 2, this.getPosY() + this.getHeight() - 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 2, this.getPosY() + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 2, outLineColor.getRGB());

        this.drawGradientRectangleTopBottom(this.getPosX() + 2, this.getPosY() + 2, this.getPosX() + this.getWidth() - 2, this.getPosY() + this.getHeight() - 2, Color.WHITE.getRGB(), Color.BLACK.getRGB());

        int barHeight = this.pointY >= 124 ? -2 : 0;

        this.drawRectangle(this.getPosX() + 2, this.getPosY() + 2 + this.pointY + barHeight, this.getPosX() + this.getWidth() - 2, this.getPosY() + this.pointY + 4 + barHeight, new Color(this.button.colorSettingElement.colorSetting.getValue().getRed(), this.button.colorSettingElement.colorSetting.getValue().getGreen(), this.button.colorSettingElement.colorSetting.getValue().getBlue(), 255).getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (!this.button.hasAlpha) {
            return;
        }

        int boxStartY = this.getPosY() + 2;

        if (this.isHovered(this.getPosX() + 2, boxStartY + this.pointY - 3, this.getPosX() + this.getWidth() - 2, boxStartY + this.pointY + 3, mouseX, mouseY)) {
            this.isDragging = true;

            this.dragYToSet = boxStartY + this.pointY;

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

    private int getYFromAlpha(int alpha) {
        if (!this.button.hasAlpha) {
            return 124;
        }

        double dAlpha = (double) alpha / 255;
        int y = (int) (124 * dAlpha);

        if (y > 124) {
            return 124;
        } else if (y < 0) {
            return 0;
        }

        return y;
    }

    private int getAlphaFromY(int y) {
        double dY = (double) y / 124;

        int alpha = (int) (dY * 255);

        if (alpha < 0) {
            return 0;
        } else return Math.min(alpha, 255);
    }
}
