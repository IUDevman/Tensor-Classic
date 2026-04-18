package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.inventory.FoodSlot;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet102WindowClick;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Hoosiers
 * @since 04-04-2026
 */

@Hack.Info(name = "AutoEat", category = Category.Combat)
public final class AutoEat extends Hack {

    public final IntegerSetting maxHealth = new IntegerSetting("Max Health", 15, 5, 20);
    public final IntegerSetting delay = new IntegerSetting("Tick Delay", 8, 0, 60);
    public final BooleanSetting moveFromInventory = new BooleanSetting("Fill From Inventory", true);

    private int delayTicks = 0;

    @Override
    public void onEnable(boolean isNull) {
        this.delayTicks = this.delay.getValue();
    }

    @Override
    public void onClientTick() {

        if (this.delayTicks < this.delay.getValue()) {
            delayTicks++;
            return;
        }

        int health = this.getPlayer().health;

        if (health >= this.maxHealth.getValue()) {
            return;
        }

        ArrayList<FoodSlot> foodSlots = new ArrayList<>();

        ItemStack[] items = this.getInventory().mainInventory;
        for (int i = 0; i < items.length; i++) {

            ItemStack itemStack = this.getInventory().getStackInSlot(i);

            if (itemStack == null) {
                continue;
            }

            if (itemStack.getItem() instanceof ItemFood) {
                ItemFood itemFood = (ItemFood) itemStack.getItem();
                foodSlots.add(new FoodSlot(i > 8, i, itemStack, itemFood.getHealAmount()));
            }
        }

        if (foodSlots.isEmpty()) {
            return;
        }

        ArrayList<FoodSlot> overSlots = new ArrayList<>();
        ArrayList<FoodSlot> underSlots = new ArrayList<>();

        for (int i = 0; i < foodSlots.size(); i++) {

            FoodSlot foodSlot = foodSlots.get(i);

            int waste = (health + foodSlot.healthRestored) - this.maxHealth.getValue();

            if (waste >= 0) {
                overSlots.add(foodSlot);
            } else {
                underSlots.add(foodSlot);
            }
        }

        FoodSlot overSlotMain = overSlots.stream().filter(foodSlot -> !foodSlot.needsSwapped).min(Comparator.comparing(foodSlot -> foodSlot.healthRestored)).orElse(null);
        FoodSlot underSlotMain = underSlots.stream().filter(foodSlot -> !foodSlot.needsSwapped).max(Comparator.comparing(foodSlot -> foodSlot.healthRestored)).orElse(null);

        FoodSlot toEat = null;

        if (overSlotMain != null) {
            toEat = overSlotMain;

        } else if (underSlotMain != null) {
            toEat = underSlotMain;

        } else {
            if (!overSlots.isEmpty()) {
                toEat = overSlots.stream().min(Comparator.comparing(foodSlot -> foodSlot.healthRestored)).orElse(null);

            } else if (!underSlots.isEmpty()) {
                toEat = underSlots.stream().max(Comparator.comparing(foodSlot -> foodSlot.healthRestored)).orElse(null);
            }
        }

        if (toEat != null) {

            if (toEat.needsSwapped && this.moveFromInventory.getValue()) {

                int swapSlot = -1;
                for (int i = 0; i < 9; i++) {
                    if (this.getInventory().mainInventory[i] == null) {
                        swapSlot = i;
                        break;
                    }
                }

                if (swapSlot != -1) {
                    Packet102WindowClick click = new Packet102WindowClick(0, toEat.slot, 0, true, toEat.item, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
                    this.sendPacket(click);

                    this.eat(swapSlot, toEat.item);

                }

            } else if (!toEat.needsSwapped) {
                this.eat(toEat.slot, toEat.item);
            }

            this.delayTicks = 0;
        }
    }

    private void eat(int newSlot, ItemStack item) {
        int prevItem = this.getInventory().currentItem;
        this.getInventory().currentItem = newSlot;
        this.getMinecraft().playerController.sendUseItem(this.getPlayer(), this.getWorld(), item);
        this.getInventory().currentItem = prevItem;
    }
}
