package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.inventory.ArmorSlot;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.ItemStack;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 09-15-2026
 */

@Hack.Info(name = "AutoArmor", category = Category.Combat)
public final class AutoArmor extends Hack {

    public final IntegerSetting delay = new IntegerSetting("Delay", 2, 0, 10);

    private int delayCount = 0;

    @Override
    public void onEnable(boolean isNull) {
        this.delayCount = this.delay.getValue();
    }

    @Override
    public void onClientTick() {
        if (this.delayCount < this.delay.getValue()) {
            this.delayCount++;
            return;
        }

        this.delayCount = 0;

        ItemStack[] armor = this.getPlayer().inventory.armorInventory;

        ArrayList<ArmorSlot> slotsToSwap = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            ItemStack piece = armor[i];

            if (piece == null || piece.getItem() == null) {
                slotsToSwap.add(new ArmorSlot(i, null));
                continue;
            }

            if (!ArmorSlot.MAX_ITEMS().contains(piece.getItemName())) {
                slotsToSwap.add(new ArmorSlot(i, piece.getItem()));
            }
        }

        if (slotsToSwap.isEmpty()) {
            return;
        }

        boolean swappingEmpty = true;
        ArmorSlot toSwap = slotsToSwap.stream().filter(armorSlot -> armorSlot.item == null).findFirst().orElse(null);

        if (toSwap == null) {
            swappingEmpty = false;
            toSwap = slotsToSwap.get(0);
        }

        if (toSwap == null) {
            return;
        }

        int slotToSwapTo = toSwap.findBestItemForSlot(this.getPlayer().inventory.mainInventory);

        if (slotToSwapTo == -1) {
            return;
        }

        this.swapArmor(toSwap.location, slotToSwapTo, swappingEmpty);
    }

    private void swapArmor(int armorSlot, int inventorySlot, boolean swappingEmpty) {
        //todo: code to swap
    }
}
