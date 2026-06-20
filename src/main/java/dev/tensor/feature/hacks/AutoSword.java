package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.AttackEntityEvent;
import dev.tensor.misc.event.EventAction;
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

    public boolean dispatched = false;
    public boolean swapped = false;
    public int prevItem = -1;

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
        if (!this.swapBack.getValue()) {
            this.prevItem = -1;
            this.swapped = false;
            return;
        }

        if (this.swapped && this.prevItem != -1 && this.getInventory().currentItem != this.prevItem) {

            this.getInventory().currentItem = this.prevItem;
            this.resetPrevSlot();
        }
    }

    @SuppressWarnings("unused")
    private final EventAction<AttackEntityEvent> attackEntityEventAction = this.addEventAction(new EventAction<>(AttackEntityEvent.class, event -> {
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
            this.dispatched = false;
            this.swapped = true;
            this.prevItem = oldSlot;
            this.getInventory().currentItem = swapSlot;
        }
    }));

    public void resetPrevSlot() {
        this.dispatched = false;
        this.swapped = false;
        this.prevItem = -1;
    }
}
