package dev.tensor.feature.hacks.hud;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.ItemStack;

import java.awt.*;
import java.util.HashMap;

/**
 * @author Hoosiers
 * @since 04-03-2026
 */

@Hack.Info(name = "Inventory", category = Category.HUD, description = "Displays the items in your inventory.")
public final class Inventory extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Shading_and_Images;
    }

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 314, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 1.0, 0.5, 3.0, 1);
    public final ColorSetting fillColor = new ColorSetting("Fill Color", new Color(0, 0, 0, 150));
    public final ColorSetting outlineColor = new ColorSetting("Outline Color", new Color(62, 11, 10, 200));

    @Override
    public IntegerSetting getMinX() {
        return this.x;
    }

    @Override
    public IntegerSetting getMinY() {
        return this.y;
    }

    @Deprecated
    @Override
    public DoubleSetting getScale() {
        return this.scale;
    }

    @Override
    public int getWidth() {
        return (int) (162 * this.scale.getValue());
    }

    @Override
    public int getWidthScaled() {
        return this.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) (54 * this.scale.getValue());
    }

    @Override
    public int getHeightScaled() {
        return this.getHeight();
    }

    private final HashMap<Integer, ItemStack> itemsToRender = new HashMap<>();

    @Override
    public void onClientTick() {
        this.itemsToRender.clear();

        ItemStack[] items = this.getInventory().mainInventory;
        for (int size = items.length, item = 9; item < size; ++item) {
            ItemStack itemStack = items[item];

            if (itemStack == null) {
                continue;
            }

            this.itemsToRender.put(item, itemStack);
        }
    }

    @Override
    public void render() {
        this.drawOutlinedRectangle(this.getMinX().getValue(), this.getMinY().getValue(), this.getWidth(), this.getHeight(), this.getScaledOutline(), this.fillColor.getValueRainbow().getRGB(), this.outlineColor.getValueRainbow().getRGB(), true);

        if (this.itemsToRender.isEmpty()) {
            return;
        }

        this.itemsToRender.forEach((integer, itemStack) -> {

            int itemStartX = this.getMinX().getValue() + this.getScaledOutline() + integer % 9 * this.getScaledItemWidth();
            int itemStartY = this.getMinY().getValue() + this.getScaledOutline() + (integer / 9 - 1) * this.getScaledItemWidth();

            this.startItemGL();

            try {
                this.renderItem(itemStack, (int) (itemStartX - (1 * this.scale.getValue())), (int) (itemStartY + (3 * this.scale.getValue())), 1, 4, this.scale.getValue(), 0);
                this.renderItemOverlay(itemStack, 0, 0, this.scale.getValue());

            } catch (Exception ignored) {
            }

            this.finishItemGL();
        });
    }

    private int getScaledOutline() {
        return (int) (1 * this.scale.getValue());
    }

    private int getScaledItemWidth() {
        return (int) (18 * this.scale.getValue());
    }
}
