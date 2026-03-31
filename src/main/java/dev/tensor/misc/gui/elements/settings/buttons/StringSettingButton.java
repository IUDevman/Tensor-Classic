package dev.tensor.misc.gui.elements.settings.buttons;

import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.elements.ButtonSetting;
import dev.tensor.misc.imp.setting.settings.StringSetting;
import net.minecraft.src.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Hoosiers
 * @since 03-23-2026
 */

public final class StringSettingButton implements ButtonSetting {

    public static final int height = 200;

    private final TensorGUI tensorGUI;
    private final ClickGUI clickGUI;
    private final StringSetting stringSetting;

    private boolean searching;
    private boolean active;
    private String value;

    public StringSettingButton(TensorGUI tensorGUI, ClickGUI clickGUI, StringSetting stringSetting) {
        this.tensorGUI = tensorGUI;
        this.clickGUI = clickGUI;
        this.stringSetting = stringSetting;
        this.value = stringSetting.getValue();
    }

    @Override
    public int getWidth() {
        return this.tensorGUI.guiWidth;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getPosX() {
        return this.clickGUI.x.getValue();
    }

    @Override
    public int getPosY() {
        return this.clickGUI.y.getValue();
    }

    private boolean hoveredOnBack = false;

    private long prevStrobe = System.currentTimeMillis();

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {

        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 102, this.getPosY() + this.tensorGUI.titleHeight, fillColor.getRGB());
        this.drawRectangle(this.getPosX() - 2, this.getPosY() - 2, this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth(), this.getPosY() - 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.tensorGUI.titleHeight + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() - 2, this.getPosX() + this.getWidth(), this.getPosY(), outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 2, outLineColor.getRGB());
        this.drawCenteredString("Setting (String) : " + this.stringSetting.getName(), this.getPosX() + ((this.getWidth() - 102) / 2), this.getPosY() + 2 + ((this.tensorGUI.titleHeight - 12) / 2), Color.WHITE.getRGB());

        this.drawRectangle(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight, this.hoveredOnBack ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 102, this.getPosY(), this.getPosX() + this.getWidth() - 100, this.getPosY() + this.tensorGUI.titleHeight, outLineColor.getRGB());
        this.drawCenteredString(">> Go Back <<", this.getPosX() + this.getWidth() - (100 / 2), this.getPosY() + 2 + ((this.tensorGUI.titleHeight - 12) / 2), Color.YELLOW.getRGB());

        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 4 + this.tensorGUI.titleHeight, this.isSearching() ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.drawRectangle(this.getPosX() - 2, this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 6 + this.tensorGUI.titleHeight, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.tensorGUI.titleHeight + 6 + this.tensorGUI.titleHeight, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + this.getWidth(), this.tensorGUI.titleHeight + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 4 + this.tensorGUI.titleHeight, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 6 + this.tensorGUI.titleHeight, outLineColor.getRGB());

        int maxWidth = this.getWidth() - 20;

        String toWrite = "";

        boolean strobe = this.isSearching() && (System.currentTimeMillis() - this.prevStrobe) >= 500;

        if (strobe && (System.currentTimeMillis() - this.prevStrobe >= 1000)) {
            this.prevStrobe = System.currentTimeMillis();
        }

        if (this.getFontRenderer().getStringWidth(this.value + (strobe ? "|" : "")) > maxWidth) {
            int split1 = this.value.length() - 1;

            while (split1 >= 1 && (this.getFontRenderer().getStringWidth("..." + this.value.substring(split1 - 1, this.value.length() - 1) + (strobe ? "|" : "")) <= maxWidth)) {
                split1--;
            }

            toWrite = "..." + this.getColor(ChatColor.LIGHT_GRAY) + this.value.substring(split1, this.value.length() - 1);

        } else {
            toWrite = this.getColor(ChatColor.LIGHT_GRAY) + this.value;
        }

        this.drawString("'" + toWrite + this.getColor(ChatColor.YELLOW) + (strobe ? "|" : "") + "'", this.getPosX() + 10, this.getPosY() + this.tensorGUI.titleHeight + 4 + ((this.tensorGUI.titleHeight - 12) / 2), Color.YELLOW.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight, mouseX, mouseY)) {
            this.close();
        }

        if (this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + (2 * this.tensorGUI.titleHeight) + 4, mouseX, mouseY)) {

            if (this.isSearching()) {
                this.stringSetting.setValue(this.value);
                this.setSearching(false);
                return;
            }

            this.setSearching(true);
        } else {
            this.setSearching(false);
        }
    }

    @Override
    public void handleScroll(int scroll) {

    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        this.hoveredOnBack = this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight, mouseX, mouseY);
    }

    @Override
    public void onKeyPressed(char letter, int key) {
        if (!isSearching()) return;

        if (key == Keyboard.KEY_DELETE || key == Keyboard.KEY_BACK) {

            if (!this.value.isEmpty()) {
                this.value = this.value.substring(0, this.value.length() - 1);
            }

            return;
        }

        if (key == Keyboard.KEY_ESCAPE || key == Keyboard.KEY_RETURN) {
            this.stringSetting.setValue(this.value);
            this.setSearching(false);
            return;
        }

        if (!this.isKeyBlackListed(key) && ChatAllowedCharacters.allowedCharacters.contains(String.valueOf(letter))) {
            this.value = this.value + letter;
        }
    }

    private boolean isKeyBlackListed(int key) {
        return Objects.equals(this.badKeys, key);
    }

    private final int[] badKeys = new int[]{
            0x00, 0x01, 0x0E, 0x0F, 0x1C, 0x1D, 0x2A, 0x36,
            0x37, 0x38, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F, 0x40,
            0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x53, 0x57, 0x58,
            0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x70, 0x71, 0x79,
            0x7B, 0x7D, 0x8D, 0x90, 0x91, 0x93, 0x94, 0x95, 0x96,
            0x97, 0x9C, 0x9D, 0xA7, 0xB7, 0xB8, 0xC4, 0xC5, 0xC7,
            0xC9, 0xCF, 0xD1, 0xD2, 0xD3, 0xDA, 0xDB, 0xDC, 0xDD,
            0xDE, 0xDF
    };

    @Override
    public boolean isSearching() {
        return this.searching;
    }

    @Override
    public void setSearching(boolean searching) {

        if (searching) {
            this.prevStrobe = System.currentTimeMillis();
        }

        if (!searching) {
            this.stringSetting.setValue(this.value);
        }

        this.value = this.stringSetting.getValue();

        this.searching = searching;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void close() {
        this.setActive(false);
        this.setSearching(false);
        this.hoveredOnBack = false;
        this.tensorGUI.closeButton();
    }
}
