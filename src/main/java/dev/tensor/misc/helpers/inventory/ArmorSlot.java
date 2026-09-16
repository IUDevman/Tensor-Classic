package dev.tensor.misc.helpers.inventory;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 09-15-2026
 */

public final class ArmorSlot {

    private static final ArrayList<String> MAX_ITEMS = new ArrayList<>();

    public static ArrayList<String> MAX_ITEMS() {
        if (MAX_ITEMS.isEmpty()) {
            MAX_ITEMS.add(Item.bootsDiamond.getItemName());
            MAX_ITEMS.add(Item.legsDiamond.getItemName());
            MAX_ITEMS.add(Item.plateDiamond.getItemName());
            MAX_ITEMS.add(Item.helmetDiamond.getItemName());
        }

        return MAX_ITEMS;
    }

    private static final ArrayList<String> BOOTS = new ArrayList<>();

    public static ArrayList<String> BOOTS() {
        if (BOOTS.isEmpty()) {
            BOOTS.add(Item.bootsDiamond.getItemName());
            BOOTS.add(Item.bootsSteel.getItemName());
            BOOTS.add(Item.bootsChain.getItemName());
            BOOTS.add(Item.bootsGold.getItemName());
            BOOTS.add(Item.bootsLeather.getItemName());
        }

        return BOOTS;
    }

    private static final ArrayList<String> PANTS = new ArrayList<>();

    public static ArrayList<String> PANTS() {
        if (PANTS.isEmpty()) {
            PANTS.add(Item.legsDiamond.getItemName());
            PANTS.add(Item.legsSteel.getItemName());
            PANTS.add(Item.legsChain.getItemName());
            PANTS.add(Item.legsGold.getItemName());
            PANTS.add(Item.legsLeather.getItemName());
        }

        return PANTS;
    }

    private static final ArrayList<String> CHESTS = new ArrayList<>();

    public static ArrayList<String> CHESTS() {
        if (CHESTS.isEmpty()) {
            CHESTS.add(Item.plateDiamond.getItemName());
            CHESTS.add(Item.plateSteel.getItemName());
            CHESTS.add(Item.plateChain.getItemName());
            CHESTS.add(Item.plateGold.getItemName());
            CHESTS.add(Item.plateLeather.getItemName());
        }

        return CHESTS;
    }

    private static final ArrayList<String> HELMETS = new ArrayList<>();

    public static ArrayList<String> HELMETS() {
        if (HELMETS.isEmpty()) {
            HELMETS.add(Item.helmetDiamond.getItemName());
            HELMETS.add(Item.helmetSteel.getItemName());
            HELMETS.add(Item.helmetChain.getItemName());
            HELMETS.add(Item.helmetGold.getItemName());
            HELMETS.add(Item.helmetLeather.getItemName());
        }

        return HELMETS;
    }

    public static int convert2Value(String item) {
        item = item.toLowerCase();

        if (item.contains("diamond")) {
            return 5;
        } else if (item.contains("gold")) {
            return 4;
        } else if (item.contains("iron")) {
            return 3;
        } else if (item.contains("chain")) {
            return 2;
        } else if (item.contains("cloth")) {
            return 1;
        } else {
            return 0;
        }
    }

    public final int location;
    public final Item item;

    public ArmorSlot(int location, Item item) {
        this.location = location;
        this.item = item;
    }

    public int findBestItemForSlot(ItemStack[] mainInventory) {
        int toReturn = -1;
        int currentLevel = item == null ? 0 : convert2Value(item.getItemName());

        for (int i = 0; i <= 36; i++) {

            ItemStack item = mainInventory[i];

            if (item == null || item.getItem() == null) {
                continue;
            }

            String itemName = item.getItemName();

            switch (this.location) {
                case 0:
                    if (BOOTS().contains(itemName)) {
                        int value = convert2Value(itemName);

                        if (value > currentLevel) {
                            currentLevel = value;
                            toReturn = i;
                        }
                    }

                    break;
                case 1:
                    if (PANTS().contains(itemName)) {
                        int value = convert2Value(itemName);

                        if (value > currentLevel) {
                            currentLevel = value;
                            toReturn = i;
                        }
                    }
                    break;
                case 2:
                    if (CHESTS().contains(itemName)) {
                        int value = convert2Value(itemName);

                        if (value > currentLevel) {
                            currentLevel = value;
                            toReturn = i;
                        }
                    }
                    break;
                case 3:
                    if (HELMETS().contains(itemName)) {
                        int value = convert2Value(itemName);

                        if (value > currentLevel) {
                            currentLevel = value;
                            toReturn = i;
                        }
                    }
                    break;
                default:
                    return -1;
            }
        }

        return toReturn;
    }
}
