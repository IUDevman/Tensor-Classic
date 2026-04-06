package dev.tensor.misc.helpers.inventory;

import net.minecraft.src.ItemStack;

/**
 * @author Hoosiers
 * @since 04-04-2026
 */

public final class FoodSlot {

    public final boolean needsSwapped;
    public final int slot;
    public final ItemStack item;
    public final int healthRestored;

    public FoodSlot(boolean needsSwapped, int slot, ItemStack item, int healthRestored) {
        this.needsSwapped = needsSwapped;
        this.slot = slot;
        this.item = item;
        this.healthRestored = healthRestored;
    }
}
