package dev.tensor.misc.gui.elements;

import dev.tensor.misc.gui.Element;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author IUDevman
 * @since 05-03-2021
 */

public final class HackElement implements Element {

    public static int width = 120;

    private final Hack hack;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final IntegerSetting scrollY;
    private final int posX;
    private final int posY;

    private boolean viewed = false;

    public HackElement(Hack hack, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int posX, int posY) {
        this.hack = hack;
        this.x = x;
        this.y = y;
        this.scrollY = scrollY;
        this.posX = posX;
        this.posY = posY;
    }

    public Hack getHack() {
        return this.hack;
    }

    public String getName() {
        return this.getHack().NAME;
    }

    public boolean isViewed() {
        return this.viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return 20;
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
    public void render(Color fillColor, Color selectedFillColor, Color outLineColor, Color textColor, Color selectedTextColor) {
        this.draw2DRectangle(this.getPosX() + 1, this.getPosY() + 1, this.getPosX() + this.getWidth() - 1, this.getPosY() + this.getHeight() - 1, this.isViewed() ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.draw2DRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + 1, outLineColor.getRGB());
        this.draw2DRectangle(this.getPosX(), this.getPosY() + this.getHeight() - 1, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), outLineColor.getRGB());
        this.draw2DRectangle(this.getPosX(), this.getPosY() + 1, this.getPosX() + 1, this.getPosY() + this.getHeight() - 1, outLineColor.getRGB());
        this.draw2DRectangle(this.getPosX() + this.getWidth() - 1, this.getPosY() + 1, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 1, outLineColor.getRGB());

        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), this.getName(), this.getPosX() + (this.getWidth() / 2), this.getPosY() + ((this.getHeight() - 10) / 2), hack.isEnabled() ? selectedTextColor.getRGB() : textColor.getRGB());

        if (this.isViewed()) {

        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {

    }
}
