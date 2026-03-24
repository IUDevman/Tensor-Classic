package dev.tensor.misc.gui.elements.settings;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.elements.HackElement;
import dev.tensor.misc.gui.elements.KeyPressElement;
import dev.tensor.misc.gui.elements.SettingElement;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Arrays;

/**
 * @author IUDevman
 * @since 05-04-2021
 */

public final class DoubleSettingElement extends SettingElement implements KeyPressElement {

    private final DoubleSetting doubleSetting;
    private boolean searching = false;
    private String value = "";

    public DoubleSettingElement(HackElement hackElement, DoubleSetting doubleSetting, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int posX, int posY) {
        super(hackElement, x, y, scrollY, posX, posY);
        this.doubleSetting = doubleSetting;
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

        this.draw2DRectangle(minX + 1, minY, maxX - 1, maxY, this.isSearching() ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.draw2DRectangle(minX, minY, minX + 1, maxY, outLineColor.getRGB());
        this.draw2DRectangle(maxX - 1, minY, maxX, maxY, outLineColor.getRGB());

        String text = this.doubleSetting.getName() + " (" + this.doubleSetting.getMin() + " -> " + this.doubleSetting.getMax() + ") : " + this.getColor(ChatColor.LIGHT_GRAY) + (this.isSearching() ? (this.value.equalsIgnoreCase("") ? "..." : this.value) : this.doubleSetting.getValue());
        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), text, midX, minY + 3, Color.WHITE.getRGB());
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
        if (!isSearching()) return;

        if (key == Keyboard.KEY_DELETE || key == Keyboard.KEY_ESCAPE) {
            setSearching(false);
            return;
        }

        if (key == Keyboard.KEY_RETURN) {

            if (this.value.equals("")) {
                setSearching(false);
                return;
            }

            try {
                this.doubleSetting.setValueFromString(this.value);

                this.value = "";
                setSearching(false);

            } catch (Exception ignored) {
                this.value = "";
                setSearching(false);
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
                Keyboard.KEY_PERIOD,
                Keyboard.KEY_MINUS,
                Keyboard.KEY_BACK
        };
    }
}