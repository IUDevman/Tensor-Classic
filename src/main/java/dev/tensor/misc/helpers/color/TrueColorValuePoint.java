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

    private final int boxWidth;
    private final int boxHeight;

    private ColorValueCache cache;
    private Color realColor;

    private ColorOrder highest = ColorOrder.None;
    private ColorOrder middle = ColorOrder.None;

    private int mainRedEnd = 0;
    private int mainGreenEnd = 0;
    private int mainBlueEnd = 0;

    private boolean isDragging = false;

    private int pointX;
    private int pointY;

    private int dragOffsetX;
    private int dragOffsetY;

    private int dragXToSet;
    private int dragYToSet;

    public TrueColorValuePoint(int boxWidth, int boxHeight, ColorValueCache cache, Color realColor) {
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
        this.cache = cache;

        this.updateAll(cache, realColor);
    }

    public void updateAll(ColorValueCache cache, Color newColor) {
        this.cache = cache;

        this.highest = ColorOrder.None;
        this.middle = ColorOrder.None;

        this.mainRedEnd = 0;
        this.mainGreenEnd = 0;
        this.mainBlueEnd = 0;

        switch (this.cache.getRedOrderType()) {
            case Highest:
                this.highest = ColorOrder.Red;
                this.mainRedEnd = 255;
                break;
            case Middle:
                this.middle = ColorOrder.Red;
                this.mainRedEnd = cache.getDefaultColor().getRed();
                break;
            default:
                break;
        }

        switch (this.cache.getGreenOrderType()) {
            case Highest:
                this.highest = ColorOrder.Green;
                this.mainGreenEnd = 255;
                break;
            case Middle:
                this.middle = ColorOrder.Green;
                this.mainGreenEnd = cache.getDefaultColor().getGreen();
                break;
            default:
                break;
        }

        switch (this.cache.getBlueOrderType()) {
            case Highest:
                this.highest = ColorOrder.Blue;
                this.mainBlueEnd = 255;
                break;
            case Middle:
                this.middle = ColorOrder.Blue;
                this.mainBlueEnd = cache.getDefaultColor().getBlue();
                break;
            default:
                break;
        }

        Color oldColor = this.realColor;
        this.realColor = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue());

        if (oldColor == null || this.realColor.getRGB() != oldColor.getRGB()) {
            int[] old = this.getRenderPointForCurrentColor();

            this.pointX = old[0];
            this.pointY = old[1];
        }
    }

    public void updateColorFromDrag(int screenX, int screenY, int startX, int startY, ColorSettingButton colorSettingButton) {
        if (!this.isDragging) {
            return;
        }

        int[] old = new int[]{this.pointX, this.pointY};

        this.dragXToSet = screenX + dragOffsetX;
        this.dragYToSet = screenY + dragOffsetY;

        if (this.dragXToSet <= startX + this.boxWidth && this.dragXToSet >= startX && this.dragOffsetX != 0) {
            old[0] = this.dragXToSet - startX;
        } else {
            this.dragXToSet = old[0] + startX;
        }

        if (this.dragYToSet <= startY + this.boxHeight && this.dragYToSet >= startY && this.dragOffsetY != 0) {
            old[1] = this.dragYToSet - startY;
        } else {
            this.dragYToSet = old[1] + startY;
        }

        this.pointX = old[0];
        this.pointY = old[1];

        Color color = this.convertFromXYToColor(this.pointX, this.pointY);

        colorSettingButton.setNewColorFrom(color);
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
        int wX = startX + this.boxWidth;

        int y1 = startY + pointY;
        int wY = startY + this.boxHeight;

        int minX = x1 - (x1 - 3 >= startX ? 3 : x1 - startX);
        int minY = y1 - (y1 - 3 >= startY ? 3 : y1 - startY);
        int maxX = x1 + (x1 + 3 <= wX ? 3 : wX - x1);
        int maxY = y1 + (y1 + 3 <= wY ? 3 : wY - y1);

        this.drawRectangle(minX, minY, maxX, maxY, rgb);
    }

    private int[] getRenderPointForCurrentColor() {
        int colorLR = 0;
        int colorUD = 0;

        switch (this.highest) {
            case Red:
                if (this.middle == ColorOrder.None) {
                    colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getGreen(), this.boxWidth);
                    colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getRed(), this.boxHeight);

                } else {
                    if (this.middle == ColorOrder.Green) {
                        colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getBlue(), this.boxWidth);
                        colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getRed(), this.boxHeight);
                    } else {
                        colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getGreen(), this.boxWidth);
                        colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getRed(), this.boxHeight);
                    }
                }
                break;

            case Green:
                if (this.middle == ColorOrder.None) {
                    colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getBlue(), this.boxWidth);
                    colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getGreen(), this.boxHeight);

                } else {
                    if (this.middle == ColorOrder.Red) {
                        colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getBlue(), this.boxWidth);
                        colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getGreen(), this.boxHeight);

                    } else {
                        colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getRed(), this.boxWidth);
                        colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getGreen(), this.boxHeight);
                    }
                }
                break;

            case Blue:
                if (this.middle == ColorOrder.None) {
                    colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getRed(), this.boxWidth);
                    colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getBlue(), this.boxHeight);

                } else {
                    if (this.middle == ColorOrder.Green) {
                        colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getRed(), this.boxWidth);
                        colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getBlue(), this.boxHeight);

                    } else {
                        colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getGreen(), this.boxWidth);
                        colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getBlue(), this.boxHeight);
                    }
                }
                break;

            default:
                break;
        }

        return new int[]{colorLR, colorUD};
    }

    private Color convertFromXYToColor(int x, int y) {
        int redNew = 0;
        int greenNew = 0;
        int blueNew = 0;

        int unorderedRGB = 255 - this.convertXYToRGB(x, this.boxWidth, 255);

        switch (this.highest) {
            case Red:
                if (this.middle == ColorOrder.None) {
                    redNew = this.mainRedEnd - this.convertXYToRGB(y, this.boxHeight, this.mainRedEnd);
                    greenNew = unorderedRGB - this.convertXYToRGB(y, this.boxWidth, unorderedRGB);
                    blueNew = unorderedRGB - this.convertXYToRGB(y, this.boxWidth, unorderedRGB);

                } else {
                    if (this.middle == ColorOrder.Green) {
                        redNew = this.mainRedEnd - this.convertXYToRGB(y, this.boxHeight, this.mainRedEnd);
                        greenNew = this.mainGreenEnd - this.convertXYToRGB(y, this.boxHeight, this.mainGreenEnd);
                        blueNew = unorderedRGB - this.convertXYToRGB(y, this.boxWidth, unorderedRGB);
                    } else {
                        redNew = this.mainRedEnd - this.convertXYToRGB(y, this.boxHeight, this.mainRedEnd);
                        greenNew = unorderedRGB - this.convertXYToRGB(y, this.boxWidth, unorderedRGB);
                        blueNew = this.mainBlueEnd - this.convertXYToRGB(y, this.boxHeight, this.mainBlueEnd);
                    }
                }
                break;

            case Green:
                if (this.middle == ColorOrder.None) {
                    greenNew = this.mainGreenEnd - this.convertXYToRGB(y, this.boxHeight, this.mainGreenEnd);
                    redNew = unorderedRGB - this.convertXYToRGB(y, this.boxWidth, unorderedRGB);
                    blueNew = unorderedRGB - this.convertXYToRGB(y, this.boxWidth, unorderedRGB);

                } else {
                    if (this.middle == ColorOrder.Red) {
                        redNew = this.mainRedEnd - this.convertXYToRGB(y, this.boxHeight, this.mainRedEnd);
                        greenNew = this.mainGreenEnd - this.convertXYToRGB(y, this.boxHeight, this.mainGreenEnd);
                        blueNew = unorderedRGB - this.convertXYToRGB(y, this.boxWidth, unorderedRGB);
                    } else {
                        redNew = unorderedRGB - this.convertXYToRGB(y, this.boxWidth, unorderedRGB);
                        greenNew = this.mainGreenEnd - this.convertXYToRGB(y, this.boxHeight, this.mainGreenEnd);
                        blueNew = this.mainBlueEnd - this.convertXYToRGB(y, this.boxHeight, this.mainBlueEnd);
                    }
                }
                break;

            case Blue:
                if (this.middle == ColorOrder.None) {
                    blueNew = this.mainBlueEnd - this.convertXYToRGB(y, this.boxHeight, this.mainBlueEnd);
                    redNew = unorderedRGB - this.convertXYToRGB(y, this.boxWidth, unorderedRGB);
                    greenNew = unorderedRGB - this.convertXYToRGB(y, this.boxWidth, unorderedRGB);

                } else {
                    if (this.middle == ColorOrder.Red) {
                        redNew = this.mainRedEnd - this.convertXYToRGB(y, this.boxHeight, this.mainRedEnd);
                        greenNew = unorderedRGB - this.convertXYToRGB(y, this.boxWidth, unorderedRGB);
                        blueNew = this.mainBlueEnd - this.convertXYToRGB(y, this.boxHeight, this.mainBlueEnd);
                    } else {
                        redNew = unorderedRGB - this.convertXYToRGB(y, this.boxWidth, unorderedRGB);
                        greenNew = this.mainGreenEnd - this.convertXYToRGB(y, this.boxWidth, this.mainGreenEnd);
                        blueNew = this.mainBlueEnd - this.convertXYToRGB(y, this.boxHeight, this.mainBlueEnd);
                    }
                }
                break;

            default:
                break;
        }

        return new Color(redNew, greenNew, blueNew);
    }

    private int convertRGBToXY(int RGB, int WIDTH_OR_HEIGHT) {
        double dRGB = (double) RGB / 255;

        return (int) (dRGB * WIDTH_OR_HEIGHT);
    }

    private int convertXYToRGB(int XY, int WIDTH_OR_HEIGHT, int maxRGB) {
        double dXY = (double) XY / WIDTH_OR_HEIGHT;

        int rgb = (int) (dXY * maxRGB);

        if (rgb > maxRGB) {
            return maxRGB;
        } else return Math.max(rgb, 0);
    }

    private enum ColorOrder {
        Red,
        Green,
        Blue,
        None
    }
}
