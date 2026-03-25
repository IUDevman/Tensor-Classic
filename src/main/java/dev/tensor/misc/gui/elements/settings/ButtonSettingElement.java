package dev.tensor.misc.gui.elements.settings;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.elements.ButtonSetting;
import dev.tensor.misc.gui.elements.HackElement;
import dev.tensor.misc.gui.elements.KeyPressElement;
import dev.tensor.misc.gui.elements.SettingElement;
import dev.tensor.misc.gui.elements.settings.buttons.StringSettingButton;
import dev.tensor.misc.imp.setting.Setting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import dev.tensor.misc.imp.setting.settings.StringSetting;

import java.awt.*;

/**
 * @author IUDevman
 * @since 05-04-2021
 */

public final class ButtonSettingElement extends SettingElement {

    private final Setting<?> setting;

    public final ButtonSetting button;

    public ButtonSettingElement(HackElement hackElement, Setting<?> setting, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int posX, int posY) {
        super(hackElement, x, y, scrollY, posX, posY);
        this.setting = setting;

        if (this.setting instanceof StringSetting) {
            this.button = new StringSettingButton(hackElement.categoryElement.tensorGUI, hackElement.categoryElement.tensorGUI.clickGUI, (StringSetting) setting);
        } else {
            button = null;
        }
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

        this.drawRectangle(minX + 1, minY, maxX - 1, maxY, fillColor.getRGB());
        this.drawRectangle(minX, minY, minX + 1, maxY, outLineColor.getRGB());
        this.drawRectangle(maxX - 1, minY, maxX, maxY, outLineColor.getRGB());

        String text = this.button == null ? "Unknown Setting" : this.getColor(ChatColor.YELLOW) + setting.getName() + "§r : (Click)";
        this.drawCenteredString(text, midX, minY + 3, Color.WHITE.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {

        if (this.button != null) {
            this.button.setActive(true);
        }

        this.getHackElement().settingElements.forEach(settingElement -> {

            if (settingElement instanceof KeyPressElement) {

                KeyPressElement element = (KeyPressElement) settingElement;

                if (element.isSearching()) {
                    element.setSearching(false);
                }
            }
        });
    }
}