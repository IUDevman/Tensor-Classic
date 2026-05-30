package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.color;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.settings.buttons.ColorSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.imp.render.LinkedColorValue;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Hoosiers
 * @since 05-27-2026
 */

public final class SmallColorPickerObject implements ButtonSettingObject {

    private final ColorSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    public Color defaultColor = Color.RED;
    private int defaultBarHeight = 0;

    public SmallColorPickerObject(ColorSettingButton button, IntegerSetting x, IntegerSetting y, int offsetX, int offsetY, int width, int height) {
        this.button = button;
        this.x = x;
        this.y = y;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;

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
        this.updateDefaultColor();

        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight() - 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + 2, this.getPosX() + 2, this.getPosY() + this.getHeight() - 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 2, this.getPosY() + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 2, outLineColor.getRGB());

        this.drawSmallColorPickerRectangle(this.getPosX() + 2, this.getPosY() + 2, 28, 124);

        int barY = this.defaultBarHeight;

        if (barY > 122) {
            barY = 122;
        }

        this.drawRectangle(this.getPosX() + 2, this.getPosY() + 2 + barY, this.getPosX() + this.getWidth() - 2, this.getPosY() + 2 + barY + 2, Color.DARK_GRAY.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        //todo: dragging
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

    private void updateDefaultColor() {
        Color color = this.button.colorSettingElement.colorSetting.getValue();

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
            this.defaultColor = new Color(highestVal, 0, 0);
            this.defaultBarHeight = 0;
            return;
        }

        if (highest.type == LinkedColorValue.Type.RED) {
            if (middle.toTestFloat() == 0.0F) {
                this.defaultColor = new Color(highestVal, 0, 0);

                if (this.defaultBarHeight >= 103) {
                    this.defaultBarHeight = 124;
                } else {
                    this.defaultBarHeight = 0;
                }

            } else if (middle.toTestFloat() == lowest.toTestFloat()) {
                this.defaultColor = new Color(highestVal, 0, 0);

                if (this.defaultBarHeight >= 103) {
                    this.defaultBarHeight = 124;
                } else {
                    this.defaultBarHeight = 0;
                }

            } else if (middle.type == LinkedColorValue.Type.GREEN) {
                this.defaultColor = new Color(highestVal, middleVal, 0);
                this.defaultBarHeight = (int) (((double) middleVal / 255) * 20.667);

            } else if (middle.type == LinkedColorValue.Type.BLUE) {
                this.defaultColor = new Color(highestVal, 0, middleVal);
                this.defaultBarHeight = 124 - (int) (((double) middleVal / 255) * 20.667);
            }

        } else if (highest.type == LinkedColorValue.Type.GREEN) {
            if (middle.toTestFloat() == 0.0F) {
                this.defaultColor = new Color(0, highestVal, 0);
                this.defaultBarHeight = 41;

            } else if (middle.toTestFloat() == lowest.toTestFloat()) {
                this.defaultColor = new Color(0, highestVal, 0);
                this.defaultBarHeight = 41;

            } else if (middle.type == LinkedColorValue.Type.RED) {
                this.defaultColor = new Color(middleVal, highestVal, 0);
                this.defaultBarHeight = (int) (41 - (((double) middleVal / 255) * 20.667));

            } else if (middle.type == LinkedColorValue.Type.BLUE) {
                this.defaultColor = new Color(0, highestVal, middleVal);
                this.defaultBarHeight = (int) (41 + (((double) middleVal / 255) * 20.667));
            }

        } else if (highest.type == LinkedColorValue.Type.BLUE) {
            if (middle.toTestFloat() == 0.0F) {
                this.defaultColor = new Color(0, 0, highestVal);
                this.defaultBarHeight = 83;

            } else if (middle.toTestFloat() == lowest.toTestFloat()) {
                this.defaultColor = new Color(0, 0, highestVal);
                this.defaultBarHeight = 83;

            } else if (middle.type == LinkedColorValue.Type.RED) {
                this.defaultColor = new Color(middleVal, 0, highestVal);
                this.defaultBarHeight = (int) (83 + (((double) middleVal / 255) * 20.667));

            } else if (middle.type == LinkedColorValue.Type.GREEN) {
                this.defaultColor = new Color(0, middleVal, highestVal);
                this.defaultBarHeight = (int) (83 - (((double) middleVal / 255) * 20.667));
            }

        } else {
            this.defaultColor = new Color(highestVal, 0, 0);
            this.defaultBarHeight = 0;
        }
    }

    /*
    //Jank ?!?!
    public void loadSmallColorPickerWithColor(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        if (r > g && r > b) {
            if (g > b) {
                this.setColor(new Color(255, 255, 0));

            } else if (b > g)  {
                this.setColor(new Color(255, 0, 255));

            } else {
                this.setColor(new Color(255, 0, 0));
            }

        } else if (g > r && g > b) {
            if (r > b) {
                this.setColor(new Color(255, 255, 0));

            } else if (b > r) {
                this.setColor(new Color(0, 255, 255));

            } else {
                this.setColor(new Color(0, 255, 0));
            }

        } else if (b > r && b > g) {
            if (r > g) {
                this.setColor(new Color(255, 0, 255));

            } else if (g > r) {
                this.setColor(new Color(0, 255, 255));

            } else {
                this.setColor(new Color(0, 0, 255));
            }

        } else {
            if (r == g) {
                this.defaultColor = new Color(255, 255, 0);

            } else if (r == b) {
                this.defaultColor = new Color(255, 0, 255);

            } else {
                this.setColor(new Color(255, 0, 0));
            }
        }
    }

    private void setColor(Color color) {
        this.defaultColor = color;
        this.oldDefaultColor = color;
    }

     */
}
