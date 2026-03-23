package dev.tensor.misc.gui.elements.settings.buttons;

import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.elements.ButtonSetting;
import dev.tensor.misc.imp.setting.settings.StringSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

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

    /*
    TODO:
    1. Subscript string other direction
    2. Draw string from left not center
     */
    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor, Color textColor, Color enabledTextColor) {

        //temp:
        this.draw2DRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 102, this.getPosY() + this.tensorGUI.titleHeight, fillColor.getRGB());
        this.draw2DRectangle(this.getPosX() - 2, this.getPosY() - 2, this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, outLineColor.getRGB());
        this.draw2DRectangle(this.getPosX() + this.getWidth(), this.getPosY() - 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.tensorGUI.titleHeight + 2, outLineColor.getRGB());
        this.draw2DRectangle(this.getPosX(), this.getPosY() - 2, this.getPosX() + this.getWidth(), this.getPosY(), outLineColor.getRGB());
        this.draw2DRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 2, outLineColor.getRGB());
        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), "Setting (String) : " + this.stringSetting.getName(), this.getPosX() + ((this.getWidth() - 102) / 2), this.getPosY() + 2 + ((this.tensorGUI.titleHeight - 12) / 2), textColor.getRGB());

        this.draw2DRectangle(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight, this.hoveredOnBack ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.draw2DRectangle(this.getPosX() + this.getWidth() - 102, this.getPosY(), this.getPosX() + this.getWidth() - 100, this.getPosY() + this.tensorGUI.titleHeight, outLineColor.getRGB());
        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), ">> Go Back <<", this.getPosX() + this.getWidth() - (100 / 2), this.getPosY() + 2 + ((this.tensorGUI.titleHeight - 12) / 2), enabledTextColor.getRGB());

        this.draw2DRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 4 + this.tensorGUI.titleHeight, this.isSearching() ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.draw2DRectangle(this.getPosX() - 2, this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 6 + this.tensorGUI.titleHeight, outLineColor.getRGB());
        this.draw2DRectangle(this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.tensorGUI.titleHeight + 6 + this.tensorGUI.titleHeight, outLineColor.getRGB());
        this.draw2DRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + this.getWidth(), this.tensorGUI.titleHeight + 2, outLineColor.getRGB());
        this.draw2DRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 4 + this.tensorGUI.titleHeight, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 6 + this.tensorGUI.titleHeight, outLineColor.getRGB());

        int maxWidth = this.getWidth() - 20;
        int firstEnd = 1;

        int dotDotDot;
        boolean shouldDot = false;

        if (this.getFontRenderer().getStringWidth(this.value) >= maxWidth) {
            shouldDot = true;
            dotDotDot = this.getFontRenderer().getStringWidth("...");
        } else {
            dotDotDot = 0;
        }

        boolean strobe = (System.currentTimeMillis() - this.prevStrobe) >= 500;

        if (strobe && (System.currentTimeMillis() - this.prevStrobe >= 1000)) {
            this.prevStrobe = System.currentTimeMillis();
        }

        int searching = this.isSearching() ? (strobe ? this.getFontRenderer().getStringWidth("|") : 0) : 0;

        while (firstEnd < this.value.length() && (this.getFontRenderer().getStringWidth(this.value.substring(0, firstEnd + 1)) + dotDotDot + searching < maxWidth)) {
            firstEnd++;
        }

        //substring the other way!
        String croppedValue = (shouldDot ? "..." :  "") + this.value.substring(0, firstEnd) + (this.isSearching() ? (strobe ? "|" : "") : "");

        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), "'" + this.getColor(ChatColor.LIGHT_GRAY) + croppedValue + "§r'", this.getPosX() + (this.getWidth() / 2), this.getPosY() + this.tensorGUI.titleHeight + 4 + ((this.tensorGUI.titleHeight - 12) / 2), textColor.getRGB());

    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight, mouseX, mouseY)) {
            this.close();
        }

        if (this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + (2 * this.tensorGUI.titleHeight) + 4, mouseX, mouseY)) {
            this.setSearching(!this.isSearching());
        } else {
            this.setSearching(false);
        }
    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        this.hoveredOnBack = this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight, mouseX, mouseY);
    }

    @Override
    public void onKeyPressed(int key) {
        if (!isSearching()) return;

        if (key == Keyboard.KEY_DELETE) {
            //remove value
            return;
        }

        if (key != Keyboard.KEY_ESCAPE && key != Keyboard.KEY_RETURN) {
            //set value
            this.stringSetting.setValue(this.value);
            return;
        }

        this.value += Keyboard.getKeyName(key);
    }

    @Override
    public boolean isSearching() {
        return this.searching;
    }

    @Override
    public void setSearching(boolean searching) {
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
        this.hoveredOnBack = false;
        this.tensorGUI.closeButton();
    }
}
