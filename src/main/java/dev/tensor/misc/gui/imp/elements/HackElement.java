package dev.tensor.misc.gui.imp.elements;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.Element;
import dev.tensor.misc.gui.imp.elements.settings.*;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 05-03-2021
 */

public final class HackElement implements Element {

    public static final int width = 120;
    public static final int height = 20;

    public final CategoryElement categoryElement;
    public final Hack hack;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final IntegerSetting scrollY;
    private final int posX;
    private final int posY;

    private boolean viewed = false;

    public final IntegerSetting sScrollY = new IntegerSetting("Setting Scroll Y", 0, -1000, 1000);

    public PropertyElement propertyElement;

    public ArrayList<SettingElement> settingElements = new ArrayList<>();

    public HackElement(CategoryElement categoryElement, Hack hack, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int posX, int posY) {
        this.categoryElement = categoryElement;
        this.hack = hack;
        this.x = x;
        this.y = y;
        this.scrollY = scrollY;
        this.posX = posX;
        this.posY = posY;

        this.setupSettingElements();
    }

    private void setupSettingElements() {

        AtomicInteger settingY = new AtomicInteger(22);

        this.propertyElement = new PropertyElement(this, this.hack, this.x, this.y, this.sScrollY, this.posX + this.getWidth() + 2, settingY.get());
        settingY.getAndAdd(this.propertyElement.getHeight());

        BindResetSettingElement bindResetSettingElement = new BindResetSettingElement(this, this.hack, this.x, this.y, this.sScrollY, this.posX + this.getWidth() + 2, settingY.get());
        this.settingElements.add(bindResetSettingElement);
        settingY.getAndAdd(bindResetSettingElement.getHeight());

        this.hack.getSettings().forEach(setting -> {

            if (setting instanceof BooleanSetting) {

                BooleanSettingElement element = new BooleanSettingElement(this, (BooleanSetting) setting, this.x, this.y, this.sScrollY, this.posX + this.getWidth() + 2, settingY.get());
                this.settingElements.add(element);
                settingY.getAndAdd(element.getHeight());

            } else if (setting instanceof IntegerSetting) {

                IntegerSettingElement element = new IntegerSettingElement(this, (IntegerSetting) setting, this.x, this.y, this.sScrollY, this.posX + this.getWidth() + 2, settingY.get());
                this.settingElements.add(element);
                settingY.getAndAdd(element.getHeight());

            } else if (setting instanceof DoubleSetting) {

                DoubleSettingElement element = new DoubleSettingElement(this, (DoubleSetting) setting, this.x, this.y, this.sScrollY, this.posX + this.getWidth() + 2, settingY.get());
                this.settingElements.add(element);
                settingY.getAndAdd(element.getHeight());

            } else if (setting instanceof EnumSetting) {

                EnumSettingElement element = new EnumSettingElement(this, (EnumSetting) setting, this.x, this.y, this.sScrollY, this.posX + this.getWidth() + 2, settingY.get());
                this.settingElements.add(element);
                settingY.getAndAdd(element.getHeight());

            } else if (setting instanceof ColorSetting) {

                ColorSettingElement element = new ColorSettingElement(this, (ColorSetting) setting, this.x, this.y, this.sScrollY, this.posX + this.getWidth() + 2, settingY.get());
                this.settingElements.add(element);
                settingY.getAndAdd(element.getHeight());

            } else {
                ButtonSettingElement element = new ButtonSettingElement(this, setting, this.x, this.y, this.sScrollY, this.posX + this.getWidth() + 2, settingY.get());
                this.settingElements.add(element);
                settingY.getAndAdd(element.getHeight());
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
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        int minX = this.getPosX();
        int minY = this.getPosY();
        int maxX = this.getPosX() + this.getWidth();
        int maxY = this.getPosY() + this.getHeight() - 1;
        int midX = this.getPosX() + (this.getWidth() / 2);
        int midY = this.getPosY() + ((this.getHeight() - 10) / 2);

        this.drawRectangle(minX + 1, minY + 1, maxX - 1, maxY - 1, this.isViewed() ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.drawRectangle(minX, minY + 1, minX + 1, maxY - 1, outLineColor.getRGB());
        this.drawRectangle(maxX - 1, minY + 1, maxX, maxY - 1, outLineColor.getRGB());
        this.drawRectangle(minX, minY, maxX, minY + 1, outLineColor.getRGB());
        this.drawRectangle(minX, maxY - 1, maxX, maxY + 1, outLineColor.getRGB());

        this.drawCenteredString(this.hack.NAME, midX, midY, hack.isEnabled() ? Color.YELLOW.getRGB() : Color.WHITE.getRGB());

        if (this.isViewed()) {
            AtomicInteger settingheight = new AtomicInteger(0);

            this.propertyElement.render(tensorGUI, fillColor, selectedFillColor, outLineColor);
            settingheight.getAndAdd(this.propertyElement.getHeight());

            this.settingElements.forEach(settingElement -> {
                settingElement.render(tensorGUI, fillColor, selectedFillColor, outLineColor);
                settingheight.getAndAdd(settingElement.getHeight());
            });

            int barMinX = this.getPosX() + this.getWidth() + 2;
            int barMinY = propertyElement.getPosY() + settingheight.get();
            int barMaxX = this.getPosX() + this.getWidth() + SettingElement.width + 2;
            int barMaxY = propertyElement.getPosY() + tensorGUI.guiHeight.get() - tensorGUI.titleBoxHeight - 3;

            if (barMinY >= barMaxY) {
                return;
            }

            this.drawRectangle(barMinX + 1, barMinY, barMaxX - 1, barMaxY - 1, fillColor.getRGB());
            this.drawRectangle(barMinX, barMinY + 4, barMinX + 1, barMaxY - 1, outLineColor.getRGB());
            this.drawRectangle(barMaxX - 1, barMinY + 4, barMaxX, barMaxY - 1, outLineColor.getRGB());
            this.drawRectangle(barMinX, barMinY, barMinX + 1, barMinY + 4, outLineColor.getRGB());
            this.drawRectangle(barMaxX - 1, barMinY, barMaxX, barMinY + 4, outLineColor.getRGB());
            this.drawRectangle(barMinX, barMaxY - 1, barMaxX, barMaxY + 1, outLineColor.getRGB());
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setViewed(true);

        this.categoryElement.hackElements.forEach(hackElement -> {

            if (hackElement != this && hackElement.isViewed()) {
                hackElement.setViewed(false);

                hackElement.sScrollY.setValue(0);

                hackElement.settingElements.forEach(settingElement -> {

                    if (settingElement instanceof KeyPressElement) {
                        KeyPressElement element = (KeyPressElement) settingElement;
                        element.setSearching(false);
                    }
                });
            }
        });
    }
}
