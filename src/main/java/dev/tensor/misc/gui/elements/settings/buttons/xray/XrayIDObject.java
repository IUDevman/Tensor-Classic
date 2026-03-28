package dev.tensor.misc.gui.elements.settings.buttons.xray;

import dev.tensor.misc.gui.Element;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.elements.settings.buttons.BlockListSettingButton;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.Block;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

public final class XrayIDObject implements Element {


    public final int blockID;
    private final BlockListSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final IntegerSetting scrollY;
    private final int offSetX;
    private final int offSetY;

    public XrayIDObject(int tensorID, BlockListSettingButton button, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int offsetX, int offsetY, int row, int column) {
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
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), this.isEnabled() ? selectedFillColor.getRGB() : fillColor.getRGB());

        Block block = Block.blocksList[blockID];
        String name = "null";

        if (block != null) {
            name = String.valueOf(block.blockID);
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

    private boolean isEnabled() {
        return this.button.blockListSetting.containsBlock(blockID, false);
    }
}
