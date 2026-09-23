package dev.tensor.feature.hacks.hud;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.ItemStack;

import java.awt.*;
import java.util.HashMap;

/**
 * @author Hoosiers
 * @since 04-04-2026
 */

@Hack.Info(name = "Armor", category = Category.HUD, description = "Displays your current armor and it's percentages.")
public final class Armor extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Shading_and_Images;
    }

    public final IntegerSetting x = new IntegerSetting("X", 86, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 63, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 2.0, 0.5, 3.0, 1);
    public final EnumSetting durability = new EnumSetting("Durability", Durability.Percentage, new String[]{
            "This is how the armor's durability will be displayed.",
            "None® Durability is hidden.",
            "Vanilla® Renders the vanilla durability bar.",
            "Percentage® Renders durability as % remaining."
    });
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
        return (int) (18 * this.scale.getValue());
    }

    @Override
    public int getWidthScaled() {
        return this.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) (74 * this.scale.getValue());
    }

    @Override
    public int getHeightScaled() {
        return this.getHeight();
    }

    private final HashMap<Integer, ItemStack> itemsToRender = new HashMap<>();

    @Override
    public void onDisable(boolean isNull) {
        this.itemsToRender.clear();
    }

    @Override
    public void onClientTick() {
        this.itemsToRender.clear();

        ItemStack[] armor = this.getInventory().armorInventory;
        for (int i = armor.length - 1; i >= 0; --i) {
            ItemStack itemStack = armor[i];

            if (itemStack == null) {
                continue;
            }

            this.itemsToRender.put(i, itemStack);
        }
    }

    @Override
    public void render() {
        this.drawOutlinedRectangle(this.getMinX().getValue(), this.getMinY().getValue(), this.getWidth(), this.getHeight(), this.getScaledOutline(), this.fillColor.getValueRainbow().getRGB(), this.outlineColor.getValueRainbow().getRGB(), true);

        if (this.itemsToRender.isEmpty()) {
            return;
        }

        int itemStartX = this.getMinX().getValue() + this.getScaledOutline();

        this.itemsToRender.forEach((integer, itemStack) -> {

            int itemStartY = this.getMinY().getValue() + this.getScaledOutline() + (this.getScaledItemWidth() * (3 - integer));

            this.startItemGL();

            try {
                this.renderItem(itemStack, (int) (itemStartX - (1 * this.scale.getValue())), (int) (itemStartY + (3 * this.scale.getValue())), 1, 4, this.scale.getValue(), 0);

                switch ((Durability) this.durability.getValue()) {
                    case None:
                        break;
                    case Vanilla:
                        this.renderItemOverlay(itemStack, 0, 0, this.scale.getValue());
                        break;
                    case Percentage:
                        this.renderItemPercentage(itemStack, 14, 12);
                        break;
                }

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

    private enum Durability {
        None,
        Vanilla,
        Percentage
    }
}
