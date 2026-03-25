package dev.tensor.misc.gui.elements.settings;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.elements.HackElement;
import dev.tensor.misc.gui.elements.KeyPressElement;
import dev.tensor.misc.gui.elements.SettingElement;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.Setting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author IUDevman
 * @since 05-11-2021
 */

public final class BindResetSettingElement extends SettingElement implements KeyPressElement {

    private final Hack hack;
    private boolean searching = false;

    public BindResetSettingElement(HackElement hackElement, Hack hack, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int posX, int posY) {
        super(hackElement, x, y, scrollY, posX, posY);

        this.hack = hack;
    }

    @Override
    public boolean isSearching() {
        return this.searching;
    }

    @Override
    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    @Override
    public int getWidth() {
        return SettingElement.width;
    }

    @Override
    public int getHeight() {
        return 30;
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
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        int minX = this.getPosX();
        int minY = this.getPosY();
        int maxX = this.getPosX() + this.getWidth();
        int maxY = this.getPosY() + this.getHeight();
        int midX = this.getPosX() + (this.getWidth() / 2);

        this.drawRectangle(minX + 1, minY + 1, maxX - 1, maxY, fillColor.getRGB());
        this.drawRectangle(minX, minY + 1, minX + 1, maxY, outLineColor.getRGB());
        this.drawRectangle(maxX - 1, minY + 1, maxX, maxY, outLineColor.getRGB());
        this.drawRectangle(minX, minY, maxX, minY + 1, outLineColor.getRGB());

        String text = "Keybind : " + this.getColor(ChatColor.LIGHT_GRAY) + (this.isSearching() ? "[...]" : "[" + Keyboard.getKeyName(hack.BIND) + "]");
        this.drawCenteredString(text, midX, minY + 3, Color.YELLOW.getRGB());

        this.drawCenteredString(this.getColor(ChatColor.LIGHT_GRAY) + "Reset", midX, minY + 18, Color.WHITE.getRGB());
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

        if (mouseY > this.getPosY() + 15) {
            if (isSearching()) setSearching(false);

            this.hack.reset();

            this.hack.getSettings().forEach(Setting::reset);
            return;
        }

        setSearching(!isSearching());
    }

    @Override
    public void onKeyPressed(char letter, int key) {
        if (!isSearching()) return;

        if (key == Keyboard.KEY_DELETE) {
            this.hack.BIND = Keyboard.KEY_NONE;
            return;
        }

        if (key != Keyboard.KEY_ESCAPE && key != Keyboard.KEY_RETURN) {
            this.hack.BIND = key;
        }

        setSearching(false);
    }
}