package dev.tensor.misc.helpers.color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Hoosiers
 * @since 05-29-2026
 */

public final class ColorValueCache {

    public static ColorValueCache getCacheForColor(Color color, int previousDefaultBarHeight) {
        ArrayList<LinkedColorValue> colorValues = new ArrayList<>();
        colorValues.add(new LinkedColorValue(LinkedColorValue.Type.RED, color.getRed()));
        colorValues.add(new LinkedColorValue(LinkedColorValue.Type.GREEN, color.getGreen()));
        colorValues.add(new LinkedColorValue(LinkedColorValue.Type.BLUE, color.getBlue()));

        colorValues.sort(Comparator.comparing(linkedColorValue -> ((LinkedColorValue) linkedColorValue).value).reversed());

        LinkedColorValue highest = colorValues.get(0);
        LinkedColorValue middle = colorValues.get(1);
        LinkedColorValue lowest = colorValues.get(2);

        int defaultBarHeight = 0;

        if (highest.type == LinkedColorValue.Type.RED) {
            if (middle.value == 0) {
                defaultBarHeight = previousDefaultBarHeight >= 103 ? 124 : 0;

            } else if (middle.value == lowest.value) {
                defaultBarHeight = previousDefaultBarHeight >= 103 ? 124 : 0;

            } else if (middle.type == LinkedColorValue.Type.GREEN) {
                defaultBarHeight = (int) (((double) middle.value / 255) * 20.667);

            } else if (middle.type == LinkedColorValue.Type.BLUE) {
                defaultBarHeight = 124 - (int) (((double) middle.value / 255) * 20.667);
            }

        } else if (highest.type == LinkedColorValue.Type.GREEN) {
            if (middle.value == 0) {
                defaultBarHeight = 41;

            } else if (middle.value == lowest.value) {
                defaultBarHeight = 41;

            } else if (middle.type == LinkedColorValue.Type.RED) {
                defaultBarHeight = (int) (41 - (((double) middle.value / 255) * 20.667));

            } else if (middle.type == LinkedColorValue.Type.BLUE) {
                defaultBarHeight = (int) (41 + (((double) middle.value / 255) * 20.667));
            }

        } else if (highest.type == LinkedColorValue.Type.BLUE) {
            if (middle.value == 0) {
                defaultBarHeight = 83;

            } else if (middle.value == lowest.value) {
                defaultBarHeight = 83;

            } else if (middle.type == LinkedColorValue.Type.RED) {
                defaultBarHeight = (int) (83 + (((double) middle.value / 255) * 20.667));

            } else if (middle.type == LinkedColorValue.Type.GREEN) {
                defaultBarHeight = (int) (83 - (((double) middle.value / 255) * 20.667));
            }
        }

        return new ColorValueCache(convertToDefaultColor(color), defaultBarHeight);
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

    private final Color defaultColor;
    private final int defaultBarHeight;

    private ColorValueCache(Color defaultColor, int defaultBarHeight) {
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
