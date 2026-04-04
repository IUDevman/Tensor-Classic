package dev.tensor.feature.hacks.hud;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.HUDObject;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.ItemStack;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-03-2026
 */

@Hack.Info(name = "Inventory", category = Category.HUD)
public final class Inventory extends Hack implements HUDObject {

    public final IntegerSetting x = new IntegerSetting("X", 200, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 0, 0, this.getMaxY());
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

    @Override
    public int getWidth() {
        return (int) (162 * this.scale.getValue());
    }

    @Override
    public int getHeight() {
        return (int) (54 * this.scale.getValue());
    }

    @Override
    public void render() {
        this.drawRectangle(this.getMinX().getValue() + this.getScaledOutline(), this.getMinY().getValue() + this.getScaledOutline(), this.getMinX().getValue() + this.getWidth() - this.getScaledOutline(), this.getMinY().getValue() + this.getHeight() - this.getScaledOutline(), this.fillColor.getValue().getRGB());
        this.drawRectangle(this.getMinX().getValue(), this.getMinY().getValue(), this.getMinX().getValue() + this.getWidth(), this.getMinY().getValue() + this.getScaledOutline(), this.outlineColor.getValue().getRGB());
        this.drawRectangle(this.getMinX().getValue(), this.getMinY().getValue() + this.getHeight() - this.getScaledOutline(), this.getMinX().getValue() + this.getWidth(), this.getMinY().getValue() + this.getHeight(), this.outlineColor.getValue().getRGB());
        this.drawRectangle(this.getMinX().getValue(), this.getMinY().getValue() + this.getScaledOutline(), this.getMinX().getValue() + this.getScaledOutline(), this.getMinY().getValue() + this.getHeight() - this.getScaledOutline(), this.outlineColor.getValue().getRGB());
        this.drawRectangle(this.getMinX().getValue() + this.getWidth() - this.getScaledOutline(), this.getMinY().getValue() + this.getScaledOutline(), this.getMinX().getValue() + this.getWidth(), this.getMinY().getValue() + this.getHeight() - this.getScaledOutline(), this.outlineColor.getValue().getRGB());

        ItemStack[] items = this.getPlayer().inventory.mainInventory;
        for (int size = items.length, item = 9; item < size; ++item) {
            int itemStartX = this.getMinX().getValue() + this.getScaledOutline() + item % 9 * this.getScaledItemWidth();
            int itemStartY = this.getMinY().getValue() + this.getScaledOutline() + (item / 9 - 1) * this.getScaledItemWidth();

            this.startItemGL();

            try {
                this.renderItem(items[item], (int) (itemStartX - (2 * this.scale.getValue())), (int) (itemStartY + (3 * this.scale.getValue())), 1, 4, this.scale.getValue(), 0);
                this.renderItemOverlay(items[item], 0, 0, this.scale.getValue());

            } catch (Exception ignored) {
            }

            this.finishItemGL();
        }

    }

    private int getScaledOutline() {
        return (int) (1 * this.scale.getValue());
    }

    private int getScaledItemWidth() {
        return (int) (18 * this.scale.getValue());
    }
}
