package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.settings.buttons.SpammerListSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 07-04-2026
 */

public final class SpammerFileObject implements ButtonSettingObject {

    public final String name;
    private final SpammerListSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final IntegerSetting scrollY;
    private final int offSetY;

    private boolean hovered = false;
    private boolean hoveredOnActive = false;

    public SpammerFileObject(String name, SpammerListSettingButton button, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int offsetY, int number) {
        this.name = name;
        this.button = button;
        this.x = x;
        this.y = y;
        this.scrollY = scrollY;

        this.offSetY = (number * this.getHeight()) + offsetY;
    }

    @Override
    public boolean hovered() {
        return this.hovered;
    }

    @Override
    public void setHovered(boolean hovered) {
        this.hovered = hovered;

        if (!this.hovered) {
            this.hoveredOnActive = false;
        }
    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        this.setHovered(this.isHovered(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 102, this.getPosY() + this.getHeight(), mouseX, mouseY));

        if (!this.hovered()) {

            this.hoveredOnActive = this.isHovered(this.getPosX() + this.getWidth() - 102, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY);

        } else {
            this.hoveredOnActive = false;
        }
    }

    @Override
    public int getWidth() {
        return this.button.tensorGUI.guiWidth;
    }

    @Override
    public int getHeight() {
        return 30;
    }

    @Override
    public int getPosX() {
        return this.x.getValue();
    }

    @Override
    public int getPosY() {
        return this.y.getValue() + this.scrollY.getValue() + this.offSetY;
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        boolean isCurrent = this.button.isFileActive(this.name);

        this.startFlatGL(false);
        this.prepRectangle(this.hovered() ? selectedFillColor.getRGB() : fillColor.getRGB());

        this.genRectangleVertices(this.getPosX(), this.getPosY() + 2, this.getPosX() + this.getWidth() - 102, this.getPosY() + this.getHeight() - 2);

        this.swapRectangleColor(outLineColor.getRGB());

        this.genRectangleVertices(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 102, this.getPosY() + 2);
        this.genRectangleVertices(this.getPosX(), this.getPosY() + this.getHeight() - 2, this.getPosX() + this.getWidth() - 102, this.getPosY() + this.getHeight());
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 102, this.getPosY(), this.getPosX() + this.getWidth() - 100, this.getPosY() + this.getHeight());
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + 2);
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 100, this.getPosY() + this.getHeight() - 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight());

        this.swapRectangleColor(isCurrent ? (this.hoveredOnActive ? new Color(0, 255, 0, fillColor.getAlpha()).darker().getRGB() : new Color(0, 255, 0, fillColor.getAlpha()).getRGB()) : (this.hoveredOnActive ? new Color(255, 255, 0, fillColor.getAlpha()).darker().getRGB() : new Color(255, 255, 0, fillColor.getAlpha()).getRGB()));

        this.genRectangleVertices(this.getPosX() + this.getWidth() - 100, this.getPosY() + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 2);

        this.finishRectangle();
        this.finishFlatGL(false);

        this.drawCenteredString(isCurrent ? ">> Active <<" : ">> Set Active <<", this.getPosX() + this.getWidth() - (100 / 2), this.getPosY() - 4 + (this.getHeight() / 2), Color.WHITE.getRGB());
        this.drawString("File - " + (this.name.length() >= 40 ? this.name.substring(0, 39) : this.name) + ".txt", this.getPosX() + 5, this.getPosY() - 4 + (this.getHeight() / 2), Color.YELLOW.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.hoveredOnActive) {
            this.button.swapActiveFile(this.name);
        }
    }
}
