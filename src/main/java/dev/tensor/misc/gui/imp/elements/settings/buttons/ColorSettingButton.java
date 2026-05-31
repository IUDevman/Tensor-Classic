package dev.tensor.misc.gui.imp.elements.settings.buttons;

import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.ButtonSetting;
import dev.tensor.misc.gui.imp.elements.HackElement;
import dev.tensor.misc.gui.imp.elements.settings.ColorSettingElement;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.color.AlphaPickerObject;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.color.BigColorPickerObject;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.color.OtherHackColorObject;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.color.SmallColorPickerObject;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 05-27-2026
 */

public final class ColorSettingButton implements ButtonSetting {

    public final TensorGUI tensorGUI;
    private final ClickGUI clickGUI;
    private final HackElement hackElement;
    public final ColorSettingElement colorSettingElement;

    public final BigColorPickerObject bigColorPickerObject;
    public final SmallColorPickerObject smallColorPickerObject;
    public final AlphaPickerObject alphaPickerObject;

    private final ArrayList<OtherHackColorObject> otherHackColorObjects = new ArrayList<>();

    public boolean hasAlpha = true;

    private boolean hoveredOnBack = false;
    private boolean hoveredOnList = false;

    private boolean active;

    private boolean searchingR = false;
    private boolean searchingG = false;
    private boolean searchingB = false;
    private boolean searchingA = false;
    private String value = "";

    private final IntegerSetting scrollY = new IntegerSetting("ColorSettingList Scroll Y", 0, -10000, 10000);

    public ColorSettingButton(TensorGUI tensorGUI, ClickGUI clickGUI, HackElement hackElement, ColorSettingElement colorSettingElement) {
        this.tensorGUI = tensorGUI;
        this.clickGUI = clickGUI;
        this.hackElement = hackElement;
        this.colorSettingElement = colorSettingElement;

        this.smallColorPickerObject = new SmallColorPickerObject(this, this.clickGUI.x, this.clickGUI.y, 148, this.tensorGUI.titleBoxHeight + 12, 32, 128);
        this.bigColorPickerObject = new BigColorPickerObject(this, this.clickGUI.x, this.clickGUI.y, 10, this.tensorGUI.titleBoxHeight + 12, 128, 128);
        this.alphaPickerObject = new AlphaPickerObject(this, this.clickGUI.x, this.clickGUI.y, 190, this.tensorGUI.titleBoxHeight + 12, 32, 128);
    }

    @Override
    public int getWidth() {
        return this.tensorGUI.guiWidth;
    }

    @Override
    public int getHeight() {
        return this.tensorGUI.guiHeight.get();
    }

    @Override
    public int getPosX() {
        return this.clickGUI.x.getValue();
    }

    @Override
    public int getPosY() {
        return this.clickGUI.y.getValue();
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 102, this.getPosY() + this.tensorGUI.titleBoxHeight, fillColor.getRGB());
        this.drawRectangle(this.getPosX() - 2, this.getPosY() - 2, this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth(), this.getPosY() - 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() - 2, this.getPosX() + this.getWidth(), this.getPosY(), outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, outLineColor.getRGB());
        this.drawCenteredString(this.hackElement.hack.NAME + " : " + this.colorSettingElement.colorSetting.getName(), this.getPosX() + ((this.getWidth() - 102) / 2), this.getPosY() + 2 + ((this.tensorGUI.titleBoxHeight - 12) / 2), Color.WHITE.getRGB());

        this.drawRectangle(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight, this.hoveredOnBack ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 102, this.getPosY(), this.getPosX() + this.getWidth() - 100, this.getPosY() + this.tensorGUI.titleBoxHeight, outLineColor.getRGB());
        this.drawCenteredString(">> Go Back <<", this.getPosX() + this.getWidth() - (100 / 2), this.getPosY() + 2 + ((this.tensorGUI.titleBoxHeight - 12) / 2), Color.YELLOW.getRGB());

        this.drawRectangle(this.getPosX() - 2, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX(), this.getPosY() + this.getHeight() + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.getHeight() + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth(), this.tensorGUI.titleBoxHeight + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() + 2, outLineColor.getRGB());

