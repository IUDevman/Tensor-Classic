package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.settings.buttons.BlockListSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

public final class BlockIDSettingObject implements ButtonSettingObject {

    public final int blockID;
    private final BlockListSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final IntegerSetting scrollY;
    private final int offSetX;
    private final int offSetY;

    private boolean hovered = false;

    public BlockIDSettingObject(int tensorID, BlockListSettingButton button, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int offsetX, int offsetY, int row, int column) {
        this.blockID = tensorID;
        this.button = button;
        this.x = x;
        this.y = y;
        this.scrollY = scrollY;

        this.offSetX = ((row - 1) * this.getWidth()) + offsetX;
        this.offSetY = (column * this.getWidth()) + offsetY;
    }

    @Override
    public int getWidth() {
        return 42;
    }

    @Override
    public int getHeight() {
        return 42;
    }

    @Override
    public int getPosX() {
        return this.x.getValue() + this.offSetX;
    }

    @Override
    public int getPosY() {
        return this.y.getValue() + this.scrollY.getValue() + this.offSetY;
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {

        if (this.hovered) {
            fillColor = selectedFillColor;
            outLineColor = outLineColor.darker();
        }

        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), this.isEnabled() ? outLineColor.getRGB() : fillColor.getRGB());

        if (blockID >= Block.blocksList.length) {
            return;
        }

        Block block = Block.blocksList[blockID];
        String name = "";

        if (block != null) {
            name = String.valueOf(block.blockID);
        }

        Block block1 = Block.blocksList[this.blockID];

        if (block1 != null) {

            ItemStack itemStack = new ItemStack(Block.blocksList[this.blockID]);

            this.startItemGL();

            try {
                this.renderItem(itemStack, this.getPosX() + 1, this.getPosY() + 11, 3, 2, 2D, 0.25D);

            } catch (Exception ignored) {
            }

            this.finishItemGL();
        }

        this.drawString(name, this.getPosX() + 2, this.getPosY() + 2, Color.WHITE.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.isEnabled()) {
            this.button.blockListSetting.removeBlock(blockID);
        } else {
            this.button.blockListSetting.addBlock(blockID);
        }
    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        if (this.isHovered(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY) && !this.hovered) {
            this.hovered = true;
            this.button.resetHover(this);
        }
    }

    @Override
    public boolean hovered() {
        return this.hovered;
    }

    @Override
    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    private boolean isEnabled() {
        return this.button.blockListSetting.containsBlock(blockID);
    }
}
