package dev.tensor.misc.gui.elements;

import dev.tensor.misc.gui.Element;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author IUDevman
 * @since 05-03-2021
 */

public final class CategoryElement implements Element {

    public static final int width = 60;

    public final TensorGUI tensorGUI;
    private final Category category;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final int posX;
    private final int posY;

    private boolean selected = false;

    public final ArrayList<HackElement> hackElements = new ArrayList<>();

    public final IntegerSetting mScrollY = new IntegerSetting("Module Scroll Y", 0, -1000, 1000);

    public CategoryElement(TensorGUI tensorGUI, Category category, IntegerSetting x, IntegerSetting y, int posX, int posY) {
        this.tensorGUI = tensorGUI;
        this.category = category;
        this.x = x;
        this.y = y;
        this.posX = posX;
        this.posY = posY;

        this.setupHackElements();
    }

    private void setupHackElements() {
        this.hackElements.clear();

        AtomicInteger offsetY = new AtomicInteger(22);

        this.getHackManager().getHacksInCategory(this.category).forEach(hack -> {

            HackElement hackElement = new HackElement(this, hack, this.x, this.y, this.mScrollY, this.getWidth() + 2, offsetY.get());
            this.hackElements.add(hackElement);
            offsetY.getAndAdd(hackElement.getHeight());

            if (this.hackElements.size() == 1) {
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

        this.draw2DRectangle(minX + 1, minY + 1, maxX - 1, maxY - 1, this.isSelected() ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.draw2DRectangle(minX, minY + 1, minX + 1, maxY - 1, outLineColor.getRGB());
        this.draw2DRectangle(maxX - 1, minY + 1, maxX, maxY - 1, outLineColor.getRGB());
        this.draw2DRectangle(minX, minY, maxX, minY + 1, outLineColor.getRGB());
        this.draw2DRectangle(minX, maxY - 1, maxX, maxY + 1, outLineColor.getRGB());

        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), this.category.name(), midX, midY, this.isSelected() ? enabledTextColor.getRGB() : textColor.getRGB());

        if (this.isSelected()) {

            GL11.glEnable(GL11.GL_SCISSOR_TEST);

            int posX = this.x.getValue();
            int posY = this.y.getValue() + tensorGUI.titleHeight + 2;
            int width1 = tensorGUI.guiWidth + 1;
            int height1 = tensorGUI.guiHeight.get() - tensorGUI.titleHeight - 4;

            int windowHeight = this.getMinecraft().displayHeight;
            double scaleX = (double) this.getMinecraft().displayWidth / (double) this.getMinecraft().currentScreen.width;
            double scaleY = (double) this.getMinecraft().displayHeight / (double) this.getMinecraft().currentScreen.height;
            int widthScaled = (int) (scaleX * width1);
            int heightScaled = (int) (scaleY * height1);
            int xScaled = (int) (posX * scaleX);
            int yScaled = (int) (posY * scaleY);

            GL11.glScissor(xScaled, windowHeight - yScaled - heightScaled, widthScaled, heightScaled);

            this.hackElements.forEach(hackElement -> hackElement.render(tensorGUI, fillColor, selectedFillColor, outLineColor, textColor, enabledTextColor));

            if (this.hackElements.isEmpty()) {
                int barMinXX = this.x.getValue() + this.getWidth() + HackElement.width + 4;
                int barMinYY = this.y.getValue() + tensorGUI.titleHeight;
                int barMaxXX = this.x.getValue() + this.getWidth() + HackElement.width + 4 + SettingElement.width;
                int barMaxYY = this.y.getValue() + tensorGUI.guiHeight.get();

                if (barMinYY >= barMaxYY) {
                    return;
                }

                this.draw2DRectangle(barMinXX + 1, barMinYY + 3, barMaxXX - 1, barMaxYY - 1, fillColor.getRGB());
                this.draw2DRectangle(barMinXX, barMinYY + 3, barMinXX + 1, barMaxYY - 1, outLineColor.getRGB());
                this.draw2DRectangle(barMaxXX - 1, barMinYY + 3, barMaxXX, barMaxYY - 1, outLineColor.getRGB());
                this.draw2DRectangle(barMinXX, barMinYY, barMaxXX, barMinYY + 3, outLineColor.getRGB());
                this.draw2DRectangle(barMinXX, barMaxYY - 1, barMaxXX, barMaxYY + 1, outLineColor.getRGB());
            }

            int barMinX = this.x.getValue() + this.getWidth() + 2;
            int barMinY = this.y.getValue() + (this.hackElements.size() * HackElement.height) + tensorGUI.titleHeight + 2;
            int barMaxX = this.x.getValue() + this.getWidth() + HackElement.width + 2;
            int barMaxY = this.y.getValue() + tensorGUI.guiHeight.get() - 1;

            if (barMinY < barMaxY) {
                this.draw2DRectangle(barMinX + 1, barMinY + 1, barMaxX - 1, barMaxY - 1, fillColor.getRGB());
                this.draw2DRectangle(barMinX, barMinY + 1, barMinX + 1, barMaxY - 1, outLineColor.getRGB());
                this.draw2DRectangle(barMaxX - 1, barMinY + 1, barMaxX, barMaxY - 1, outLineColor.getRGB());
                this.draw2DRectangle(barMinX, barMinY, barMaxX, barMinY + 1, outLineColor.getRGB());
                this.draw2DRectangle(barMinX, barMaxY - 1, barMaxX, barMaxY + 1, outLineColor.getRGB());
            }

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setSelected(true);
        this.tensorGUI.categoryElements.forEach(categoryElement -> {

            if (categoryElement != this && categoryElement.isSelected()) {
                categoryElement.setSelected(false);

                categoryElement.mScrollY.setValue(0);

                categoryElement.hackElements.forEach(hackElement -> {

                    hackElement.sScrollY.setValue(0);

                    hackElement.settingElements.forEach(settingElement -> {

                        if (settingElement instanceof KeyPressElement) {
                            KeyPressElement element = (KeyPressElement) settingElement;
                            element.setSearching(false);
                        }
                    });
                });
            }
        });
    }
}
