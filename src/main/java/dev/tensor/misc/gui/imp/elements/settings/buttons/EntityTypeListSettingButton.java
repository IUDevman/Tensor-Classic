package dev.tensor.misc.gui.imp.elements.settings.buttons;

import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.ButtonSetting;
import dev.tensor.misc.gui.imp.elements.HackElement;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.EntityTypeSettingObject;
import dev.tensor.misc.imp.setting.settings.EntityTypeListSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 03-31-2026
 */

public final class EntityTypeListSettingButton implements ButtonSetting {

    public final TensorGUI tensorGUI;
    private final ClickGUI clickGUI;
    private final HackElement hackElement;
    public final EntityTypeListSetting entityTypeListSetting;

    private boolean active;

    private final ArrayList<EntityTypeSettingObject> entityTypeObjects = new ArrayList<>();
    private final IntegerSetting scrollY = new IntegerSetting("EntityType Scroll Y", 0, -1000, 1000);

    public EntityTypeListSettingButton(TensorGUI tensorGUI, ClickGUI clickGUI, HackElement hackElement, EntityTypeListSetting entityTypeListSetting) {
        this.tensorGUI = tensorGUI;
        this.clickGUI = clickGUI;
        this.hackElement = hackElement;
        this.entityTypeListSetting = entityTypeListSetting;

        AtomicInteger number = new AtomicInteger(0);

        entityTypeListSetting.getFullValue().getEntityList().forEach(value -> this.entityTypeObjects.add(new EntityTypeSettingObject(value, this, this.clickGUI.x, this.clickGUI.y, this.scrollY, 5, this.tensorGUI.titleBoxHeight + 2, number.getAndAdd(1))));
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
        this.startFlatGL(false);
        this.prepRectangle(fillColor.getRGB());

        this.genRectangleVertices(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 102, this.getPosY() + this.tensorGUI.titleBoxHeight);
        this.genRectangleVertices(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + 5, this.getPosY() + this.getHeight());
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 5, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight());

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

        this.drawCenteredString(this.hackElement.hack.NAME + " : " + this.entityTypeListSetting.getName(), this.getPosX() + ((this.getWidth() - 102) / 2), this.getPosY() + 2 + ((this.tensorGUI.titleBoxHeight - 12) / 2), Color.WHITE.getRGB());
        this.drawCenteredString(">> Go Back <<", this.getPosX() + this.getWidth() - (100 / 2), this.getPosY() + 2 + ((this.tensorGUI.titleBoxHeight - 12) / 2), Color.YELLOW.getRGB());

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

        this.entityTypeObjects.forEach(entityTypeObject -> entityTypeObject.render(tensorGUI, fillColor, selectedFillColor, outLineColor));

        if ((this.entityTypeObjects.size() * 15) < this.getHeight() - this.tensorGUI.titleBoxHeight - 2) {
            this.drawRectangle(this.getPosX() + 5, this.getPosY() + this.tensorGUI.titleBoxHeight + 2 + (this.entityTypeObjects.size() * 15), this.getPosX() + this.getWidth() - 5, this.getPosY() + this.getHeight(), fillColor.getRGB());
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        this.hoveredOnBack = this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight, mouseX, mouseY);

        if (!this.hoveredOnBack && this.isHovered(this.getPosX() + 5, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth() - 7, this.getPosY() + this.tensorGUI.titleBoxHeight + (this.entityTypeObjects.size() * 15), mouseX, mouseY)) {
            this.entityTypeObjects.forEach(object -> object.onHovered(mouseX, mouseY));
        } else {
            this.entityTypeObjects.forEach(object -> object.setHovered(false));
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

        if ((this.entityTypeObjects.size() * 15) + (scroll < 0 ? this.scrollY.getValue() - this.tensorGUI.getScrollSpeed() : 0) <= maxHeight) {
            int test = (((this.entityTypeObjects.size() * 15) + (scroll < 0 ? this.scrollY.getValue() : 0)) - maxHeight);

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

            this.entityTypeObjects.forEach(entityTypeObject -> {

                if (!hasDispatched.get() && this.isHovered(entityTypeObject.getPosX(), entityTypeObject.getPosY(), entityTypeObject.getPosX() + entityTypeObject.getWidth(), entityTypeObject.getPosY() + entityTypeObject.getHeight(), mouseX, mouseY)) {
                    hasDispatched.set(true);
                    entityTypeObject.onClick(mouseX, mouseY);
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

    public void resetHover(EntityTypeSettingObject entityTypeObject) {

        this.hoveredOnBack = false;

        this.entityTypeObjects.forEach(entityTypeObject1 -> {

            if (entityTypeObject1.hovered() && entityTypeObject1 != entityTypeObject) {
                entityTypeObject1.setHovered(false);
            }
        });
    }
}
