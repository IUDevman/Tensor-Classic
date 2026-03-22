package dev.tensor.misc.gui.elements;

import dev.tensor.feature.hacks.KillAura;
import dev.tensor.misc.gui.Element;
import dev.tensor.misc.gui.TensorGUI;
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

    public static final int width = 60;

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

        this.getHackManager().getHacksInCategory(this.category).forEach(hack -> {

            HackElement hackElement = new HackElement(hack, this.x, this.y, this.mScrollY, this.getWidth() + 2, offsetY.get());
            this.hackElements.add(hackElement);
            offsetY.getAndAdd(hackElement.getHeight());

            //todo: change this back
            if (hack instanceof KillAura) {
                hackElement.setViewed(true);
            }
        });
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
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor, Color textColor, Color enabledTextColor) {
        int minX = this.getPosX();
        int minY = this.getPosY();
        int maxX = this.getPosX() + this.getWidth();
        int maxY = this.getPosY() + this.getHeight() - 1;
        int midX = this.getPosX() + (this.getWidth() / 2);
        int midY = this.getPosY() + ((this.getHeight() - 10) / 2);

        this.draw2DRectangle(minX + 1, minY + 1, maxX - 1, maxY - 1, fillColor.getRGB());
        this.draw2DRectangle(minX, minY + 1, minX + 1, maxY - 1, outLineColor.getRGB());
        this.draw2DRectangle(maxX - 1, minY + 1, maxX, maxY - 1, outLineColor.getRGB());
        this.draw2DRectangle(minX, minY, maxX, minY + 1, outLineColor.getRGB());
        this.draw2DRectangle(minX, maxY - 1, maxX, maxY + 1, outLineColor.getRGB());

        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), this.category.name(), midX, midY, this.isSelected() ? enabledTextColor.getRGB() : textColor.getRGB());

        if (this.isSelected()) {
            this.hackElements.forEach(hackElement -> hackElement.render(tensorGUI, fillColor, selectedFillColor, outLineColor, textColor, enabledTextColor));

            int barMinX = this.getPosX() + this.getWidth() + 2;
            int barMinY = this.getPosY() + (this.hackElements.size() * HackElement.height) + tensorGUI.titleHeight + 2;
            int barMaxX = this.getPosX() + this.getWidth() + HackElement.width + 2;
            int barMaxY = this.getPosY() + tensorGUI.guiHeight.get() - 1;

            if (barMinY >= barMaxY) {
                return;
            }

            this.draw2DRectangle(barMinX + 1, barMinY + 1, barMaxX - 1, barMaxY - 1, fillColor.getRGB());
            this.draw2DRectangle(barMinX, barMinY + 1, barMinX + 1, barMaxY - 1, outLineColor.getRGB());
            this.draw2DRectangle(barMaxX - 1, barMinY + 1, barMaxX, barMaxY - 1, outLineColor.getRGB());
            this.draw2DRectangle(barMinX, barMinY, barMaxX, barMinY + 1, outLineColor.getRGB());
            this.draw2DRectangle(barMinX, barMaxY - 1, barMaxX, barMaxY + 1, outLineColor.getRGB());
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setSelected(true);
    }
}
