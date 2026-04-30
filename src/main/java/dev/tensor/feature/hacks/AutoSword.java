package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.AttackEntityEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import net.minecraft.src.ItemStack;

/**
 * @author Hoosiers
 * @since 04-23-2026
 */

@Hack.Info(name = "AutoSword", category = Category.Combat)
public final class AutoSword extends Hack {

    public final BooleanSetting swapBack = new BooleanSetting("Swap Back", false) {

        @Override
        public void setValue(Boolean value) {
            super.setValue(value);

            if (this.failsNullCheck()) {
                return;
            }

            AutoSword autoSword = this.getHackManager().getHack(AutoSword.class);

            if (autoSword != null && autoSword.isEnabled()) {
                autoSword.resetPrevSlot();
            }
        }
    };

    public boolean swapped = false;
    public int prevItem = -1;

    private int ticksSince = 0;

    @Override
    public void onEnable(boolean isNull) {
        this.resetPrevSlot();
    }

    @Override
    public void onDisable(boolean isNull) {
        this.resetPrevSlot();
    }

    @Override
    public void onClientTick() {
        if (this.ticksSince < 1) {
            this.ticksSince++;
            return;
        }

        this.ticksSince = 0;

        if (!this.swapBack.getValue()) {
            return;
        }

        InfiniteDurability infiniteDurability = this.getHackManager().getHack(InfiniteDurability.class);

        if (infiniteDurability != null && infiniteDurability.isEnabled()) {
            return;
        }

        if (this.swapped && this.prevItem != -1 && this.getInventory().currentItem != this.prevItem) {

            this.getInventory().currentItem = this.prevItem;
            this.resetPrevSlot();
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onAttackEntityEvent(AttackEntityEvent event) {

        int swapSlot = this.getInventory().currentItem;
        float currentDamage = 0;

        ItemStack currentItem = this.getInventory().getStackInSlot(swapSlot);

        if (currentItem != null) {
            currentDamage = currentItem.getDamageVsEntity(null);
        }

        int oldSlot = swapSlot;

        for (int i = 0; i < 9; i++) {

            ItemStack itemStack = this.getInventory().getStackInSlot(i);

            if (itemStack == null) {
                continue;
            }

            float damage = itemStack.getDamageVsEntity(null);

            if (damage > currentDamage) {
                currentDamage = damage;
                swapSlot = i;
            }
        }

        if (swapSlot != oldSlot) {
            this.ticksSince = 0;
            this.swapped = true;
            this.prevItem = oldSlot;
            this.getInventory().currentItem = swapSlot;
        }
    }

    public void resetPrevSlot() {
        this.ticksSince = 0;
        this.swapped = false;
        this.prevItem = -1;
    }
}
