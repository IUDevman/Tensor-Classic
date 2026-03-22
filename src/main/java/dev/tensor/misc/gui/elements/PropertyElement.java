package dev.tensor.misc.gui.elements;

import dev.tensor.misc.gui.Element;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-21-2026
 */

public final class PropertyElement implements Element {

    private final Hack hack;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final IntegerSetting scrollY;
    private final int posX;
    private final int posY;

    public PropertyElement(Hack hack, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int posX, int posY) {
        this.hack = hack;
        this.x = x;
        this.y = y;
        this.scrollY = scrollY;
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public int getWidth() {
        return SettingElement.width;
    }

    @Override
    public int getHeight() {
        return 45;
    }

    @Override
    public int getPosX() {
        return this.x.getValue() + this.posX;
    }

    @Override
    public int getPosY() {
        return this.y.getValue() + this.scrollY.getValue() + this.posY;
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor, Color textColor, Color enabledTextColor) {
        int minX = this.getPosX();
        int minY = this.getPosY();
        int maxX = this.getPosX() + this.getWidth();
        int maxY = this.getPosY() + this.getHeight() - 1;
        int midX = this.getPosX() + (this.getWidth() / 2);

        this.draw2DRectangle(minX + 1, minY + 1, maxX - 1, maxY - 1, fillColor.getRGB());
        this.draw2DRectangle(minX, minY + 1, minX + 1, maxY - 1, outLineColor.getRGB());
        this.draw2DRectangle(maxX - 1, minY + 1, maxX, maxY - 1, outLineColor.getRGB());
        this.draw2DRectangle(minX, minY, maxX, minY + 1, outLineColor.getRGB());
        this.draw2DRectangle(minX, maxY - 1, maxX, maxY + 1, outLineColor.getRGB());

        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), "Enabled", midX, minY + 3, this.hack.isEnabled() ? Color.GREEN.getRGB() : Color.RED.getRGB());
        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), "Drawn", midX, minY + 18, this.hack.DRAWN ? Color.GREEN.getRGB() : Color.RED.getRGB());
        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), "Messages", midX, minY + 33, this.hack.MESSAGES ? Color.GREEN.getRGB() : Color.RED.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {

    }
}
