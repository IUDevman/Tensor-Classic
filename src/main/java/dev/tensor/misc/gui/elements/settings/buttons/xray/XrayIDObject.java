package dev.tensor.misc.gui.elements.settings.buttons.xray;

import dev.tensor.misc.gui.Element;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.elements.settings.buttons.BlockListSettingButton;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

public final class XrayIDObject implements Element {

    private final int GL_WTF_IS_THIS = 32826;

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

            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(this.GL_WTF_IS_THIS);

            RenderHelper.enableStandardItemLighting();

            GL11.glDepthMask(true);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            try{
                this.renderItem(itemStack, this.getPosX() + 1, this.getPosY() + 11);

            } catch(Exception ignored) {
            }

            RenderHelper.disableStandardItemLighting();

            GL11.glDisable(this.GL_WTF_IS_THIS);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
        }

        this.drawString(name, this.getPosX() + 2, this.getPosY() + 2, Color.WHITE.getRGB());
    }

    RenderItem renderItem = new RenderItem();
    RenderBlocks renderBlocks = new RenderBlocks();

    private void renderItem(ItemStack itemStack, int x, int y) {
        int itemID = itemStack.itemID;
        int itemIcon = itemStack.getIconIndex();

        if (itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemID].getRenderType())) {
            this.getRenderEngine().bindTexture(this.getRenderEngine().getTexture("/terrain.png"));
            Block var14 = Block.blocksList[itemID];
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(x), (float)(y), -3.0F);
            GL11.glScalef(20.0F, 20.0F, 20.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);

            int var15 = Item.itemsList[itemID].getColorFromDamage(itemStack.getItemDamage());
            float var16 = (float)(var15 >> 16 & 255) / 255.0F;
            float var12 = (float)(var15 >> 8 & 255) / 255.0F;
            float var13 = (float)(var15 & 255) / 255.0F;
            if (renderItem.field_27004_a) {
                GL11.glColor4f(var16, var12, var13, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            this.renderBlocks.field_31088_b = renderItem.field_27004_a;
            this.renderBlocks.renderBlockOnInventory(var14, itemIcon, 1.0F);
            this.renderBlocks.field_31088_b = true;
            GL11.glPopMatrix();

        } else if (itemIcon >= 0) {

            GL11.glTranslatef((float)(x + 6), (float)(y - 4), -3.0F);
            GL11.glScalef(1.75F, 1.75F, 0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            if (itemID < 256) {
                this.getRenderEngine().bindTexture(this.getRenderEngine().getTexture("/terrain.png"));
            } else {
                this.getRenderEngine().bindTexture(this.getRenderEngine().getTexture("/gui/items.png"));
            }

            int var8 = Item.itemsList[itemID].getColorFromDamage(itemStack.getItemDamage());
            float var9 = (float)(var8 >> 16 & 255) / 255.0F;
            float var10 = (float)(var8 >> 8 & 255) / 255.0F;
            float var11 = (float)(var8 & 255) / 255.0F;
            if (renderItem.field_27004_a) {
                GL11.glColor4f(var9, var10, var11, 1.0F);
            }

            renderItem.renderTexturedQuad(0, 0, itemIcon % 16 * 16, itemIcon / 16 * 16, 16, 16);
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
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
        return this.button.blockListSetting.containsBlock(blockID);
    }
}
