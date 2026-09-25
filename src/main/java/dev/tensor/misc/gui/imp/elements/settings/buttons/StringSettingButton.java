package dev.tensor.misc.gui.imp.elements.settings.buttons;

import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.ButtonSetting;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.setting.settings.StringSetting;
import net.minecraft.src.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.util.Arrays;

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
    private boolean hoveredOnClear = false;
    private boolean hoveredOnPaste = false;

    private long prevStrobe = System.currentTimeMillis();

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        this.startFlatGL(false);
        this.prepRectangle(fillColor.getRGB());

        this.genRectangleVertices(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 102, this.getPosY() + this.tensorGUI.titleBoxHeight);

        this.swapRectangleColor(outLineColor.getRGB());

        this.genRectangleVertices(this.getPosX() - 2, this.getPosY() - 2, this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2);
        this.genRectangleVertices(this.getPosX() + this.getWidth(), this.getPosY() - 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.tensorGUI.titleBoxHeight + 2);
        this.genRectangleVertices(this.getPosX(), this.getPosY() - 2, this.getPosX() + this.getWidth(), this.getPosY());
        this.genRectangleVertices(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2);
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 102, this.getPosY(), this.getPosX() + this.getWidth() - 100, this.getPosY() + this.tensorGUI.titleBoxHeight);
        this.genRectangleVertices(this.getPosX() - 2, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 6 + this.tensorGUI.titleBoxHeight);
        this.genRectangleVertices(this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.tensorGUI.titleBoxHeight + 6 + this.tensorGUI.titleBoxHeight);
        this.genRectangleVertices(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 4 + this.tensorGUI.titleBoxHeight, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 6 + this.tensorGUI.titleBoxHeight);
        this.genRectangleVertices(this.getPosX() - 2, this.getPosY() + (2 * this.tensorGUI.titleBoxHeight) + 6, this.getPosX(), this.getPosY() + (3 * this.tensorGUI.titleBoxHeight) + 6);
        this.genRectangleVertices(this.getPosX() + 100, this.getPosY() + (2 * this.tensorGUI.titleBoxHeight) + 6, this.getPosX() + 102, this.getPosY() + (3 * this.tensorGUI.titleBoxHeight) + 6);
        this.genRectangleVertices(this.getPosX() + 202, this.getPosY() + (2 * this.tensorGUI.titleBoxHeight) + 6, this.getPosX() + 204, this.getPosY() + (3 * this.tensorGUI.titleBoxHeight) + 6);
        this.genRectangleVertices(this.getPosX() - 2, this.getPosY() + (3 * this.tensorGUI.titleBoxHeight) + 6, this.getPosX() + 204, this.getPosY() + (3 * this.tensorGUI.titleBoxHeight) + 8);

        this.swapRectangleColor(this.hoveredOnClear ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.genRectangleVertices(this.getPosX(), this.getPosY() + (2 * this.tensorGUI.titleBoxHeight) + 6, this.getPosX() + 100, this.getPosY() + (3 * this.tensorGUI.titleBoxHeight) + 6);

        this.swapRectangleColor(this.hoveredOnPaste ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.genRectangleVertices(this.getPosX() + 102, this.getPosY() + (2 * this.tensorGUI.titleBoxHeight) + 6, this.getPosX() + 202, this.getPosY() + (3 * this.tensorGUI.titleBoxHeight) + 6);

        this.swapRectangleColor(this.hoveredOnBack ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight);

        this.swapRectangleColor(this.isSearching() ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.genRectangleVertices(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 4 + this.tensorGUI.titleBoxHeight);

        this.finishRectangle();
        this.finishFlatGL(false);

        this.drawCenteredString("Setting (String) : " + this.stringSetting.getName(), this.getPosX() + ((this.getWidth() - 102) / 2), this.getPosY() + 2 + ((this.tensorGUI.titleBoxHeight - 12) / 2), Color.WHITE.getRGB());
        this.drawCenteredString(">> Go Back <<", this.getPosX() + this.getWidth() - (100 / 2), this.getPosY() + 2 + ((this.tensorGUI.titleBoxHeight - 12) / 2), Color.YELLOW.getRGB());
        this.drawCenteredString(">> Clear <<", this.getPosX() + 50, (this.getPosY() + (3 * this.tensorGUI.titleBoxHeight)) - 8, Color.YELLOW.getRGB());
        this.drawCenteredString(">> Paste <<", this.getPosX() + 152, (this.getPosY() + (3 * this.tensorGUI.titleBoxHeight)) - 8, Color.YELLOW.getRGB());

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

            toWrite = "..." + ChatColor.LIGHT_GRAY.text() + this.value.substring(split1, this.value.length() - 1);

        } else {
            toWrite = ChatColor.LIGHT_GRAY.text() + this.value;
        }

        this.drawString("'" + toWrite + ChatColor.YELLOW.text() + (strobe ? "|" : "") + "'", this.getPosX() + 10, this.getPosY() + this.tensorGUI.titleBoxHeight + 4 + ((this.tensorGUI.titleBoxHeight - 12) / 2), Color.YELLOW.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.hoveredOnBack) {
            this.close();

        } else if (this.hoveredOnClear) {
            this.stringSetting.setValue("");
            this.value = "";

        } else if (this.hoveredOnPaste) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            if (clipboard != null) {
                try {
                    String result = clipboard.getData(DataFlavor.stringFlavor).toString();

                    if (result != null && !result.isEmpty()) {
                        for (int i = 0; i < result.length(); i++) {
                            this.handleAddingChar(result.charAt(i));
                        }
                    }

                } catch (Exception ignored) {
                    this.getTensorLogger().log("Failed to get clipboard message!", TensorLogger.Level.WARN);
                }
            }
        }

        if (this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + (2 * this.tensorGUI.titleBoxHeight) + 4, mouseX, mouseY)) {

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
        this.hoveredOnBack = this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight, mouseX, mouseY);

        if (!this.hoveredOnBack) {
            this.hoveredOnClear = this.isHovered(this.getPosX(), this.getPosY() + (2 * this.tensorGUI.titleBoxHeight) + 6, this.getPosX() + 100, this.getPosY() + (3 * this.tensorGUI.titleBoxHeight) + 6, mouseX, mouseY);

            if (!this.hoveredOnClear) {
                this.hoveredOnPaste = this.isHovered(this.getPosX() + 102, this.getPosY() + (2 * this.tensorGUI.titleBoxHeight) + 6, this.getPosX() + 202, this.getPosY() + (3 * this.tensorGUI.titleBoxHeight) + 6, mouseX, mouseY);

            } else {
                this.hoveredOnPaste = false;
            }

        } else {
            this.hoveredOnClear = false;
            this.hoveredOnPaste = false;
        }
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

        if (!this.isKeyBlackListed(key)) {
            this.handleAddingChar(letter);
        }
    }

    private void handleAddingChar(char letter) {
        if (ChatAllowedCharacters.allowedCharacters.contains(String.valueOf(letter))) {
            this.value = this.value + letter;
        }
    }

    private boolean isKeyBlackListed(int key) {
        return Arrays.binarySearch(this.badKeys, key) >= 0;
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
        this.hoveredOnClear = false;
        this.hoveredOnPaste = false;
        this.tensorGUI.closeButton();
    }
}
