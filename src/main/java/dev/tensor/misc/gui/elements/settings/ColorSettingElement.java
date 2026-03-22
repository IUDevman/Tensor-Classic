package dev.tensor.misc.gui.elements.settings;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.elements.SettingElement;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Arrays;

/**
 * @author IUDevman
 * @since 05-04-2021
 */

public final class ColorSettingElement extends SettingElement {

    private final ColorSetting colorSetting;
    private boolean searchingR = false;
    private boolean searchingG = false;
    private boolean searchingB = false;
    private String value = "";

    public ColorSettingElement(ColorSetting colorSetting, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int posX, int posY) {
        super(x, y, scrollY, posX, posY);
        this.colorSetting = colorSetting;
    }

    public void clearSearching() {
        this.searchingR = false;
        this.searchingG = false;
        this.searchingB = false;
        this.value = "";
    }

    @Override
    public int getWidth() {
        return 204;
    }

    @Override
    public int getHeight() {
        return 60;
    }

    @Override
    public int getPosX() {
        return this.getX().getValue() + this.getXPos();
    }

    @Override
    public int getPosY() {
        return this.getY().getValue() + this.getScrollY().getValue() + this.getYPos();
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor, Color textColor, Color enabledTextColor) {
        int minX = this.getPosX();
        int minY = this.getPosY();
        int maxX = this.getPosX() + this.getWidth();
        int maxY = this.getPosY() + this.getHeight();
        int midX = this.getPosX() + (this.getWidth() / 2);

        this.draw2DRectangle(minX + 1, minY, maxX - 1, maxY - 45, fillColor.getRGB());
        this.draw2DRectangle(minX + 1, minY + 15, maxX - 1, maxY - 30, this.searchingR ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.draw2DRectangle(minX + 1, minY + 30, maxX - 1, maxY - 15, this.searchingG ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.draw2DRectangle(minX + 1, minY + 45, maxX - 1, maxY, this.searchingB ? selectedFillColor.getRGB() : fillColor.getRGB());

        this.draw2DRectangle(minX, minY, minX + 1, maxY, outLineColor.getRGB());
        this.draw2DRectangle(maxX - 1, minY, maxX, maxY, outLineColor.getRGB());

        String name = this.colorSetting.getName() + " : ";
        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), name, midX + 3 - (this.getFontRenderer().getStringWidth(name) / 2), minY + 3, textColor.getRGB());

        String rgb = "(" + this.colorSetting.getValue().getRed() + ", " + this.colorSetting.getValue().getGreen() + ", " + this.colorSetting.getValue().getBlue() + ")";
        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), rgb, midX + 3 + (this.getFontRenderer().getStringWidth(name) / 2), minY + 3, this.colorSetting.getValue().getRGB());

        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), "  > Red (0 -> 255) : " + this.getColor(ChatColor.LIGHT_GRAY) + (this.searchingR ? (this.value.equalsIgnoreCase("") ? "[...]" : this.value) : "[" + this.colorSetting.getValue().getRed() + "]"), midX + 3, minY + 18, textColor.getRGB());
        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), "  > Green (0 -> 255) : " + this.getColor(ChatColor.LIGHT_GRAY) + (this.searchingG ? (this.value.equalsIgnoreCase("") ? "[...]" : this.value) : "[" + this.colorSetting.getValue().getGreen() + "]"), midX + 3, minY + 33, textColor.getRGB());
        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), "  > Blue (0 -> 255) : " + this.getColor(ChatColor.LIGHT_GRAY) + (this.searchingB ? (this.value.equalsIgnoreCase("") ? "[...]" : this.value) : "[" + this.colorSetting.getValue().getBlue() + "]"), midX + 3, minY + 48, textColor.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (mouseY > getPosY() + 15 && mouseY <= getPosY() + 30) {
            this.searchingR = !this.searchingR;
            this.searchingG = false;
            this.searchingB = false;
        } else if (mouseY > getPosY() + 30 && mouseY <= getPosY() + 45) {
            this.searchingR = false;
            this.searchingG = !this.searchingG;
            this.searchingB = false;
        } else if (mouseY > getPosY() + 45 && mouseY <= getPosY() + 60) {
            this.searchingR = false;
            this.searchingG = false;
            this.searchingB = !this.searchingB;
        } else {
            clearSearching();
        }
    }

    public void onKeyPressed(int key) {
        if (!this.searchingR && !this.searchingG && !this.searchingB) return;

        if (key == Keyboard.KEY_DELETE || key == Keyboard.KEY_ESCAPE) {
            clearSearching();
            return;
        }

        if (key == Keyboard.KEY_END) {

            if (this.value.equals("")) {
                clearSearching();
                return;
            }

            int value1 = Integer.parseInt(this.value);

            if (value1 > 255) value1 = 255;
            else if (value1 < 0) value1 = 0;

            this.setValue(value1);

            this.value = "";
            clearSearching();
        }

        if (Arrays.stream(this.acceptedKeys()).noneMatch(value -> value == key)) {
            return;
        }

        if (key == Keyboard.KEY_BACK) {
            if (this.value.length() < 1) return;
            this.value = this.value.substring(0, this.value.length() - 1);
            return;
        }

        this.value += Keyboard.getKeyName(key);
    }

    private void setValue(int value) {
        Color oldColor = this.colorSetting.getValue();

        if (this.searchingR) {
            this.colorSetting.setValue(new Color(value, oldColor.getGreen(), oldColor.getBlue(), oldColor.getAlpha()));
        } else if (this.searchingG) {
            this.colorSetting.setValue(new Color(oldColor.getRed(), value, oldColor.getBlue(), oldColor.getAlpha()));
        } else if (this.searchingB) {
            this.colorSetting.setValue(new Color(oldColor.getRed(), oldColor.getGreen(), value, oldColor.getAlpha()));
        }
    }

    private int[] acceptedKeys() {
        return new int[]{
                Keyboard.KEY_0,
                Keyboard.KEY_1,
                Keyboard.KEY_2,
                Keyboard.KEY_3,
                Keyboard.KEY_4,
                Keyboard.KEY_5,
                Keyboard.KEY_6,
                Keyboard.KEY_7,
                Keyboard.KEY_8,
                Keyboard.KEY_9,
                Keyboard.KEY_BACK
        };
    }
}