        this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight() - 110, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 108, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight() - 108, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 90, fillColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight() - 90, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 88, outLineColor.getRGB());

        this.smallColorPickerObject.render(tensorGUI, fillColor, selectedFillColor, outLineColor);
        this.bigColorPickerObject.render(tensorGUI, fillColor, selectedFillColor, outLineColor);
        this.alphaPickerObject.render(tensorGUI, fillColor, selectedFillColor, outLineColor);

        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 12, fillColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight() - 120, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 110, fillColor.getRGB());

        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 12, this.getPosX() + 10, this.getPosY() + this.getHeight() - 120, fillColor.getRGB());
        this.drawRectangle(this.getPosX() + 138, this.getPosY() + this.tensorGUI.titleBoxHeight + 12, this.getPosX() + 148, this.getPosY() + this.getHeight() - 120, fillColor.getRGB());
        this.drawRectangle(this.getPosX() + 180, this.getPosY() + this.tensorGUI.titleBoxHeight + 12, this.getPosX() + 190, this.getPosY() + this.getHeight() - 120, fillColor.getRGB());
        this.drawRectangle(this.getPosX() + 222, this.getPosY() + this.getHeight() - 152, this.getPosX() + 232, this.getPosY() + this.getHeight() - 120, fillColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 10, this.getPosY() + this.getHeight() - 152, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 120, fillColor.getRGB());

        this.drawRectangle(this.getPosX() + 222, this.getPosY() + this.tensorGUI.titleBoxHeight + 12, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 152, fillColor.getRGB());

        this.drawRectangle(this.getPosX() + 234, this.getPosY() + this.getHeight() - 150, this.getPosX() + this.getWidth() - 12, this.getPosY() + this.getHeight() - 122, this.colorSettingElement.colorSetting.getValue().getRGB());
        this.drawRectangle(this.getPosX() + 232, this.getPosY() + this.getHeight() - 152, this.getPosX() + this.getWidth() - 10, this.getPosY() + this.getHeight() - 150, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + 232, this.getPosY() + this.getHeight() - 122, this.getPosX() + this.getWidth() - 10, this.getPosY() + this.getHeight() - 120, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + 232, this.getPosY() + this.getHeight() - 150, this.getPosX() + 234, this.getPosY() + this.getHeight() - 122, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 12, this.getPosY() + this.getHeight() - 150, this.getPosX() + this.getWidth() - 10, this.getPosY() + this.getHeight() - 122, outLineColor.getRGB());

        this.drawString("Copy Color From Other ColorSetting - ", this.getPosX() + 2, this.getPosY() + this.getHeight() - 106, Color.WHITE.getRGB());

        int startX = this.getPosX() + 232;
        int startY = this.getPosY() + this.tensorGUI.titleBoxHeight + 7;

        this.drawCenteredString("> Reset <", startX + ((this.getPosX() + this.getWidth() - 10 - startX) / 2), startY + 18, Color.WHITE.getRGB());
        this.drawString("  > Red (0 -> 255) : " + ChatColor.LIGHT_GRAY.text() + (this.searchingR ? (this.value.equalsIgnoreCase("") ? "[...]" : this.value) : "[" + this.colorSettingElement.colorSetting.getValue().getRed() + "]"), startX + 3, startY + 33, Color.WHITE.getRGB());
        this.drawString("  > Green (0 -> 255) : " + ChatColor.LIGHT_GRAY.text() + (this.searchingG ? (this.value.equalsIgnoreCase("") ? "[...]" : this.value) : "[" + this.colorSettingElement.colorSetting.getValue().getGreen() + "]"), startX + 3, startY + 48, Color.WHITE.getRGB());
        this.drawString("  > Blue (0 -> 255) : " + ChatColor.LIGHT_GRAY.text() + (this.searchingB ? (this.value.equalsIgnoreCase("") ? "[...]" : this.value) : "[" + this.colorSettingElement.colorSetting.getValue().getBlue() + "]"), startX + 3, startY + 63, Color.WHITE.getRGB());
        this.drawString("  > Alpha (0 -> 255) : " + ChatColor.LIGHT_GRAY.text() + (!this.hasAlpha ? "[N/A]" : (this.searchingA ? (this.value.equalsIgnoreCase("") ? "[...]" : this.value) : "[" + this.colorSettingElement.colorSetting.getValue().getAlpha() + "]")), startX + 3, startY + 78, Color.WHITE.getRGB());

        if (this.otherHackColorObjects.isEmpty()) {
            AtomicInteger count = new AtomicInteger(0);

            int offsetY = this.getHeight() - 88;
            this.getAllHackColors().forEach((colorSetting, hack) -> this.otherHackColorObjects.add(new OtherHackColorObject(this, hack, colorSetting, this.clickGUI.x, this.clickGUI.y, this.scrollY, offsetY, count.getAndAdd(1))));
        }

        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        int posX = this.getPosX();
        int posY = this.getPosY() + this.getHeight() - 88;
        int width1 = this.getWidth();
        int height1 = this.getHeight() - 192;

        int windowHeight = this.getMinecraft().displayHeight;
        double scaleX = (double) this.getMinecraft().displayWidth / (double) this.getMinecraft().currentScreen.width;
        double scaleY = (double) this.getMinecraft().displayHeight / (double) this.getMinecraft().currentScreen.height;
        int widthScaled = (int) (scaleX * width1);
        int heightScaled = (int) (scaleY * height1);
        int xScaled = (int) (posX * scaleX);
        int yScaled = (int) (posY * scaleY);

        GL11.glScissor(xScaled, windowHeight - yScaled - heightScaled, widthScaled, heightScaled);

        this.otherHackColorObjects.forEach(otherHackColorObject -> otherHackColorObject.render(tensorGUI, fillColor, selectedFillColor, outLineColor));

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight, mouseX, mouseY)) {
            this.close();
        }

        if (this.isHovered(this.getPosX(), this.getPosY() + this.getHeight() - 88, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY)) {

            this.otherHackColorObjects.forEach(otherHackColorObject -> otherHackColorObject.onClick(mouseX, mouseY));
        }

        int startX = this.getPosX() + 232;
        int startY = this.getPosY() + this.tensorGUI.titleBoxHeight + 7;

        int resetXStart = startX + ((this.getPosX() + this.getWidth() - 10 - startX) / 2);

        if (this.isHovered(resetXStart, startY + 18, resetXStart + this.stringWidth("> Reset <"), startY + 30, mouseX, mouseY)) {
            this.resetColorValue();
            this.clearSearchingText();
            return;
        }

        if (this.isHovered(startX + 3, startY + 33, startX + 3 + this.stringWidth("  > Red (0 -> 255) : " + (this.searchingR ? (this.value.equalsIgnoreCase("") ? "[...]" : this.value) : "[" + this.colorSettingElement.colorSetting.getValue().getRed() + "]")), startY + 43, mouseX, mouseY)) {
            boolean oldR = this.searchingR;
            this.clearSearchingText();
            this.searchingR = !oldR;
            return;
        }

        if (this.isHovered(startX + 3, startY + 48, startX + 3 + this.stringWidth("  > Green (0 -> 255) : " + (this.searchingG ? (this.value.equalsIgnoreCase("") ? "[...]" : this.value) : "[" + this.colorSettingElement.colorSetting.getValue().getGreen() + "]")), startY + 58, mouseX, mouseY)) {
            boolean oldG = this.searchingG;
            this.clearSearchingText();
            this.searchingG = !oldG;
            return;
        }

        if (this.isHovered(startX + 3, startY + 63, startX + 3 + this.stringWidth("  > Blue (0 -> 255) : " + (this.searchingB ? (this.value.equalsIgnoreCase("") ? "[...]" : this.value) : "[" + this.colorSettingElement.colorSetting.getValue().getBlue() + "]")), startY + 73, mouseX, mouseY)) {
            boolean oldB = this.searchingB;
            this.clearSearchingText();
            this.searchingB = !oldB;
            return;

        }

        if (this.isHovered(startX + 3, startY + 78, startX + 3 + this.stringWidth("  > Alpha (0 -> 255) : " + (!this.hasAlpha ? "[N/A]" : (this.searchingA ? (this.value.equalsIgnoreCase("") ? "[...]" : this.value) : "[" + this.colorSettingElement.colorSetting.getValue().getAlpha() + "]"))), startY + 88, mouseX, mouseY)) {
            boolean oldA = this.searchingA;
            this.clearSearchingText();

            if (this.hasAlpha) {
                this.searchingA = !oldA;
            }

            return;
        }

        this.clearSearchingText();
    }

    public void onRenderDrag(int screenX, int screenY) {
        this.bigColorPickerObject.onRenderDrag(screenX, screenY);
        this.alphaPickerObject.onRenderDrag(screenX, screenY);
    }

    public void onClickDrag(double mouseX, double mouseY) {
        this.bigColorPickerObject.onClick(mouseX, mouseY);
        this.alphaPickerObject.onClick(mouseX, mouseY);
    }

    public void resetDrag() {
        this.bigColorPickerObject.resetDrag();
        this.alphaPickerObject.resetDrag();
    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        this.hoveredOnBack = this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight, mouseX, mouseY);

        if (this.isHovered(this.getPosX(), this.getPosY() + this.getHeight() - 88, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY)) {
            this.hoveredOnList = true;
            this.otherHackColorObjects.forEach(otherHackColorObject -> otherHackColorObject.onHovered(mouseX, mouseY));
        } else {
            this.otherHackColorObjects.forEach(otherHackColorObject -> otherHackColorObject.setHovered(false));
            this.hoveredOnList = false;
        }
    }

    @Override
    public void handleScroll(int scroll) {
        if (!this.hoveredOnList) {
            return;
        }

        if (scroll == 0) {
            return;
        }

        int scrollAdjust = scroll;

        if (this.scrollY.getValue() > -this.tensorGUI.getScrollSpeed() && scroll > 0) {

            scrollAdjust = -this.scrollY.getValue();
        }

        int maxHeight = this.getHeight() - 190;

        if (this.getColorListHeight() <= maxHeight) {
            return;
        }

        if (this.getColorListHeight() + (scroll < 0 ? this.scrollY.getValue() - this.tensorGUI.getScrollSpeed() : 0) <= maxHeight) {
            int test = ((this.getColorListHeight() + (scroll < 0 ? this.scrollY.getValue() : 0)) - maxHeight);

            if (test >= scrollAdjust) {
                scrollAdjust = -test;
            } else {
                return;
            }
        }

        this.scrollY.setValue(this.scrollY.getValue() + scrollAdjust);
    }

    @Override
    public void close() {
        this.setActive(false);
        this.setSearching(false);
        this.scrollY.setValue(0);
        this.resetDrag();
        //todo
        this.tensorGUI.closeButton();
    }

    @Override
    public boolean isSearching() {
        return this.searchingR || this.searchingG || this.searchingB || this.searchingA;
    }

    @Override
    public void setSearching(boolean searching) {
        if (!searching) {
            this.hoveredOnBack = false;
            this.hoveredOnList = false;

            this.clearSearchingText();
        }
    }

    private void clearSearchingText() {
        this.searchingR = false;
        this.searchingG = false;
        this.searchingB = false;
        this.searchingA = false;
        this.value = "";
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
    public void onKeyPressed(char letter, int key) {
        if (!this.searchingR && !this.searchingG && !this.searchingB && !this.searchingA) return;

        if (key == Keyboard.KEY_DELETE || key == Keyboard.KEY_ESCAPE) {
            this.clearSearchingText();
            return;
        }

        if (key == Keyboard.KEY_RETURN) {

            if (this.value.isEmpty()) {
                this.clearSearchingText();
                return;
            }

            int value1 = Integer.parseInt(this.value);

            if (value1 > 255) value1 = 255;
            else if (value1 < 0) value1 = 0;

            this.setValueFromText(value1);

            this.value = "";
            this.clearSearchingText();
        }

        if (Arrays.stream(this.acceptedKeys()).noneMatch(value -> value == key)) {
            return;
        }

        if (key == Keyboard.KEY_BACK) {
            if (this.value.isEmpty()) return;
            this.value = this.value.substring(0, this.value.length() - 1);
            return;
        }

        if (this.value.length() >= 3) {
            return;
        }

        this.value += Keyboard.getKeyName(key);
    }

    private LinkedHashMap<ColorSetting, Hack> getAllHackColors() {
        LinkedHashMap<ColorSetting, Hack> toReturn = new LinkedHashMap<>();

        this.getHackManager().getHacks().forEach(hack -> {

            hack.getSettings().forEach(setting -> {

                if (setting instanceof ColorSetting && setting != this.colorSettingElement.colorSetting) {
                    toReturn.put((ColorSetting) setting, hack);
                }
            });
        });

        return toReturn;
    }

    private int getColorListHeight() {
        return this.otherHackColorObjects.size() * 15;
    }

    private int[] acceptedKeys() {
        return new int[]{
                Keyboard.KEY_0,
                Keyboard.KEY_1,
                Keyboard.KEY_2,
                Keyboard.KEY_3,
                Keyboard.KEY_4,
                Keyboard.KEY_5,
                Keyboard.KEY_6,
                Keyboard.KEY_7,
                Keyboard.KEY_8,
                Keyboard.KEY_9,
                Keyboard.KEY_BACK
        };
    }

    private void setValueFromText(int value) {
        Color oldColor = this.colorSettingElement.colorSetting.getValue();

        if (this.searchingR) {
            this.colorSettingElement.colorSetting.setValue(new Color(value, oldColor.getGreen(), oldColor.getBlue(), oldColor.getAlpha()));
        } else if (this.searchingG) {
            this.colorSettingElement.colorSetting.setValue(new Color(oldColor.getRed(), value, oldColor.getBlue(), oldColor.getAlpha()));
        } else if (this.searchingB) {
            this.colorSettingElement.colorSetting.setValue(new Color(oldColor.getRed(), oldColor.getGreen(), value, oldColor.getAlpha()));
        } else if (this.searchingA && this.hasAlpha) {
            this.colorSettingElement.colorSetting.setValue(new Color(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), value));
        }
    }

    private void resetColorValue() {
        this.colorSettingElement.colorSetting.setValue(this.colorSettingElement.colorSetting.getDefaultValue());
    }

    public void setNewColorStealth(Color color, boolean alpha) {
        Color withAlpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), this.hasAlpha ? (alpha ? color.getAlpha() :  this.colorSettingElement.colorSetting.getValue().getAlpha()) : 255);

        this.colorSettingElement.colorSetting.setValueStealth(withAlpha);
    }
}
