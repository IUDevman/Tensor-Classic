package dev.tensor.misc.helpers.color;

import dev.tensor.misc.gui.imp.Hoverable;
import dev.tensor.misc.gui.imp.elements.settings.buttons.ColorSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.color.BigColorPickerObject;
import dev.tensor.misc.imp.render.Render2D;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 05-29-2026
 */

public final class TrueColorValuePoint implements Render2D, Hoverable {

    private final int box_Width_Height;

    private ColorValueCache cache;
    private Color realColor;

    private boolean isDragging = false;

    private int pointX;
    private int pointY;

    private int dragOffsetX;
    private int dragOffsetY;

    private int dragXToSet;
    private int dragYToSet;

    public TrueColorValuePoint(int box_Width_Height, ColorValueCache cache, Color realColor) {
        this.box_Width_Height = box_Width_Height;
        this.cache = cache;

        this.updateAll(cache, realColor);
    }

    public void updateAll(ColorValueCache cache, Color newColor) {
        this.cache = cache;

        this.realColor = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue());

        int[] old = this.getCoordinatesFromColor(this.realColor, this.cache.getDefaultColor());

        this.pointX = old[0];
        this.pointY = old[1];
    }

    public void updateColorFromDrag(int screenX, int screenY, int startX, int startY, ColorSettingButton colorSettingButton) {
        if (!this.isDragging) {
            return;
        }

        int[] old = new int[]{this.pointX, this.pointY};

        this.dragXToSet = screenX + dragOffsetX;
        this.dragYToSet = screenY + dragOffsetY;

        if (this.dragXToSet <= startX + this.box_Width_Height && this.dragXToSet >= startX && this.dragOffsetX != 0) {
            old[0] = this.dragXToSet - startX;
        } else {
            this.dragXToSet = old[0] + startX;
        }

        if (this.dragYToSet <= startY + this.box_Width_Height && this.dragYToSet >= startY && this.dragOffsetY != 0) {
            old[1] = this.dragYToSet - startY;
        } else {
            this.dragYToSet = old[1] + startY;
        }

        this.pointX = old[0];
        this.pointY = old[1];

        Color color = this.getColorFromCoordinates(this.pointX, this.pointY, this.box_Width_Height, this.cache.getDefaultColor());

        colorSettingButton.setNewColorStealth(color);
        this.realColor = color;
    }

    public void handleDraggingClick(int boxStartX, int boxStartY, int mouseX, int mouseY, BigColorPickerObject bigColorPickerObject) {
        if (this.isHovered(boxStartX + this.pointX - 3, boxStartY + this.pointY - 3, boxStartX + this.pointX + 3, boxStartY + this.pointY + 3, mouseX, mouseY)) {
            this.isDragging = true;
            bigColorPickerObject.button.smallColorPickerObject.setDraggingBigColorPicker(true);

            int[] old = new int[]{this.pointX, this.pointY};

            this.dragXToSet = boxStartX + old[0];
            this.dragYToSet = boxStartY + old[1];

            int newDragOffsetX = this.dragXToSet - mouseX;
            int newDragOffsetY = this.dragYToSet - mouseY;

            if (newDragOffsetX + boxStartX + 3 <= 0) {
                newDragOffsetX = this.dragXToSet;
            }

            if (newDragOffsetY + boxStartY + 3 <= 0) {
                newDragOffsetY = this.dragYToSet;
            }

            this.dragOffsetX = newDragOffsetX;
            this.dragOffsetY = newDragOffsetY;

        } else {
            this.isDragging = false;
            bigColorPickerObject.button.smallColorPickerObject.setDraggingBigColorPicker(false);
        }
    }

    public void resetDrag() {
        this.isDragging = false;
    }

    public void renderPoint(int startX, int startY, int rgb) {
        int x1 = startX + pointX;
        int wX = startX + this.box_Width_Height;

        int y1 = startY + pointY;
        int wY = startY + this.box_Width_Height;

        int minX = x1 - (x1 - 3 >= startX ? 3 : x1 - startX);
        int minY = y1 - (y1 - 3 >= startY ? 3 : y1 - startY);
        int maxX = x1 + (x1 + 3 <= wX ? 3 : wX - x1);
        int maxY = y1 + (y1 + 3 <= wY ? 3 : wY - y1);

        this.drawRectangle(minX, minY, maxX, maxY, rgb);
    }

    public int[] getCoordinatesFromColor(Color targetColor, Color defaultColor) {
        int red = targetColor.getRed();
        int green = targetColor.getGreen();
        int blue = targetColor.getBlue();

        int maxTarget = Math.max(red, Math.max(green, blue));
        int maxDefault = Math.max(defaultColor.getRed(), Math.max(defaultColor.getGreen(), defaultColor.getBlue()));

        double pctY = (maxDefault > 0) ? (double) maxTarget / maxDefault : 0.0;

        int x = 0;
        int y = (int) Math.round(124 * (1.0 - pctY));

        if (pctY > 0) {
            int topRed = Math.max(0, Math.min((int) Math.round(red / pctY), 255));
            int topGreen = Math.max(0, Math.min((int) Math.round(green / pctY), 255));
            int topBlue = Math.max(0, Math.min((int) Math.round(blue / pctY), 255));

            int dRed = defaultColor.getRed() - 255;
            int dGreen = defaultColor.getGreen() - 255;
            int dBlue = defaultColor.getBlue() - 255;

            int maxDiff = Math.max(Math.abs(dRed), Math.max(Math.abs(dGreen), Math.abs(dBlue)));

            if (maxDiff == Math.abs(dRed) && dRed != 0) {
                x = (int) Math.round((124.0 * (topRed - 255)) / dRed);

            } else if (maxDiff == Math.abs(dGreen) && dGreen != 0) {
                x = (int) Math.round((124.0 * (topGreen - 255)) / dGreen);

            } else if (dBlue != 0) {
                x = (int) Math.round((124.0 * (topBlue - 255)) / dBlue);
            }
        }

        x = Math.max(0, Math.min(x, 124));
        y = Math.max(0, Math.min(y, 124));

        return new int[]{x, y};
    }

    public Color getColorFromCoordinates(int x, int y, int width_or_height, Color defaultColor) {
        x = Math.max(0, Math.min(x, width_or_height));
        y = Math.max(0, Math.min(y, width_or_height));

        double pctX = (double) x / width_or_height;
        double pctY = (double) (width_or_height - y) / width_or_height;

        int topRed = (int) (255 + pctX * (defaultColor.getRed() - 255));
        int topGreen = (int) (255 + pctX * (defaultColor.getGreen() - 255));
        int topBlue = (int) (255 + pctX * (defaultColor.getBlue() - 255));

        int finalRed = (int) (topRed * pctY);
        int finalGreen = (int) (topGreen * pctY);
        int finalBlue = (int) (topBlue * pctY);

        return new Color(finalRed, finalGreen, finalBlue);
    }
}
