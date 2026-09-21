package dev.tensor.misc.gui;

import dev.tensor.feature.hacks.HUDEditor;
import dev.tensor.misc.gui.imp.Hoverable;
import dev.tensor.misc.gui.imp.description.GUIDescription;
import dev.tensor.misc.gui.imp.description.imp.HackDescription;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.render.Render2D;
import net.minecraft.src.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

public final class TensorHUDEditor extends GuiScreen implements Render2D, Hoverable {

    private final HUDEditor hudEditor;

    private final ArrayList<HUDObject> hudObjects = new ArrayList<>();

    private HUDObject draggingObject = null;

    private GUIDescription description = null;

    private int dragOffsetX;
    private int dragOffsetY;

    private int dragXToSet;
    private int dragYToSet;

    public TensorHUDEditor(HUDEditor hudEditor) {

        this.hudEditor = hudEditor;

        this.getHackManager().getHacks().forEach(hack -> {

            if (hack instanceof HUDObject) {
                this.hudObjects.add((HUDObject) hack);
            }
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (this.draggingObject != null) {
            this.dragXToSet = mouseX + dragOffsetX;
            this.dragYToSet = mouseY + dragOffsetY;

            if (this.dragXToSet + this.draggingObject.getWidthScaled() <= this.getMinecraft().currentScreen.width && this.dragOffsetX != 0) {
                this.draggingObject.getMinX().setValue(dragXToSet);
            } else {
                this.dragXToSet = this.draggingObject.getMinX().getValue();
            }

            if (this.dragYToSet + this.draggingObject.getHeightScaled() <= this.getMinecraft().currentScreen.height && this.dragOffsetY != 0) {
                this.draggingObject.getMinY().setValue(dragYToSet);
            } else {
                this.dragYToSet = this.draggingObject.getMinY().getValue();
            }
        }

        this.hudObjects.forEach(hudObject -> {

            switch ((HUDEditor.RenderType) this.hudEditor.render.getValue()) {
                case Outline:
                    this.drawHudObjectOutline(hudObject);
                    break;
                case Object:
                    hudObject.render();
                    break;
                case Both:
                    hudObject.render();
                    this.drawHudObjectOutline(hudObject);
                    break;
            }
        });

        boolean holdingShift = this.shouldRenderDescription();

        if (!holdingShift) {
            this.description = null;
            return;
        }

        AtomicBoolean hasFound = new AtomicBoolean(false);

        this.hudObjects.forEach(hudObject -> {

            if (hasFound.get()) {
                return;
            }

            if (!(hudObject instanceof Hack)) {
                return;
            }

            Hack hack = (Hack) hudObject;

            if (this.isObjectHovered(hudObject, mouseX, mouseY)) {

                if (this.description == null || !(this.description instanceof HackDescription)) {
                    this.description = new HackDescription(hack);

                } else if (this.description instanceof HackDescription) {
                    HackDescription hackDescription = (HackDescription) this.description;

                    Hack hackDesc = (Hack) hackDescription.getObject();

                    if (hackDesc != hack) {
                        this.description = new HackDescription(hack);
                    }
                }

                hasFound.set(true);
            }
        });

        if (!hasFound.get()) {
            this.description = null;
        }

        if (this.description != null) {
            this.description.render(mouseX, mouseY, this.hudEditor.fillColor.getValueRainbow(), this.hudEditor.outlineColor.getValueRainbow());
        }
    }

    private void drawHudObjectOutline(HUDObject hudObject) {
        this.drawOutlinedRectangle(hudObject.getMinX().getValue(), hudObject.getMinY().getValue(), hudObject.getWidthScaled(), hudObject.getHeightScaled(), 2, 0, this.hudEditor.outlineColor.getValueRainbow().getRGB(), false);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if (mouseButton != 0) {
            return;
        }

        this.hudObjects.forEach(hudObject -> {

            boolean isHovered = this.isObjectHovered(hudObject, mouseX, mouseY);

            if (isHovered) {
                if (this.draggingObject == hudObject) {
                    this.draggingObject = null;

                } else {
                    this.draggingObject = hudObject;

                    this.dragXToSet = hudObject.getMinX().getValue();
                    this.dragYToSet = hudObject.getMinY().getValue();

                    int newDragOffsetX = this.dragXToSet - mouseX;
                    int newDragOffsetY = this.dragYToSet - mouseY;

                    if (newDragOffsetX + hudObject.getMinX().getValue() + hudObject.getWidth() <= 0) {
                        newDragOffsetX = this.dragXToSet;
                    }

                    if (newDragOffsetY + hudObject.getMinY().getValue() + hudObject.getHeight() <= 0) {
                        newDragOffsetY = this.dragYToSet;
                    }

                    this.dragOffsetX = newDragOffsetX;
                    this.dragOffsetY = newDragOffsetY;
                }
            }
        });
    }

    @Override
    public void handleMouseInput() {

        if (Mouse.getEventButton() == 0) {
            this.draggingObject = null;
        }

        super.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        this.hudEditor.setEnabled(false);
    }

    private boolean isObjectHovered(HUDObject hudObject, double mouseX, double mouseY) {
        return this.isHovered(hudObject.getMinX().getValue(), hudObject.getMinY().getValue(), hudObject.getMinX().getValue() + hudObject.getWidthScaled(), hudObject.getMinY().getValue() + hudObject.getHeightScaled(), mouseX, mouseY);
    }

    private boolean shouldRenderDescription() {
        if (!this.hudEditor.showDescriptions.getValue()) {
            return false;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            return true;
        }

        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }
}
