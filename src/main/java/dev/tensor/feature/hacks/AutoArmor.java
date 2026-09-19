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

@Hack.Info(name = "AutoArmor", category = Category.Combat)
public final class AutoArmor extends Hack {

    /**
     * OK, so a bit on how this actually works:
     * This hack first collects all armor slots that could be filled with better protection (empty or damaged).
     * This hack will naturally first try to fill empty slots.
     * If no armor is found, it will then try to swap the damaged.
     * Beta 1.7.3 calculates protection based on armor damage %.
     * So a leather helmet with full durability will give more than a diamond with half durability.
     * This hack swaps your armor with armor that has a higher % remaining.
     * This is adjusted with the 'Damage % Diff' setting.
     * Default is 10% difference, but each % damage will drop your protection.
     * This value keeps it from swapping too much.
     */

    public final IntegerSetting diffPercent = new IntegerSetting("Damage % Diff", 10, 1, 50);
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

                if (this.shouldAddDamaged(piece, i)) {
                    slotsToSwap.add(new ArmorSlot(i, piece));
                }
            }
        } else {
            slotsToSwap = slots;
        }

        if (slotsToSwap.isEmpty()) {
            return;
        }

        ArmorSlot toSwap = slotsToSwap.stream().filter(armorSlot -> armorSlot.item == null).findFirst().orElse(null);

        if (toSwap == null) {
            toSwap = slotsToSwap.get(0);
        }

        int slotToSwapTo = toSwap.findBestItemForSlot(this.getPlayer().inventory.mainInventory, this.diffPercent.getValue());

        if (slotToSwapTo == -1) {
            if (slotsToSwap.size() > 1) {
                slotsToSwap.remove(toSwap);
                this.runAutoArmor(slotsToSwap);
            }

            return;
        }

        this.swapArmor(toSwap.location, slotToSwapTo);

        this.delayCount = 0;
    }

    private boolean shouldAddDamaged(ItemStack itemStack, int slot) {
        if (!itemStack.isItemStackDamageable()) {
            return false;
        }

        if (!itemStack.isItemDamaged()) {
            return false;
        }

        switch (slot) {
            case 0:
                return ArmorSlot.BOOTS().contains(itemStack.itemID);
            case 1:
                return ArmorSlot.PANTS().contains(itemStack.itemID);
            case 2:
                return ArmorSlot.CHESTS().contains(itemStack.itemID);
            case 3:
                return ArmorSlot.HELMETS().contains(itemStack.itemID);
            default:
                return false;
        }
    }

    private void swapArmor(int armorSlot, int inventorySlot) {
        ItemStack armorStack = this.getPlayer().inventory.armorInventory[armorSlot];
        ItemStack inventoryStack = this.getPlayer().inventory.mainInventory[inventorySlot];

        if (armorStack != null && inventoryStack != null && armorStack.itemID == inventoryStack.itemID) {
            int freeSlot = this.getFreeSlot();

            if (freeSlot == -1) {
                int toSwapSpot = this.getSwappableSlot(armorStack);

                if (toSwapSpot == -1) {
                    return;
                }

                ItemStack toSwapStack = this.getPlayer().inventory.mainInventory[toSwapSpot];

                //Swap armor of same ID but without free spaces in the inventory
                Packet102WindowClick clickArm = new Packet102WindowClick(0, 8 - armorSlot, 0, false, armorStack, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
                this.sendPacket(clickArm);
                this.getPlayer().inventory.armorInventory[armorSlot] = null;

                Packet102WindowClick clickSwap = new Packet102WindowClick(0, toSwapSpot < 9 ? toSwapSpot + 36 : toSwapSpot, 0, false, toSwapStack, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
                this.sendPacket(clickSwap);
                this.getPlayer().inventory.mainInventory[toSwapSpot] = armorStack;

                Packet102WindowClick clickInv = new Packet102WindowClick(0, inventorySlot < 9 ? inventorySlot + 36 : inventorySlot, 0, false, inventoryStack, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
                this.sendPacket(clickInv);
                this.getPlayer().inventory.mainInventory[inventorySlot] = toSwapStack;

                Packet102WindowClick clickArm2 = new Packet102WindowClick(0, 8 - armorSlot, 0, false, null, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
                this.sendPacket(clickArm2);
                this.getPlayer().inventory.armorInventory[armorSlot] = inventoryStack;

                return;
            }

            //Swap armor of same ID but free spaces are in the inventory
            Packet102WindowClick clickArm = new Packet102WindowClick(0, 8 - armorSlot, 0, false, armorStack, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
            this.sendPacket(clickArm);
            this.getPlayer().inventory.armorInventory[armorSlot] = null;

            Packet102WindowClick clickFree = new Packet102WindowClick(0, freeSlot < 9 ? freeSlot + 36 : freeSlot, 0, false, armorStack, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
            this.sendPacket(clickFree);
            this.getPlayer().inventory.mainInventory[freeSlot] = armorStack;

            Packet102WindowClick clickInv = new Packet102WindowClick(0, inventorySlot < 9 ? inventorySlot + 36 : inventorySlot, 0, false, inventoryStack, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
            this.sendPacket(clickInv);
            this.getPlayer().inventory.mainInventory[inventorySlot] = null;

            Packet102WindowClick clickArm2 = new Packet102WindowClick(0, 8 - armorSlot, 0, false, null, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
            this.sendPacket(clickArm2);
            this.getPlayer().inventory.armorInventory[armorSlot] = inventoryStack;

            return;
        }

        //Add armor to empty slot or swap with different ID
        Packet102WindowClick clickInv = new Packet102WindowClick(0, inventorySlot < 9 ? inventorySlot + 36 : inventorySlot, 0, false, inventoryStack, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
        this.sendPacket(clickInv);
        this.getPlayer().inventory.mainInventory[inventorySlot] = null;

        Packet102WindowClick clickArm = new Packet102WindowClick(0, 8 - armorSlot, 0, false, armorStack, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
        this.sendPacket(clickArm);
        this.getPlayer().inventory.armorInventory[armorSlot] = inventoryStack;

        if (armorStack != null) {
            Packet102WindowClick clickBack = new Packet102WindowClick(0, inventorySlot < 9 ? inventorySlot + 36 : inventorySlot, 0, false, armorStack, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
            this.sendPacket(clickBack);
            this.getPlayer().inventory.mainInventory[inventorySlot] = armorStack;
        }
    }

    private int getFreeSlot() {
        for (int i = 9; i < 36; i++) {
            ItemStack stack = this.getPlayer().inventory.mainInventory[i];

            if (stack == null) {
                return i;
            }
        }

        for (int i = 8; i >= 0; i--) {
            ItemStack stack = this.getPlayer().inventory.mainInventory[i];

            if (stack == null) {
                return i;
            }
        }

        return -1;
    }

    private int getSwappableSlot(ItemStack armorSlot) {
        for (int i = 35; i >= 9; i--) {
            ItemStack stack = this.getPlayer().inventory.mainInventory[i];

            if (stack == null) {
                continue;
            }

            if (stack.itemID != armorSlot.itemID) {
                return i;
            }
        }

        for (int i = 8; i >= 0; i--) {
            ItemStack stack = this.getPlayer().inventory.mainInventory[i];

            if (stack == null) {
                continue;
            }

            if (stack.itemID != armorSlot.itemID) {
                return i;
            }
        }

        return -1;
    }
}
