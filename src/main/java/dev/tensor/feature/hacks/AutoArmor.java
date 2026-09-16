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

        //yes I know we might want to swap if we are in the ClickGUI, but this makes sure we don't swap in containers
        if (this.getMinecraft().currentScreen != null) {
            return;
        }

        this.runAutoArmor(null);
    }

    private void runAutoArmor(ArrayList<ArmorSlot> slots) {
        ItemStack[] armor = this.getPlayer().inventory.armorInventory;

        ArrayList<ArmorSlot> slotsToSwap = new ArrayList<>();

        if (slots == null) {
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
        } else {
            slotsToSwap = slots;
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

        int slotToSwapTo = toSwap.findBestItemForSlot(this.getPlayer().inventory.mainInventory);

        if (slotToSwapTo == -1) {
            if (slotsToSwap.size() > 1) {
                slotsToSwap.remove(toSwap);
                this.runAutoArmor(slotsToSwap);
            }

            return;
        }

        this.swapArmor(toSwap.location, slotToSwapTo, swappingEmpty);

        this.delayCount = 0;
    }

    private void swapArmor(int armorSlot, int inventorySlot, boolean swappingEmpty) {
        //todo: code to swap

        if (swappingEmpty) {
            //click in main inventory
            //click in armor inventory
            //dome!
        } else {
            //click in armor inventory
            //click in main inventory
            //click in armor inventory
            //done!
        }
    }
}
