package dev.tensor.misc.gui.elements;

import dev.tensor.misc.gui.Element;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.elements.settings.BindResetSettingElement;
import dev.tensor.misc.gui.elements.settings.BooleanSettingElement;
import dev.tensor.misc.gui.elements.settings.IntegerSettingElement;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author IUDevman
 * @since 05-03-2021
 */

public final class HackElement implements Element {

    public static final int width = 120;
    public static final int height = 20;

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

        this.setupSettingElements();
    }

    private final IntegerSetting sScrollY = new IntegerSetting("Setting Scroll Y", 0, -1000, 1000);

    private PropertyElement propertyElement;

    private ArrayList<SettingElement> settingElements = new ArrayList<>();

    private void setupSettingElements() {

        AtomicInteger settingY = new AtomicInteger(22);

        this.propertyElement = new PropertyElement(this.hack, this.x, this.y, this.sScrollY, this.posX + width + 2, settingY.get());
        settingY.getAndAdd(this.propertyElement.getHeight());

        BindResetSettingElement bindResetSettingElement = new BindResetSettingElement(this.hack, this.x, this.y, this.sScrollY, this.posX + width + 2, settingY.get());
        this.settingElements.add(bindResetSettingElement);
        settingY.getAndAdd(bindResetSettingElement.getHeight());

        this.hack.getSettings().forEach(setting -> {

            if (setting instanceof BooleanSetting) {

                BooleanSettingElement element = new BooleanSettingElement((BooleanSetting) setting, this.x, this.y, this.sScrollY, this.posX + width + 2, settingY.get());
                this.settingElements.add(element);
                settingY.getAndAdd(element.getHeight());

            } else if (setting instanceof IntegerSetting) {

                IntegerSettingElement element = new IntegerSettingElement((IntegerSetting) setting, this.x, this.y, this.sScrollY, this.posX + width + 2, settingY.get());
                this.settingElements.add(element);
                settingY.getAndAdd(element.getHeight());

            } else if (setting instanceof DoubleSetting) {

            } else if (setting instanceof EnumSetting) {

            } else if (setting instanceof ColorSetting) {

            } else if (setting instanceof StringSetting) {

            } else {
                //dummy error element
            }
        });
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
        return height;
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
        int midY = this.getPosY() + ((this.getHeight() - 10) / 2);

        this.draw2DRectangle(minX + 1, minY + 1, maxX - 1, maxY - 1, this.isViewed() ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.draw2DRectangle(minX, minY + 1, minX + 1, maxY - 1, outLineColor.getRGB());
        this.draw2DRectangle(maxX - 1, minY + 1, maxX, maxY - 1, outLineColor.getRGB());
        this.draw2DRectangle(minX, minY, maxX, minY + 1, outLineColor.getRGB());
        this.draw2DRectangle(minX, maxY - 1, maxX, maxY + 1, outLineColor.getRGB());

        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), this.hack.NAME, midX, midY, hack.isEnabled() ? enabledTextColor.getRGB() : textColor.getRGB());

        if (this.isViewed()) {
            AtomicInteger settingheight = new AtomicInteger(0);

            this.propertyElement.render(tensorGUI, fillColor, selectedFillColor, outLineColor, textColor, enabledTextColor);
            settingheight.getAndAdd(this.propertyElement.getHeight());

            this.settingElements.forEach(settingElement -> {
                settingElement.render(tensorGUI, fillColor, selectedFillColor, outLineColor, textColor, enabledTextColor);
                settingheight.getAndAdd(settingElement.getHeight());
            });

            int barMinX = this.getPosX() + this.getWidth() + 2;
            int barMinY = propertyElement.getPosY() + settingheight.get();
            int barMaxX =  this.getPosX() + this.getWidth() + SettingElement.width + 2;
            int barMaxY = propertyElement.getPosY() + tensorGUI.guiHeight.get() - tensorGUI.titleHeight - 3;

            if (barMinY >= barMaxY) {
                return;
            }

            this.draw2DRectangle(barMinX + 1, barMinY + 2, barMaxX - 1, barMaxY - 1, fillColor.getRGB());
            this.draw2DRectangle(barMinX, barMinY + 2, barMinX + 1, barMaxY - 1, outLineColor.getRGB());
            this.draw2DRectangle(barMaxX - 1, barMinY + 2, barMaxX, barMaxY - 1, outLineColor.getRGB());
            this.draw2DRectangle(barMinX, barMinY, barMaxX, barMinY + 2, outLineColor.getRGB());
            this.draw2DRectangle(barMinX, barMaxY - 1, barMaxX, barMaxY + 1, outLineColor.getRGB());
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {

    }
}
