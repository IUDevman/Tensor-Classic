package dev.tensor.misc.gui;

import dev.tensor.feature.hacks.HUDEditor;
import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.hack.HUDObject;
import net.minecraft.src.GuiScreen;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

public final class TensorHUDEditor extends GuiScreen implements Globals {

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

            if (this.newX + this.draggedObject.getWidth() <= this.getMinecraft().currentScreen.width) {
                this.draggedObject.getMinX().setValue(newX);
            }

            if (this.newY + this.draggedObject.getHeight() <= this.getMinecraft().currentScreen.height) {
                this.draggedObject.getMinY().setValue(newY);
            }
        }

        this.hudObjects.forEach(hudObject -> {

            if (this.hudEditor.renderObject.getValue()) {
                hudObject.render();
            }

            if (this.hudEditor.drawOutline.getValue()) {
                this.drawRectangle(hudObject.getMinX().getValue(), hudObject.getMinY().getValue(), hudObject.getMinX().getValue() + hudObject.getWidth(), hudObject.getMinY().getValue() + 2, this.hudEditor.outlineColor.getValue().getRGB());
                this.drawRectangle(hudObject.getMinX().getValue(), hudObject.getMinY().getValue() + hudObject.getHeight() - 2, hudObject.getMinX().getValue() + hudObject.getWidth(), hudObject.getMinY().getValue() + hudObject.getHeight(), this.hudEditor.outlineColor.getValue().getRGB());
                this.drawRectangle(hudObject.getMinX().getValue(), hudObject.getMinY().getValue() + 2, hudObject.getMinX().getValue() + 2, hudObject.getMinY().getValue() + hudObject.getHeight() - 2, this.hudEditor.outlineColor.getValue().getRGB());
                this.drawRectangle(hudObject.getMinX().getValue() + hudObject.getWidth() - 2, hudObject.getMinY().getValue() + 2, hudObject.getMinX().getValue() + hudObject.getWidth(), hudObject.getMinY().getValue() + hudObject.getHeight() - 2, this.hudEditor.outlineColor.getValue().getRGB());
            }
        });
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
                    this.clickX = this.newX - i;
                    this.clickY = this.newY - j;
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
        return this.isHovered(hudObject.getMinX().getValue(), hudObject.getMinY().getValue(), hudObject.getMinX().getValue() + hudObject.getWidth(), hudObject.getMinY().getValue() + hudObject.getHeight(), mouseX, mouseY);
    }

    private boolean isHovered(int minX, int minY, int maxX, int maxY, double mouseX, double mouseY) {
        if (mouseX < minX || mouseX > maxX) {
            return false;
        }

        else return !(mouseY < minY) && !(mouseY > maxY);
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
