package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 05-24-2026
 */

public final class AccountPasswordObject implements ButtonSettingObject {

    public final AccountObject accountObject;
    public final String server;
    private final String password;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final IntegerSetting scrollY;
    private final int offsetX;
    private final int offsetY;

    private boolean hovered = false;
    private boolean hoveredOnDelete = false;

    public int number = 0;

    public AccountPasswordObject(AccountObject accountObject, String server, String password, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int offsetX, int offsetY, int number) {
        this.accountObject = accountObject;
        this.server = server;
        this.password = password;
        this.x = x;
        this.y = y;
        this.scrollY = scrollY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.number = number;
    }

    @Override
    public int getWidth() {
        return this.accountObject.button.tensorGUI.guiWidth - (2 * this.offsetX);
    }

    @Override
    public int getHeight() {
        return 15;
    }

    @Override
    public int getPosX() {
        return this.x.getValue() + this.offsetX;
    }

    @Override
    public int getPosY() {
        return this.y.getValue() + this.scrollY.getValue() + this.offsetY + this.accountObject.button.getAccountObjectOffsetY(this.accountObject.number) + (this.number * this.getHeight()) + this.accountObject.getHeight() - 2;
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        this.startFlatGL(false);
        this.prepRectangle(this.hovered ? selectedFillColor.getRGB() : fillColor.getRGB());

        this.genRectangleVertices(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 55, this.getPosY() + this.getHeight());

        this.swapRectangleColor(outLineColor.getRGB());
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 55, this.getPosY(), this.getPosX() + this.getWidth() - 53, this.getPosY() + this.getHeight());

        this.swapRectangleColor(this.hoveredOnDelete ? new Color(255, 0, 0, fillColor.getAlpha()).darker().getRGB() : new Color(255, 0, 0, fillColor.getAlpha()).getRGB());
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 53, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight());

        this.finishRectangle();
        this.finishFlatGL(false);

        this.drawString("> Delete <", this.getPosX() + this.getWidth() - ((this.getFontRenderer().getStringWidth("> Delete <") + 53) / 2), this.getPosY() + 3, Color.YELLOW.getRGB());
        this.drawString(this.server + " - " + ChatColor.LIGHT_GRAY.text() + this.password, this.getPosX() + 3, this.getPosY() + 3, Color.WHITE.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.hoveredOnDelete) {
            this.accountObject.queuePasswordForRemoval(this);
            this.hoveredOnDelete = false;
        }
    }

    @Override
    public boolean hovered() {
        return this.hovered || this.hoveredOnDelete;
    }

    @Override
    public void setHovered(boolean hovered) {
        if (!hovered) {
            this.hovered = false;
            this.hoveredOnDelete = false;
        }
    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        if (this.isHovered(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 55, this.getPosY() + this.getHeight(), mouseX, mouseY)) {
            this.hovered = true;
            this.hoveredOnDelete = false;
            this.accountObject.clearHover(this);

        } else if (this.isHovered(this.getPosX() + this.getWidth() - 53, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY)) {
            this.hovered = false;
            this.hoveredOnDelete = true;
            this.accountObject.clearHover(this);

        } else {
            this.setHovered(false);
        }
    }
}
