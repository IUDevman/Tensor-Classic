package dev.tensor.misc.gui;

import dev.tensor.feature.hacks.HUDEditor;
import dev.tensor.misc.gui.imp.Hoverable;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.render.Render2D;
import net.minecraft.src.GuiScreen;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

public final class TensorHUDEditor extends GuiScreen implements Render2D, Hoverable {

    private final HUDEditor hudEditor;

    private final ArrayList<HUDObject> hudObjects = new ArrayList<>();

    private HUDObject draggedObject = null;

    private int clickX;
    private int clickY;

    private int newX;
    private int newY;

    public TensorHUDEditor(HUDEditor hudEditor) {

        this.hudEditor = hudEditor;

        this.getHackManager().getHacks().forEach(hack -> {

            if (hack instanceof HUDObject) {
                this.hudObjects.add((HUDObject) hack);
            }
        });
    }

    @Override
    public void drawScreen(int i, int j, float f) {

        if (this.draggedObject != null) {
            this.newX = i + clickX;
            this.newY = j + clickY;

            if (this.newX + this.draggedObject.getWidthScaled() <= this.getMinecraft().currentScreen.width && this.clickX != 0) {
                this.draggedObject.getMinX().setValue(newX);
            } else {
                this.newX = this.draggedObject.getMinX().getValue();
            }

            if (this.newY + this.draggedObject.getHeightScaled() <= this.getMinecraft().currentScreen.height && this.clickY != 0) {
                this.draggedObject.getMinY().setValue(newY);
            } else {
                this.newY = this.draggedObject.getMinY().getValue();
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
    }

    private void drawHudObjectOutline(HUDObject hudObject) {
        this.drawRectangle(hudObject.getMinX().getValue(), hudObject.getMinY().getValue(), hudObject.getMinX().getValue() + hudObject.getWidthScaled(), hudObject.getMinY().getValue() + 2, this.hudEditor.outlineColor.getValue().getRGB());
        this.drawRectangle(hudObject.getMinX().getValue(), hudObject.getMinY().getValue() + hudObject.getHeightScaled() - 2, hudObject.getMinX().getValue() + hudObject.getWidthScaled(), hudObject.getMinY().getValue() + hudObject.getHeightScaled(), this.hudEditor.outlineColor.getValue().getRGB());
        this.drawRectangle(hudObject.getMinX().getValue(), hudObject.getMinY().getValue() + 2, hudObject.getMinX().getValue() + 2, hudObject.getMinY().getValue() + hudObject.getHeightScaled() - 2, this.hudEditor.outlineColor.getValue().getRGB());
        this.drawRectangle(hudObject.getMinX().getValue() + hudObject.getWidthScaled() - 2, hudObject.getMinY().getValue() + 2, hudObject.getMinX().getValue() + hudObject.getWidthScaled(), hudObject.getMinY().getValue() + hudObject.getHeightScaled() - 2, this.hudEditor.outlineColor.getValue().getRGB());
    }

    @Override
    public void mouseClicked(int i, int j, int k) {

        if (k != 0) {
            return;
        }

        this.hudObjects.forEach(hudObject -> {

            boolean isHovered = this.isHovered(hudObject, i, j);

            if (isHovered) {
                if (this.draggedObject == hudObject) {
                    this.draggedObject = null;

                } else {
                    this.draggedObject = hudObject;

                    this.newX = hudObject.getMinX().getValue();
                    this.newY = hudObject.getMinY().getValue();

                    int newClickX = this.newX - i;
                    int newClickY = this.newY - j;

                    if (newClickX + hudObject.getMinX().getValue() <= 0) {
                        newClickX = this.newX;
                    }

                    if (newClickY + hudObject.getMinY().getValue() <= 0) {
                        newClickY = this.newY;
                    }

                    this.clickX = newClickX;
                    this.clickY = newClickY;
                }
            }
        });
    }

    @Override
    public void handleMouseInput() {

        if (Mouse.getEventButton() == 0) {
            this.draggedObject = null;
        }

        super.handleMouseInput();
    }

    private boolean isHovered(HUDObject hudObject, double mouseX, double mouseY) {
        return this.isHovered(hudObject.getMinX().getValue(), hudObject.getMinY().getValue(), hudObject.getMinX().getValue() + hudObject.getWidthScaled(), hudObject.getMinY().getValue() + hudObject.getHeightScaled(), mouseX, mouseY);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        this.hudEditor.setEnabled(false);
    }
}
