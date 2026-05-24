package dev.tensor.misc.gui.imp.elements.settings;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.ButtonSetting;
import dev.tensor.misc.gui.imp.elements.HackElement;
import dev.tensor.misc.gui.imp.elements.KeyPressElement;
import dev.tensor.misc.gui.imp.elements.SettingElement;
import dev.tensor.misc.gui.imp.elements.settings.buttons.*;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.setting.Setting;
import dev.tensor.misc.imp.setting.settings.*;

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
        } else if (this.setting instanceof BlockListSetting) {
            this.button = new BlockListSettingButton(hackElement.categoryElement.tensorGUI, hackElement.categoryElement.tensorGUI.clickGUI, hackElement, (BlockListSetting) setting);
        } else if (this.setting instanceof EntityTypeListSetting) {
            this.button = new EntityTypeListSettingButton(hackElement.categoryElement.tensorGUI, hackElement.categoryElement.tensorGUI.clickGUI, hackElement, (EntityTypeListSetting) setting);
        } else if (this.setting instanceof WaypointListSetting) {
            this.button = new WaypointListSettingButton(hackElement.categoryElement.tensorGUI, hackElement.categoryElement.tensorGUI.clickGUI, hackElement, (WaypointListSetting) setting);
        } else if (this.setting instanceof GeneralButtonSetting) {
            GeneralButtonSetting generalButtonSetting = ((GeneralButtonSetting) this.setting);
            GeneralButtonSetting.ButtonType type = generalButtonSetting.getValue();

            if (type.equals(GeneralButtonSetting.ButtonType.ACCOUNTS)) {
                this.button = new AccountListSettingButton(hackElement.categoryElement.tensorGUI, hackElement.categoryElement.tensorGUI.clickGUI, hackElement, generalButtonSetting);

            } else if (type.equals(GeneralButtonSetting.ButtonType.CLICK_ACTION)) {
                this.button = new ActionSettingButton(generalButtonSetting);

            } else {
                this.button = new DummySettingButton();
            }

        } else {
            this.button = new DummySettingButton();
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

        String click = "Click";

        if (this.button instanceof DummySettingButton) {
            click = "N/A";
        }

        String text = ChatColor.YELLOW.text() + setting.getName() + "§r : (" + click + ")";
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