package dev.tensor.misc.helpers.color;

import dev.tensor.misc.imp.render.Render2D;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 05-29-2026
 */

public final class TrueColorValuePoint implements Render2D {

    private final int boxWidth;
    private final int boxHeight;

    private ColorValueCache cache;
    private Color realColor;

    private Order highest = Order.None;
    private Order middle = Order.None;

    private int mainRedEnd = 0;
    private int mainGreenEnd = 0;
    private int mainBlueEnd = 0;

    public TrueColorValuePoint(int boxWidth, int boxHeight, ColorValueCache cache, Color realColor) {
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
        this.cache = cache;

        this.update(cache, realColor);
    }

    public void update(ColorValueCache cache, Color realColor) {
        this.cache = cache;
        this.realColor = realColor;

        this.highest = Order.None;

        this.mainRedEnd = 0;
        this.mainGreenEnd = 0;
        this.mainBlueEnd = 0;

        switch (this.cache.getRedOrderType()) {
            case Highest:
                this.highest = Order.Red;
                this.mainRedEnd = 255;
                break;
            case Middle:
                this.middle = Order.Red;
                this.mainRedEnd = cache.getDefaultColor().getRed();
                break;
            default:
                break;
        }

        switch (this.cache.getGreenOrderType()) {
            case Highest:
                this.highest = Order.Green;
                this.mainGreenEnd = 255;
                break;
            case Middle:
                this.middle = Order.Green;
                this.mainGreenEnd = cache.getDefaultColor().getGreen();
                break;
            default:
                break;
        }

        switch (this.cache.getBlueOrderType()) {
            case Highest:
                this.highest = Order.Blue;
                this.mainBlueEnd = 255;
                break;
            case Middle:
                this.middle = Order.Blue;
                this.mainBlueEnd = cache.getDefaultColor().getBlue();
                break;
            default:
                break;
        }
    }

    public Color getDefaultColor() {
        return this.cache.getDefaultColor();
    }

    public void renderPoint(int startX, int startY, int rgb) {
        int colorLR = 0;
        int colorUD = 0;

        switch (this.highest) {
            case Red:
                if (this.middle == Order.None) {
                    colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getGreen(), this.boxWidth);
                    colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getRed(), this.boxHeight);

                } else {
                    if (this.middle == Order.Green) {
                        colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getBlue(), this.boxWidth);
                        colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getRed(), this.boxHeight);
                    } else {
                        colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getGreen(), this.boxWidth);
                        colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getRed(), this.boxHeight);
                    }
                }
                break;

            case Green:
                if (this.middle == Order.None) {
                    colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getBlue(), this.boxWidth);
                    colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getGreen(), this.boxHeight);

                } else {
                    if (this.middle == Order.Red) {
                        colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getBlue(), this.boxWidth);
                        colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getGreen(), this.boxHeight);

                    } else {
                        colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getRed(), this.boxWidth);
                        colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getGreen(), this.boxHeight);
                    }
                }
                break;

            case Blue:
                if (this.middle == Order.None) {
                    colorLR = this.boxWidth - this.convertRGBToXY(this.realColor.getRed(), this.boxWidth);
                    colorUD = this.boxHeight - this.convertRGBToXY(this.realColor.getBlue(), this.boxHeight);

                } else {
                    if (this.middle == Order.Green) {
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

        int x1 = startX + colorLR;
        int wX = startX + this.boxWidth;

        int y1 = startY + colorUD;
        int wY = startY + this.boxHeight;

        int minX = x1 - (x1 - 3 >= startX ? 3 : x1 - startX);
        int minY = y1 - (y1 - 3 >= startY ? 3 : y1 - startY);
        int maxX = x1 + (x1 + 3 <= wX ? 3 : wX - x1);
        int maxY = y1 + (y1 + 3 <= wY ? 3 : wY - y1);

        this.drawRectangle(minX, minY, maxX, maxY, rgb);
    }

    private int convertRGBToXY(int RGB, int WIDTH_OR_HEIGHT) {
        double dRGB = (double) RGB / 255;

        return (int) (dRGB * WIDTH_OR_HEIGHT);
    }

    private enum Order {
        Red,
        Green,
        Blue,
        None
    }
}
