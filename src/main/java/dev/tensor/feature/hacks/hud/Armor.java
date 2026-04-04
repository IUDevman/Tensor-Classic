package dev.tensor.feature.hacks.hud;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.HUDObject;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.ItemStack;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-04-2026
 */

@Hack.Info(name = "Armor", category = Category.HUD)
public final class Armor extends Hack implements HUDObject {

    public final IntegerSetting x = new IntegerSetting("X", 200, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 200, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 2.0, 0.5, 3.0, 1);
    public final EnumSetting durability = new EnumSetting("Durability", Durability.Percentage);
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
        return (int) (18 * this.scale.getValue());
    }

    @Override
    public int getHeight() {
        return (int) (74 * this.scale.getValue());
    }

    @Override
    public void render() {
        this.drawRectangle(this.getMinX().getValue() + this.getScaledOutline(), this.getMinY().getValue() + this.getScaledOutline(), this.getMinX().getValue() + this.getWidth() - this.getScaledOutline(), this.getMinY().getValue() + this.getHeight() - this.getScaledOutline(), this.fillColor.getValue().getRGB());
        this.drawRectangle(this.getMinX().getValue(), this.getMinY().getValue(), this.getMinX().getValue() + this.getWidth(), this.getMinY().getValue() + this.getScaledOutline(), this.outlineColor.getValue().getRGB());
        this.drawRectangle(this.getMinX().getValue(), this.getMinY().getValue() + this.getHeight() - this.getScaledOutline(), this.getMinX().getValue() + this.getWidth(), this.getMinY().getValue() + this.getHeight(), this.outlineColor.getValue().getRGB());
        this.drawRectangle(this.getMinX().getValue(), this.getMinY().getValue() + this.getScaledOutline(), this.getMinX().getValue() + this.getScaledOutline(), this.getMinY().getValue() + this.getHeight() - this.getScaledOutline(), this.outlineColor.getValue().getRGB());
        this.drawRectangle(this.getMinX().getValue() + this.getWidth() - this.getScaledOutline(), this.getMinY().getValue() + this.getScaledOutline(), this.getMinX().getValue() + this.getWidth(), this.getMinY().getValue() + this.getHeight() - this.getScaledOutline(), this.outlineColor.getValue().getRGB());

        int itemStartX = this.getMinX().getValue() + this.getScaledOutline();

        ItemStack[] armor = this.getPlayer().inventory.armorInventory;
        for (int i = armor.length - 1; i >= 0; --i) {
            int itemStartY = this.getMinY().getValue() + this.getScaledOutline() + (this.getScaledItemWidth() * (3 - i));

            this.startItemGL();

            try {
                this.renderItem(armor[i], (int) (itemStartX - (1 * this.scale.getValue())), (int) (itemStartY + (3 * this.scale.getValue())), 1, 4, this.scale.getValue(), 0);

                switch ((Durability) this.durability.getValue()) {
                    case None:
                        break;
                    case Vanilla:
                        this.renderItemOverlay(armor[i], 0, 0, this.scale.getValue());
                        break;
                    case Percentage:
                        this.renderItemPercentage(armor[i], 14, 12);
                        break;
                }

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

    private enum Durability {
        None,
        Vanilla,
        Percentage
    }
}
