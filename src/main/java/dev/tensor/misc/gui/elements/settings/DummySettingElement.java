package dev.tensor.misc.gui.elements.settings;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.elements.SettingElement;
import dev.tensor.misc.imp.setting.Setting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author IUDevman
 * @since 05-04-2021
 */

public final class DummySettingElement extends SettingElement {

    private final Setting<?> setting;

    public DummySettingElement(Setting<?> setting, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int posX, int posY) {
        super(x, y, scrollY, posX, posY);
        this.setting = setting;
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
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor, Color textColor, Color enabledTextColor) {
        int minX = this.getPosX();
        int minY = this.getPosY();
        int maxX = this.getPosX() + this.getWidth();
        int maxY = this.getPosY() + this.getHeight();
        int midX = this.getPosX() + (this.getWidth() / 2);

        this.draw2DRectangle(minX + 1, minY, maxX - 1, maxY, fillColor.getRGB());
        this.draw2DRectangle(minX, minY, minX + 1, maxY, outLineColor.getRGB());
        this.draw2DRectangle(maxX - 1, minY, maxX, maxY, outLineColor.getRGB());

        //String text = this.booleanSetting.getName() + " (" + (this.booleanSetting.getValue() ? this.getColor(ChatColor.GREEN) + "True" : this.getColor(ChatColor.RED) + "False") + this.getColor(ChatColor.DEFAULT) + ")";
        String text = "Dummy:  " + this.getColor(ChatColor.YELLOW) + setting.getClass().getSimpleName();
        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), text, midX, minY + 3, textColor.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {

    }
}