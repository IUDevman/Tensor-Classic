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

        colorValues.sort(Comparator.comparing(linkedColorValue -> ((LinkedColorValue) linkedColorValue).toTestFloat()).reversed());

        LinkedColorValue highest = colorValues.get(0);
        LinkedColorValue middle = colorValues.get(1);
        LinkedColorValue lowest = colorValues.get(2);

        int highestVal = Math.max(highest.value, 255);
        int middleVal = middle.value;

        if (highest.toTestFloat() == middle.toTestFloat() && middle.toTestFloat() == lowest.toTestFloat()) {
            return new ColorValueCache(new Color(highestVal, 0, 0), 0, OrderType.Highest, OrderType.Dont_Care, OrderType.Dont_Care);
        }

        if (highest.type == LinkedColorValue.Type.RED) {
            if (middle.toTestFloat() == 0.0F) {
                return new ColorValueCache(new Color(highestVal, 0, 0), previousDefaultBarHeight >= 103 ? 124 : 0, OrderType.Highest, OrderType.Dont_Care, OrderType.Dont_Care);

            } else if (middle.toTestFloat() == lowest.toTestFloat()) {
                return new ColorValueCache(new Color(highestVal, 0, 0), previousDefaultBarHeight >= 103 ? 124 : 0, OrderType.Highest, OrderType.Dont_Care, OrderType.Dont_Care);

            } else if (middle.type == LinkedColorValue.Type.GREEN) {
                return new ColorValueCache(new Color(highestVal, middleVal, 0), (int) (((double) middleVal / 255) * 20.667), OrderType.Highest, OrderType.Middle, OrderType.Dont_Care);

            } else if (middle.type == LinkedColorValue.Type.BLUE) {
                return new ColorValueCache(new Color(highestVal, 0, middleVal), 124 - (int) (((double) middleVal / 255) * 20.667), OrderType.Highest, OrderType.Dont_Care, OrderType.Middle);
            }

        } else if (highest.type == LinkedColorValue.Type.GREEN) {
            if (middle.toTestFloat() == 0.0F) {
                return new ColorValueCache(new Color(0, highestVal, 0), 41, OrderType.Dont_Care, OrderType.Highest, OrderType.Dont_Care);

            } else if (middle.toTestFloat() == lowest.toTestFloat()) {
                return new ColorValueCache(new Color(0, highestVal, 0), 41, OrderType.Dont_Care, OrderType.Highest, OrderType.Dont_Care);

            } else if (middle.type == LinkedColorValue.Type.RED) {
                return new ColorValueCache(new Color(middleVal, highestVal, 0), (int) (41 - (((double) middleVal / 255) * 20.667)), OrderType.Middle, OrderType.Highest, OrderType.Dont_Care);

            } else if (middle.type == LinkedColorValue.Type.BLUE) {
                return new ColorValueCache(new Color(0, highestVal, middleVal), (int) (41 + (((double) middleVal / 255) * 20.667)), OrderType.Dont_Care, OrderType.Highest, OrderType.Middle);
            }

        } else if (highest.type == LinkedColorValue.Type.BLUE) {
            if (middle.toTestFloat() == 0.0F) {
                return new ColorValueCache(new Color(0, 0, highestVal), 83, OrderType.Dont_Care, OrderType.Dont_Care, OrderType.Highest);

            } else if (middle.toTestFloat() == lowest.toTestFloat()) {
                return new ColorValueCache(new Color(0, 0, highestVal), 83, OrderType.Dont_Care, OrderType.Dont_Care, OrderType.Highest);

            } else if (middle.type == LinkedColorValue.Type.RED) {
                return new ColorValueCache(new Color(middleVal, 0, highestVal), (int) (83 + (((double) middleVal / 255) * 20.667)), OrderType.Middle, OrderType.Dont_Care, OrderType.Highest);

            } else if (middle.type == LinkedColorValue.Type.GREEN) {
                return new ColorValueCache(new Color(0, middleVal, highestVal), (int) (83 - (((double) middleVal / 255) * 20.667)), OrderType.Dont_Care, OrderType.Middle, OrderType.Highest);
            }

        }

        return new ColorValueCache(new Color(highestVal, 0, 0), 0, OrderType.Highest, OrderType.Dont_Care, OrderType.Dont_Care);
    }

    private final Color defaultColor;
    private final int defaultBarHeight;
    private final OrderType red;
    private final OrderType green;
    private final OrderType blue;

    private ColorValueCache(Color defaultColor, int defaultBarHeight, OrderType red, OrderType green, OrderType blue) {
        this.defaultColor = defaultColor;
        this.defaultBarHeight = defaultBarHeight;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color getDefaultColor() {
        return this.defaultColor;
    }

    public Color getDefaultColorNoAlpha() {
        return new Color(this.defaultColor.getRed(), this.defaultColor.getGreen(), this.defaultColor.getBlue());
    }

    public int getDefaultBarHeight() {
        return this.defaultBarHeight;
    }

    public OrderType getRedOrderType() {
        return this.red;
    }

    public OrderType getGreenOrderType() {
        return this.green;
    }

    public OrderType getBlueOrderType() {
        return this.blue;
    }

    public enum OrderType {
        Highest,
        Middle,
        Dont_Care
    }
}
