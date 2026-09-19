package dev.tensor.misc.gui.imp.elements.settings;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.HackElement;
import dev.tensor.misc.gui.imp.elements.KeyPressElement;
import dev.tensor.misc.gui.imp.elements.SettingElement;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 05-04-2021
 */

public final class EnumSettingElement extends SettingElement {

    private final EnumSetting enumSetting;

    public EnumSettingElement(HackElement hackElement, EnumSetting enumSetting, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int posX, int posY) {
        super(hackElement, x, y, scrollY, posX, posY);
        this.enumSetting = enumSetting;
    }

    @Override
    public int getWidth() {
        return 204;
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

        this.startFlatGL(false);
        this.prepRectangle(fillColor.getRGB());

        this.genRectangleVertices(minX + 1, minY, maxX - 1, maxY);

        this.swapRectangleColor(outLineColor.getRGB());

        this.genRectangleVertices(minX, minY, minX + 1, maxY);
        this.genRectangleVertices(maxX - 1, minY, maxX, maxY);

        this.finishRectangle();
        this.finishFlatGL(false);

        String text = this.enumSetting.getName() + " : " + ChatColor.LIGHT_GRAY.text() + this.enumSetting.getValue().name().replace("_", " ").replace("$", this.enumSetting.getValue().name().equals("$now") ? "$" : ":");

        if (tensorGUI.clickGUI.shouldRenderOnTheLeft()) {
            this.drawString(text, minX + 3, minY + 3, Color.WHITE.getRGB());

        } else {
            this.drawCenteredString(text, midX, minY + 3, Color.WHITE.getRGB());
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.getHackElement().settingElements.forEach(settingElement -> {

            if (settingElement instanceof KeyPressElement) {

                KeyPressElement element = (KeyPressElement) settingElement;

                if (element.isSearching()) {
                    element.setSearching(false);
                }
            }
        });

        this.enumSetting.increment();
    }
}