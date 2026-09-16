package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.inventory.ArmorSlot;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet102WindowClick;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 09-15-2026
 */

//todo: Add a system to replace max armor based on durability (toggleable slider, but lets say swap at > 10% diff)
@Hack.Info(name = "AutoArmor", category = Category.Combat)
public final class AutoArmor extends Hack {

    public final IntegerSetting delay = new IntegerSetting("Tick Delay", 2, 0, 10);

    private int delayCount = 0;

    @Override
    public void onEnable(boolean isNull) {
        this.delayCount = this.delay.getValue();
    }

    @Override
    public void onClientTick() {
        if (!this.isMultiplayer()) {
            return;
        }

        if (this.delayCount < this.delay.getValue()) {
            this.delayCount++;
            return;
        }

        if (this.getMinecraft().currentScreen != null) {
            return;
        }

        if (this.getPlayer().isDead) {
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
        ItemStack armorStack = this.getPlayer().inventory.armorInventory[armorSlot];
        ItemStack inventoryStack = this.getPlayer().inventory.mainInventory[inventorySlot];

        Packet102WindowClick clickInv = new Packet102WindowClick(0, inventorySlot < 9 ? inventorySlot + 36 : inventorySlot, 0, false, inventoryStack, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
        this.sendPacket(clickInv);
        this.getPlayer().inventory.mainInventory[inventorySlot] = null;

        Packet102WindowClick clickArm = new Packet102WindowClick(0, 8 - armorSlot, 0, false, armorStack, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
        this.sendPacket(clickArm);
        this.getPlayer().inventory.armorInventory[armorSlot] = inventoryStack;

        if (!swappingEmpty && armorStack != null) {
            Packet102WindowClick clickBack = new Packet102WindowClick(0, inventorySlot < 9 ? inventorySlot + 36 : inventorySlot, 0, false, armorStack, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
            this.sendPacket(clickBack);
            this.getPlayer().inventory.mainInventory[inventorySlot] = armorStack;
        }
    }
}
