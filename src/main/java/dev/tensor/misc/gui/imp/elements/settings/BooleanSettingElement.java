package dev.tensor.misc.gui.imp.elements.settings;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.HackElement;
import dev.tensor.misc.gui.imp.elements.KeyPressElement;
import dev.tensor.misc.gui.imp.elements.SettingElement;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 05-04-2021
 */

public final class BooleanSettingElement extends SettingElement {

    private final BooleanSetting booleanSetting;

    public BooleanSettingElement(HackElement hackElement, BooleanSetting booleanSetting, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int posX, int posY) {
        super(hackElement, x, y, scrollY, posX, posY);
        this.booleanSetting = booleanSetting;
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

        this.startFlatGL(false);
        this.prepRectangle(fillColor.getRGB());

        this.genRectangleVertices(minX + 1, minY, maxX - 1, maxY);

        this.swapRectangleColor(outLineColor.getRGB());

        this.genRectangleVertices(minX, minY, minX + 1, maxY);
        this.genRectangleVertices(maxX - 1, minY, maxX, maxY);

        this.finishRectangle();
        this.finishFlatGL(false);

        String text = this.booleanSetting.getName() + " (" + (this.booleanSetting.getValue() ? ChatColor.GREEN.text() + "True" : ChatColor.RED.text() + "False") + ChatColor.DEFAULT.text() + ")";

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

        this.booleanSetting.setValue(!this.booleanSetting.getValue());
    }
}