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

    /**
     * todo: This dragging shit is bugged RN.
     */

    private final ColorSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    private int alpha = 255;

    private int screenX;
    private int screenY;

    private boolean isDragging = false;

    private int barY;

    private int dragOffsetY;

    private int dragYToSet;

    public AlphaPickerObject(ColorSettingButton button, IntegerSetting x, IntegerSetting y, int offsetX, int offsetY, int width, int height) {
        this.button = button;
        this.x = x;
        this.y = y;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;

        if (this.button.hasAlpha) {
            this.alpha = this.button.colorSettingElement.colorSetting.getValue().getAlpha();
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
            int oldY = this.barY + startY;

            this.dragYToSet = screenY + dragOffsetY;

            if (this.dragYToSet <= startY + 124 && this.dragYToSet >= startY && this.dragOffsetY != 0) {
                oldY = this.dragYToSet - startY;
            } else {
                this.dragYToSet = oldY + startY;
            }

            this.barY = oldY;

            this.alpha = this.yToAlpha(this.barY);
        }

        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight() - 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + 2, this.getPosX() + 2, this.getPosY() + this.getHeight() - 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 2, this.getPosY() + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 2, outLineColor.getRGB());

        this.drawGradientRectangleTopBottom(this.getPosX() + 2, this.getPosY() + 2, this.getPosX() + this.getWidth() - 2, this.getPosY() + this.getHeight() - 2, Color.WHITE.getRGB(), Color.BLACK.getRGB());

        int alphaY = this.alphaToY(this.alpha);

        if (alphaY == 0) {
            alphaY = 2;
        }

        this.drawRectangle(this.getPosX() + 2, this.getPosY() + alphaY, this.getPosX() + this.getWidth() - 2, this.getPosY() + alphaY + 2, new Color(this.button.colorSettingElement.colorSetting.getValue().getRed(), this.button.colorSettingElement.colorSetting.getValue().getGreen(), this.button.colorSettingElement.colorSetting.getValue().getBlue(), 255).getRGB());

    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (!this.button.hasAlpha) {
            return;
        }

        int alphaY = this.alphaToY(this.alpha);

        if (alphaY == 0) {
            alphaY = 2;
        }

        if (this.isHovered(this.getPosX() + 2, this.getPosY() + 2 + alphaY, this.getPosX() + this.getWidth() - 2, this.getPosY() + alphaY + 4, mouseX, mouseY)) {
            this.isDragging = true;

            int boxStartY = this.getPosY() + 2;

            this.dragYToSet = boxStartY + this.barY;

            int newDragOffsetY = (int) (this.dragYToSet - mouseY);

            if (newDragOffsetY + boxStartY + 3 <= 0) {
                newDragOffsetY = this.dragYToSet;
            }

            this.dragOffsetY = newDragOffsetY;

        } else {
            this.isDragging = false;
        }
    }

    @Override
    public void onRenderDrag(int screenX, int screenY) {
        this.screenX = screenX;
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

    private int alphaToY(int alpha) {
        double diff = (double) alpha / 255;
        return (int) (diff * 124);
    }

    //height 124
    private int yToAlpha(int y) {
        int alpha = (y / 124) * 255;

        return Math.min(alpha, 255);

    }

    public int getAlpha() {
        return this.alpha;
    }
}
