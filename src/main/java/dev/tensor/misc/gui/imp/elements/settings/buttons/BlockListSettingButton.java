package dev.tensor.misc.gui.imp.elements.settings.buttons;

import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.ButtonSetting;
import dev.tensor.misc.gui.imp.elements.HackElement;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.BlockIDSettingObject;
import dev.tensor.misc.imp.setting.settings.BlockListSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Hoosiers
 * @since 03-27-2026
 */

public final class BlockListSettingButton implements ButtonSetting {

    private final TensorGUI tensorGUI;
    private final ClickGUI clickGUI;
    private final HackElement hackElement;
    public final BlockListSetting blockListSetting;

    private boolean active;

    private final ArrayList<BlockIDSettingObject> blockIDObjects = new ArrayList<>();
    private final IntegerSetting scrollY = new IntegerSetting("Xray Scroll Y", 0, -1000, 1000);

    public BlockListSettingButton(TensorGUI tensorGUI, ClickGUI clickGUI, HackElement hackElement, BlockListSetting blockListSetting) {
        this.tensorGUI = tensorGUI;
        this.clickGUI = clickGUI;
        this.hackElement = hackElement;
        this.blockListSetting = blockListSetting;

        int numberWidth = 9;
        int blockSideAdjust = 11;

        for (int i = 1; i < numberWidth + 1; i++) {
            for (int ix = 0; ix < blockSideAdjust; ix++) {

                int number = i + (ix * 9);
                this.blockIDObjects.add(new BlockIDSettingObject(number, this, this.clickGUI.x, this.clickGUI.y, this.scrollY, 5, this.tensorGUI.titleBoxHeight + 2, i, ix));
            }
        }
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

    private boolean hoveredOnBack = false;

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 102, this.getPosY() + this.tensorGUI.titleBoxHeight, fillColor.getRGB());
        this.drawRectangle(this.getPosX() - 2, this.getPosY() - 2, this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth(), this.getPosY() - 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() - 2, this.getPosX() + this.getWidth(), this.getPosY(), outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, outLineColor.getRGB());
        this.drawCenteredString(this.hackElement.hack.NAME + " BlockList : Custom", this.getPosX() + ((this.getWidth() - 102) / 2), this.getPosY() + 2 + ((this.tensorGUI.titleBoxHeight - 12) / 2), Color.WHITE.getRGB());

        this.drawRectangle(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight, this.hoveredOnBack ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 102, this.getPosY(), this.getPosX() + this.getWidth() - 100, this.getPosY() + this.tensorGUI.titleBoxHeight, outLineColor.getRGB());
        this.drawCenteredString(">> Go Back <<", this.getPosX() + this.getWidth() - (100 / 2), this.getPosY() + 2 + ((this.tensorGUI.titleBoxHeight - 12) / 2), Color.YELLOW.getRGB());

        this.drawRectangle(this.getPosX() - 2, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX(), this.getPosY() + this.getHeight() + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.getHeight() + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth(), this.tensorGUI.titleBoxHeight + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() + 2, outLineColor.getRGB());

        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + 5, this.getPosY() + this.getHeight(), fillColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 5, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), fillColor.getRGB());

        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        int posX = this.getPosX() + 5;
        int posY = this.getPosY() + this.tensorGUI.titleBoxHeight + 2;
        int width1 = this.getWidth() - 7;
        int height1 = this.getHeight() - this.tensorGUI.titleBoxHeight - 2;

        int windowHeight = this.getMinecraft().displayHeight;
        double scaleX = (double) this.getMinecraft().displayWidth / (double) this.getMinecraft().currentScreen.width;
        double scaleY = (double) this.getMinecraft().displayHeight / (double) this.getMinecraft().currentScreen.height;
        int widthScaled = (int) (scaleX * width1);
        int heightScaled = (int) (scaleY * height1);
        int xScaled = (int) (posX * scaleX);
        int yScaled = (int) (posY * scaleY);

        GL11.glScissor(xScaled, windowHeight - yScaled - heightScaled, widthScaled, heightScaled);

        this.blockIDObjects.forEach(blockIDObject -> blockIDObject.render(tensorGUI, fillColor, selectedFillColor, new Color(0, 255, 0, fillColor.getAlpha())));

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        this.hoveredOnBack = this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight, mouseX, mouseY);

        if (!this.hoveredOnBack && this.isHovered(this.getPosX() + 5, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth() - 7, this.getPosY() + this.getHeight() - 2, mouseX, mouseY)) {
            this.blockIDObjects.forEach(object -> object.onHovered(mouseX, mouseY));
        } else {
            this.blockIDObjects.forEach(object -> object.setHovered(false));
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

        if (462 + (scroll < 0 ? this.scrollY.getValue() - this.tensorGUI.getScrollSpeed() : 0) <= maxHeight) {
            int test = ((462 + (scroll < 0 ? this.scrollY.getValue() : 0)) - maxHeight);

            if (test >= scrollAdjust) {
                scrollAdjust = -test;
            } else {
                return;
            }
        }

        this.scrollY.setValue(this.scrollY.getValue() + scrollAdjust);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight, mouseX, mouseY)) {
            this.close();
        }

        if (this.isHovered(this.getPosX() + 5, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth() - 7, this.getPosY() + this.getHeight() - 2, mouseX, mouseY)) {

            AtomicBoolean hasDispatched = new AtomicBoolean(false);

            this.blockIDObjects.forEach(blockIDObject -> {

                if (!hasDispatched.get() && this.isHovered(blockIDObject.getPosX(), blockIDObject.getPosY(), blockIDObject.getPosX() + blockIDObject.getWidth(), blockIDObject.getPosY() + blockIDObject.getHeight(), mouseX, mouseY)) {
                    hasDispatched.set(true);
                    blockIDObject.onClick(mouseX, mouseY);
                }
            });
        }
    }

    @Override
    public void onKeyPressed(char letter, int key) {

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
        this.setSearching(false);
        this.scrollY.setValue(0);
        this.hoveredOnBack = false;
        this.tensorGUI.closeButton();
    }

    public void resetHover(BlockIDSettingObject blockIDObject) {

        this.hoveredOnBack = false;

        this.blockIDObjects.forEach(blockIDObject1 -> {

            if (blockIDObject1.hovered() && blockIDObject1 != blockIDObject) {
                blockIDObject1.setHovered(false);
            }
        });
    }
}
