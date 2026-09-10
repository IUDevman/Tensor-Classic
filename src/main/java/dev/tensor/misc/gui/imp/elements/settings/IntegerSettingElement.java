package dev.tensor.misc.gui.imp.elements.settings;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.HackElement;
import dev.tensor.misc.gui.imp.elements.KeyPressElement;
import dev.tensor.misc.gui.imp.elements.SettingElement;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Arrays;

/**
 * @author Hoosiers
 * @since 05-04-2021
 */

public final class IntegerSettingElement extends SettingElement implements KeyPressElement {

    private final IntegerSetting integerSetting;
    private boolean searching = false;
    private String value = "";

    public IntegerSettingElement(HackElement hackElement, IntegerSetting integerSetting, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int posX, int posY) {
        super(hackElement, x, y, scrollY, posX, posY);
        this.integerSetting = integerSetting;
    }

    public boolean isSearching() {
        return this.searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
        this.value = "";
    }

    @Override
    public int getWidth() {
        return SettingElement.width;
    }

    @Override
    public int getHeight() {
        return 15;
    }

    @Override
    public int getPosX() {
        return getX().getValue().intValue() + getXPos();
    }

    @Override
    public int getPosY() {
        return getY().getValue().intValue() + getScrollY().getValue().intValue() + getYPos();
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        int minX = this.getPosX();
        int minY = this.getPosY();
        int maxX = this.getPosX() + this.getWidth();
        int maxY = this.getPosY() + this.getHeight();
        int midX = this.getPosX() + (this.getWidth() / 2);

        this.startFlatGL(false);
        this.prepRectangle(this.isSearching() ? selectedFillColor.getRGB() : fillColor.getRGB());

        this.genRectangleVertices(minX + 1, minY, maxX - 1, maxY);

        this.swapRectangleColor(outLineColor.getRGB());

        this.genRectangleVertices(minX, minY, minX + 1, maxY);
        this.genRectangleVertices(maxX - 1, minY, maxX, maxY);

        this.finishRectangle();
        this.finishFlatGL(false);

        String text = this.integerSetting.getName() + " (" + this.integerSetting.getMin() + " -> " + this.integerSetting.getMax() + ") : " + ChatColor.LIGHT_GRAY.text() + (this.isSearching() ? (this.value.equalsIgnoreCase("") ? "..." : this.value) : this.integerSetting.getValue());
        this.drawCenteredString(text, midX, minY + 3, Color.WHITE.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.getHackElement().settingElements.forEach(settingElement -> {

            if (settingElement != this && settingElement instanceof KeyPressElement) {

                KeyPressElement element = (KeyPressElement) settingElement;

                if (element.isSearching()) {
                    element.setSearching(false);
                }
            }
        });

        setSearching(!isSearching());
    }

    @Override
    public void onKeyPressed(char letter, int key) {
        if (!this.isSearching()) {
            return;
        }

        if (key == Keyboard.KEY_DELETE || key == Keyboard.KEY_ESCAPE) {
            this.setSearching(false);
            return;
        }

        if (key == Keyboard.KEY_RETURN) {

            if (this.value.equals("")) {
                this.setSearching(false);
                return;
            }

            try {
                this.integerSetting.setValueFromString(this.value);

                this.value = "";
                this.setSearching(false);

            } catch (Exception ignored) {
                this.value = "";
                this.setSearching(false);
                return;
            }
        }

        if (Arrays.stream(this.acceptedKeys()).noneMatch(value -> value == key)) {
            return;
        }

        if (key == Keyboard.KEY_BACK) {
            if (this.value.length() < 1) return;
            this.value = this.value.substring(0, this.value.length() - 1);
            return;
        }

        if (this.value.length() >= this.integerSetting.getMax().toString().length()) {
            return;
        }

        String name = Keyboard.getKeyName(key);

        if (name.equalsIgnoreCase("PERIOD")) {
            name = ".";
        } else if (name.equalsIgnoreCase("MINUS")) {
            name = "-";
        }

        this.value += name;
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
                Keyboard.KEY_MINUS,
                Keyboard.KEY_BACK
        };
    }
}