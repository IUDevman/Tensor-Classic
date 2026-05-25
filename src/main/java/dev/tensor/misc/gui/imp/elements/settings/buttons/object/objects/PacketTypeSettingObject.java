package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.settings.buttons.PacketTypeListSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import dev.tensor.misc.imp.setting.settings.PacketTypeListSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 5-24-2026
 */

public final class PacketTypeSettingObject implements ButtonSettingObject {

    private final String value;
    private final PacketTypeListSetting setting;
    private final PacketTypeListSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final IntegerSetting scrollY;
    private final int offSetX;
    private final int offSetY;

    private boolean hovered = false;

    public PacketTypeSettingObject(String value, PacketTypeListSettingButton button, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int offsetX, int offsetY, int number) {
        this.value = value;
        this.setting = button.packetTypeListSetting;
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

        this.drawString(this.value + " (" + (this.isEnabled() ? ChatColor.GREEN.text() + "True" : ChatColor.RED.text() + "False") + ChatColor.DEFAULT.text() + ")", this.getPosX() + 1, this.getPosY() + 3, Color.WHITE.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.isEnabled()) {
            this.setting.removePacket(this.value);
        } else {
            this.setting.addPacket(this.value);
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
        return this.setting.containsPacket(this.value);
    }
}
