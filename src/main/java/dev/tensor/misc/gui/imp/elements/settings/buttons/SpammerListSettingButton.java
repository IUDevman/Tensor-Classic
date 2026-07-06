package dev.tensor.misc.gui.imp.elements.settings.buttons;

import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.ButtonSetting;
import dev.tensor.misc.gui.imp.elements.HackElement;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.SpammerFileObject;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import dev.tensor.misc.imp.setting.settings.SpammerListSetting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 07-04-2026
 */

public final class SpammerListSettingButton implements ButtonSetting {

    public final TensorGUI tensorGUI;
    private final ClickGUI clickGUI;
    private final HackElement hackElement;
    private final SpammerListSetting setting;

    private final ArrayList<SpammerFileObject> spammerFileObjects = new ArrayList<>();
    private final IntegerSetting scrollY = new IntegerSetting("SpammerList Scroll Y", 0, -1000, 1000);

    private boolean active;

    private boolean hoveredOnBack = false;

    public SpammerListSettingButton(TensorGUI tensorGUI, ClickGUI clickGUI, HackElement hackElement, SpammerListSetting setting) {
        this.tensorGUI = tensorGUI;
        this.clickGUI = clickGUI;
        this.hackElement = hackElement;
        this.setting = setting;

        this.loadOrResetSpammerList();
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
        this.startFlatGL(false);
        this.prepRectangle(fillColor.getRGB());

        this.genRectangleVertices(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 102, this.getPosY() + this.tensorGUI.titleBoxHeight);

        this.swapRectangleColor(outLineColor.getRGB());

        this.genRectangleVertices(this.getPosX() - 2, this.getPosY() - 2, this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2);
        this.genRectangleVertices(this.getPosX() + this.getWidth(), this.getPosY() - 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.tensorGUI.titleBoxHeight + 2);
        this.genRectangleVertices(this.getPosX(), this.getPosY() - 2, this.getPosX() + this.getWidth(), this.getPosY());
        this.genRectangleVertices(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2);
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 102, this.getPosY(), this.getPosX() + this.getWidth() - 100, this.getPosY() + this.tensorGUI.titleBoxHeight);
        this.genRectangleVertices(this.getPosX() - 2, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX(), this.getPosY() + this.getHeight() + 2);
        this.genRectangleVertices(this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.getHeight() + 2);
        this.genRectangleVertices(this.getPosX(), this.getPosY() + this.getHeight(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() + 2);

        this.swapRectangleColor(this.hoveredOnBack ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight);

        this.finishRectangle();
        this.finishFlatGL(false);

        this.drawCenteredString(this.hackElement.hack.NAME + " : File List", this.getPosX() + ((this.getWidth() - 102) / 2), this.getPosY() + 2 + ((this.tensorGUI.titleBoxHeight - 12) / 2), Color.WHITE.getRGB());
        this.drawCenteredString(">> Go Back <<", this.getPosX() + this.getWidth() - (100 / 2), this.getPosY() + 2 + ((this.tensorGUI.titleBoxHeight - 12) / 2), Color.YELLOW.getRGB());

        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        int posX = this.getPosX();
        int posY = this.getPosY() + this.tensorGUI.titleBoxHeight + 2;
        int width1 = this.getWidth();
        int height1 = this.getHeight() - this.tensorGUI.titleBoxHeight - 2;

        double scaleX = (double) this.getMinecraft().displayWidth / (double) this.getMinecraft().currentScreen.width;
        double scaleY = (double) this.getMinecraft().displayHeight / (double) this.getMinecraft().currentScreen.height;

        GL11.glScissor((int) Math.ceil(posX * scaleX), this.getMinecraft().displayHeight - (int) Math.ceil((posY + height1) * scaleY), (int) Math.ceil(width1 * scaleX), (int) Math.ceil(height1 * scaleY));

        this.spammerFileObjects.forEach(spammerFileObject -> spammerFileObject.render(tensorGUI, fillColor, selectedFillColor, outLineColor));

        if ((this.spammerFileObjects.size() * 30) < this.getHeight() - this.tensorGUI.titleBoxHeight - 2) {
            if (this.spammerFileObjects.isEmpty()) {
                this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2 + (this.spammerFileObjects.size() * 30), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), fillColor.getRGB());
            } else {
                int startY = this.getPosY() + this.tensorGUI.titleBoxHeight + 2 + (this.spammerFileObjects.size() * 30);

                this.startFlatGL(false);
                this.prepRectangle(fillColor.getRGB());

                this.genRectangleVertices(this.getPosX(), startY + 1, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight());

                this.swapRectangleColor(outLineColor.getRGB());
                this.genRectangleVertices(this.getPosX(), startY, this.getPosX() + this.getWidth(), startY + 1);

                this.finishRectangle();
                this.finishFlatGL(false);
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight, mouseX, mouseY)) {
            this.close();
        } else if (this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY)) {

            AtomicBoolean hasDispatched = new AtomicBoolean(false);

            this.spammerFileObjects.forEach(object -> {

                if (!hasDispatched.get() && this.isHovered(object.getPosX(), object.getPosY(), object.getPosX() + object.getWidth(), object.getPosY() + object.getHeight(), mouseX, mouseY)) {
                    object.onClick(mouseX, mouseY);

                }
            });
        }
    }

    @Override
    public boolean isSearching() {
        return false;
    }

    @Override
    public void setSearching(boolean searching) {

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
        this.scrollY.setValue(0);
        this.hoveredOnBack = false;
        this.tensorGUI.closeButton();
    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        this.hoveredOnBack = this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight, mouseX, mouseY);

        if (!this.hoveredOnBack && this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY)) {
            this.spammerFileObjects.forEach(object -> object.onHovered(mouseX, mouseY));
        } else {
            this.spammerFileObjects.forEach(object -> object.setHovered(false));
        }
    }

    @Override
    public void handleScroll(int scroll) {
        if (scroll == 0) {
            return;
        }

        int scrollAdjust = scroll;

        if (scrollY.getValue() > -this.tensorGUI.getScrollSpeed() && scroll > 0) {

            scrollAdjust = -scrollY.getValue();
        }

        int maxHeight = this.getHeight() - this.tensorGUI.titleBoxHeight - 2;

        if ((this.spammerFileObjects.size() * 30) + (scroll < 0 ? this.scrollY.getValue() - this.tensorGUI.getScrollSpeed() : 0) <= maxHeight) {
            int test = (((this.spammerFileObjects.size() * 30) + (scroll < 0 ? this.scrollY.getValue() : 0)) - maxHeight);

            if (test >= scrollAdjust) {
                scrollAdjust = -test;
            } else {
                return;
            }
        }

        this.scrollY.setValue(this.scrollY.getValue() + scrollAdjust);
    }

    @Override
    public void onKeyPressed(char letter, int key) {

    }

    public boolean isFileActive(String name) {
        return this.setting.getValue().isCurrentSpammer(name);
    }

    public void swapActiveFile(String name) {
        if (this.setting.getValue().isCurrentSpammer(name)) {
            this.setting.getValue().setCurrentSpammer(null, true);
        } else {
            this.setting.getValue().setCurrentSpammer(name, false);
        }
    }

    public void loadOrResetSpammerList() {
        this.spammerFileObjects.clear();

        AtomicInteger number = new AtomicInteger(0);

        this.setting.getValue().getFileNames().forEach(value -> this.spammerFileObjects.add(new SpammerFileObject(value, this, this.clickGUI.x, this.clickGUI.y, this.scrollY, this.tensorGUI.titleBoxHeight + 2, number.getAndAdd(1))));

    }
}
