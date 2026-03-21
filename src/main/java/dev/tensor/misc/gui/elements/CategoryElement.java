package dev.tensor.misc.gui.elements;

import dev.tensor.misc.gui.Element;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author IUDevman
 * @since 05-03-2021
 */

public final class CategoryElement implements Element {

    public static int width = 60;

    private final Category category;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final int posX;
    private final int posY;

    private boolean selected = false;

    private final ArrayList<HackElement> hackElements = new ArrayList<>();

    public CategoryElement(Category category, IntegerSetting x, IntegerSetting y, int posX, int posY) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.posX = posX;
        this.posY = posY;

        this.setupHackElements();
    }

    private final IntegerSetting mScrollY = new IntegerSetting("Module Scroll Y", 0, -1000, 1000);

    private void setupHackElements() {
        this.hackElements.clear();

        AtomicInteger offsetY = new AtomicInteger(22);

        this.getHackManager().getHacksInCategory(this.getCategory()).forEach(hack -> {

            HackElement hackElement = new HackElement(hack, this.x, this.y, this.mScrollY, this.getWidth() + 2, offsetY.get());
            this.hackElements.add(hackElement);
            offsetY.getAndAdd(hackElement.getHeight());

            if (this.hackElements.size() == 1) {
                hackElement.setViewed(true);
            }
        });
    }

    public Category getCategory() {
        return this.category;
    }

    public String getName() {
        return this.getCategory().name();
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return 40;
    }

    @Override
    public int getPosX() {
        return this.x.getValue() + this.posX;
    }

    @Override
    public int getPosY() {
        return this.y.getValue() + this.posY;
    }

    @Override
    public void render(Color fillColor, Color selectedFillColor, Color outLineColor, Color textColor, Color selectedTextColor) {
        this.draw2DRectangle(this.getPosX()+ 1, this.getPosY() + 1, this.getPosX() + this.getWidth() - 1, this.getPosY() + this.getHeight() - 1, this.isSelected() ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.draw2DRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + 1, outLineColor.getRGB());
        this.draw2DRectangle(this.getPosX(), this.getPosY() + this.getHeight() - 1, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), outLineColor.getRGB());
        this.draw2DRectangle(this.getPosX(), this.getPosY() + 1, this.getPosX() + 1, this.getPosY() + this.getHeight() - 1, outLineColor.getRGB());
        this.draw2DRectangle(this.getPosX() + this.getWidth() - 1, this.getPosY() + 1, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 1, outLineColor.getRGB());

        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), this.getName(), this.getPosX() + (this.getWidth() / 2), this.getPosY() + ((this.getHeight() - 10) / 2), this.isSelected() ? selectedTextColor.getRGB() : textColor.getRGB());

        if (this.isSelected()) {
            this.hackElements.forEach(hackElement -> hackElement.render(fillColor, selectedFillColor, outLineColor, textColor, selectedTextColor));
            //render clear box (y - hackelements height)
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setSelected(true);
    }
}
