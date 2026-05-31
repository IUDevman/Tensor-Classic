package dev.tensor.misc.helpers.color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Hoosiers
 * @since 05-29-2026
 */

public final class SliderDefaultColor {

    public static SliderDefaultColor getDefaultColorCache(Color inputColor, int previousDefaultBarHeight) {
        ArrayList<ColorTypeValue> colorValues = new ArrayList<>();
        colorValues.add(new ColorTypeValue(ColorTypeValue.Type.RED, inputColor.getRed()));
        colorValues.add(new ColorTypeValue(ColorTypeValue.Type.GREEN, inputColor.getGreen()));
        colorValues.add(new ColorTypeValue(ColorTypeValue.Type.BLUE, inputColor.getBlue()));

        colorValues.sort(Comparator.comparing(linkedColorValue -> ((ColorTypeValue) linkedColorValue).value).reversed());

        ColorTypeValue highest = colorValues.get(0);
        ColorTypeValue middle = colorValues.get(1);
        ColorTypeValue lowest = colorValues.get(2);

        int defaultBarHeight = 0;

        if (highest.type == ColorTypeValue.Type.RED) {
            if (middle.value == 0) {
                defaultBarHeight = previousDefaultBarHeight >= 103 ? 124 : 0;

            } else if (middle.value == lowest.value) {
                defaultBarHeight = previousDefaultBarHeight >= 103 ? 124 : 0;

            } else if (middle.type == ColorTypeValue.Type.GREEN) {
                defaultBarHeight = (int) (((double) middle.value / 255) * 20.667);

            } else if (middle.type == ColorTypeValue.Type.BLUE) {
                defaultBarHeight = 124 - (int) (((double) middle.value / 255) * 20.667);
            }

        } else if (highest.type == ColorTypeValue.Type.GREEN) {
            if (middle.value == 0) {
                defaultBarHeight = 41;

            } else if (middle.value == lowest.value) {
                defaultBarHeight = 41;

            } else if (middle.type == ColorTypeValue.Type.RED) {
                defaultBarHeight = (int) (41 - (((double) middle.value / 255) * 20.667));

            } else if (middle.type == ColorTypeValue.Type.BLUE) {
                defaultBarHeight = (int) (41 + (((double) middle.value / 255) * 20.667));
            }

        } else if (highest.type == ColorTypeValue.Type.BLUE) {
            if (middle.value == 0) {
                defaultBarHeight = 83;

            } else if (middle.value == lowest.value) {
                defaultBarHeight = 83;

            } else if (middle.type == ColorTypeValue.Type.RED) {
                defaultBarHeight = (int) (83 + (((double) middle.value / 255) * 20.667));

            } else if (middle.type == ColorTypeValue.Type.GREEN) {
                defaultBarHeight = (int) (83 - (((double) middle.value / 255) * 20.667));
            }
        }

        return new SliderDefaultColor(convertToDefaultColor(inputColor), defaultBarHeight);
    }

    private static Color convertToDefaultColor(Color inputColor) {
        int red = inputColor.getRed();
        int green = inputColor.getGreen();
        int blue = inputColor.getBlue();

        int maxComponent = Math.max(red, Math.max(green, blue));

        if (maxComponent == 0) {
            return new Color(255, 0, 0);
        }

        int baseRed = (int) Math.round((red * 255.0) / maxComponent);
        int baseGreen = (int) Math.round((green * 255.0) / maxComponent);
        int baseBlue = (int) Math.round((blue * 255.0) / maxComponent);

        int minComponent = Math.min(baseRed, Math.min(baseGreen, baseBlue));

        if (baseRed == minComponent) {
            baseRed = 0;

        } else if (baseGreen == minComponent) {
            baseGreen = 0;

        } else {
            baseBlue = 0;
        }

        return new Color(baseRed, baseGreen, baseBlue);
    }

    public static Color getDefaultColorFromBarHeight(int barHeight) {
        int red = 0;
        int green = 0;
        int blue = 0;

        if (barHeight >= 0 && barHeight <= 20) {
            red = 255;
            green = (int) Math.round((barHeight / 20.667) * 255);

        } else if (barHeight > 20 && barHeight <= 41) {
            red = (int) Math.round(((41 - barHeight) / 20.667) * 255);
            green = 255;

        } else if (barHeight > 41 && barHeight <= 62) {
            green = 255;
            blue = (int) Math.round(((barHeight - 41) / 20.667) * 255);

        } else if (barHeight > 62 && barHeight <= 83) {
            green = (int) Math.round(((82.667 - barHeight) / 20.667) * 255);
            blue = 255;

        } else if (barHeight > 83 && barHeight <= 103) {
            red = (int) Math.round(((barHeight - 82.667) / 20.667) * 255);
            blue = 255;

        } else if (barHeight > 103 && barHeight <= 124) {
            red = 255;
            blue = (int) Math.round(((124 - barHeight) / 20.667) * 255);
        }

        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        return new Color(red, green, blue);
    }

    private final Color defaultColor;
    private final int defaultBarHeight;

    private SliderDefaultColor(Color defaultColor, int defaultBarHeight) {
        this.defaultColor = defaultColor;
        this.defaultBarHeight = defaultBarHeight;
    }

    public Color getDefaultColor() {
        return this.defaultColor;
    }

    public int getDefaultBarHeight() {
        return this.defaultBarHeight;
    }
}
