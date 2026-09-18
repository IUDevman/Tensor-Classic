package dev.tensor.misc.helpers.inventory;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 09-15-2026
 */

public final class ArmorSlot {

    private static final ArrayList<Integer> BOOTS = new ArrayList<>();

    public static ArrayList<Integer> BOOTS() {
        if (BOOTS.isEmpty()) {
            BOOTS.add(Item.bootsDiamond.shiftedIndex);
            BOOTS.add(Item.bootsSteel.shiftedIndex);
            BOOTS.add(Item.bootsChain.shiftedIndex);
            BOOTS.add(Item.bootsGold.shiftedIndex);
            BOOTS.add(Item.bootsLeather.shiftedIndex);
        }

        return BOOTS;
    }

    private static final ArrayList<Integer> PANTS = new ArrayList<>();

    public static ArrayList<Integer> PANTS() {
        if (PANTS.isEmpty()) {
            PANTS.add(Item.legsDiamond.shiftedIndex);
            PANTS.add(Item.legsSteel.shiftedIndex);
            PANTS.add(Item.legsChain.shiftedIndex);
            PANTS.add(Item.legsGold.shiftedIndex);
            PANTS.add(Item.legsLeather.shiftedIndex);
        }

        return PANTS;
    }

    private static final ArrayList<Integer> CHESTS = new ArrayList<>();

    public static ArrayList<Integer> CHESTS() {
        if (CHESTS.isEmpty()) {
            CHESTS.add(Item.plateDiamond.shiftedIndex);
            CHESTS.add(Item.plateSteel.shiftedIndex);
            CHESTS.add(Item.plateChain.shiftedIndex);
            CHESTS.add(Item.plateGold.shiftedIndex);
            CHESTS.add(Item.plateLeather.shiftedIndex);
        }

        return CHESTS;
    }

    private static final ArrayList<Integer> HELMETS = new ArrayList<>();

    public static ArrayList<Integer> HELMETS() {
        if (HELMETS.isEmpty()) {
            HELMETS.add(Item.helmetDiamond.shiftedIndex);
            HELMETS.add(Item.helmetSteel.shiftedIndex);
            HELMETS.add(Item.helmetChain.shiftedIndex);
            HELMETS.add(Item.helmetGold.shiftedIndex);
            HELMETS.add(Item.helmetLeather.shiftedIndex);
        }

        return HELMETS;
    }

    public final int location;
    public final Item item;

    public final int damage;

    public ArmorSlot(int location, ItemStack stack) {
        this.location = location;

        if (stack == null) {
            this.item = null;
            this.damage = -1;
            return;
        }

        this.item = stack.getItem();

        if (!stack.isItemStackDamageable()) {
            this.damage = -1;
        } else {
            this.damage = this.getDamagePercent(stack);
        }
    }

    private int getDamagePercent(ItemStack itemStack) {
        if (!itemStack.isItemStackDamageable()) {
            return 100;
        }

        if (!itemStack.isItemDamaged()) {
            return 0;
        }

        double damageFraction = (double) itemStack.getItemDamage() / itemStack.getMaxDamage();

        return (int) Math.min(Math.max(damageFraction * 100, 0), 100);
    }

    public int findBestItemForSlot(ItemStack[] mainInventory, int damageDiffPercent, int excludedItem) {
        int toReturn = -1;
        int damage = 101;
        boolean shouldCheckDiff = false;
        boolean shouldExclude = excludedItem != -1;

        if (this.damage != -1) {
            damage = this.damage;
            shouldCheckDiff = true;
        }

        boolean hasEmptySpace = false;

        for (int i = 0; i < 36; i++) {

            ItemStack item = mainInventory[i];

            if (item == null || item.getItem() == null) {
                hasEmptySpace = true;
                continue;
            }

            if (shouldExclude && item.itemID == excludedItem) {
                continue;
            }

            switch (this.location) {
                case 0:
                    if (BOOTS().contains(item.itemID)) {
                        int itemDamage = this.getDamagePercent(item);

                        if (itemDamage < damage) {
                            if (shouldCheckDiff && this.damage - itemDamage <= damageDiffPercent) {
                                continue;
                            }

                            damage = itemDamage;
                            toReturn = i;
                        }
                    }
                    break;
                case 1:
                    if (PANTS().contains(item.itemID)) {
                        int itemDamage = this.getDamagePercent(item);

                        if (itemDamage < damage) {
                            if (shouldCheckDiff && this.damage - itemDamage <= damageDiffPercent) {
                                continue;
                            }

                            damage = itemDamage;
                            toReturn = i;
                        }
                    }
                    break;
                case 2:
                    if (CHESTS().contains(item.itemID)) {
                        int itemDamage = this.getDamagePercent(item);

                        if (itemDamage < damage) {
                            if (shouldCheckDiff && this.damage - itemDamage <= damageDiffPercent) {
                                continue;
                            }

                            damage = itemDamage;
                            toReturn = i;
                        }
                    }
                    break;
                case 3:
                    if (HELMETS().contains(item.itemID)) {
                        int itemDamage = this.getDamagePercent(item);

                        if (itemDamage < damage) {
                            if (shouldCheckDiff && this.damage - itemDamage <= damageDiffPercent) {
                                continue;
                            }

                            damage = itemDamage;
                            toReturn = i;
                        }
                    }
                    break;
                default:
                    return -1;
            }
        }

        if (toReturn != -1 && !hasEmptySpace && this.item != null) {
            ItemStack toReturnItem = mainInventory[toReturn];
            int returnID = toReturnItem.itemID;

            //we cannot swap same items without having another empty slot
            if (returnID == this.item.shiftedIndex) {
                if (shouldExclude) {
                    return -1;
                } else {
                    return this.findBestItemForSlot(mainInventory, damageDiffPercent, returnID);
                }
            }
        }

        return toReturn;
    }
}
