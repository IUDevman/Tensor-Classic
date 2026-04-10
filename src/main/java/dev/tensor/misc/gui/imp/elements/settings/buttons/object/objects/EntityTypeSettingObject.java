package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.settings.buttons.EntityTypeListSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.imp.setting.settings.EntityTypeListSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-31-2026
 */

public final class EntityTypeSettingObject implements ButtonSettingObject {

    private final String value;
    private final EntityTypeListSetting setting;
    private final EntityTypeListSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final IntegerSetting scrollY;
    private final int offSetX;
    private final int offSetY;

    private boolean hovered = false;

    public EntityTypeSettingObject(String value, EntityTypeListSettingButton button, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int offsetX, int offsetY, int number) {
        this.value = value;
        this.setting = button.entityTypeListSetting;
        this.button = button;
        this.x = x;
        this.y = y;
        this.scrollY = scrollY;
        this.offSetX = offsetX;

        this.offSetY = (number * this.getHeight()) + offsetY;
    }

    @Override
    public int getWidth() {
        return this.button.tensorGUI.guiWidth - (2 * this.offSetX);
    }

    @Override
    public int getHeight() {
        return 15;
    }

    @Override
    public int getPosX() {
        return this.x.getValue() + this.offSetX;
    }

    @Override
    public int getPosY() {
        return this.y.getValue() + this.scrollY.getValue() + this.offSetY;
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {

        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), this.hovered ? selectedFillColor.getRGB() : fillColor.getRGB());

        this.drawString(this.value + " (" + (this.isEnabled() ? this.getColor(ChatColor.GREEN) + "True" : this.getColor(ChatColor.RED) + "False") + this.getColor(ChatColor.DEFAULT) + ")", this.getPosX() + 1, this.getPosY() + 3, Color.WHITE.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.isEnabled()) {
            this.setting.removeEntity(this.value);
        } else {
            this.setting.addEntity(this.value);
        }
    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        if (this.isHovered(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY) && !this.hovered) {
            this.hovered = true;
            this.button.resetHover(this);
        }
    }

    @Override
    public boolean hovered() {
        return this.hovered;
    }

    @Override
    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    private boolean isEnabled() {
        return this.setting.containsEntity(this.value);
    }
}
