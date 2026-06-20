package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.BlockClickToPlaceEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.ItemBow;
import net.minecraft.src.ItemStack;

/**
 * @author Hoosiers
 * @since 04-28-2026
 */

@Hack.Info(name = "FastBow", category = Category.Combat)
public final class FastBow extends Hack {

    public final IntegerSetting arrowsPerClick = new IntegerSetting("Arrows Per Click", 1, 1, 64);

    @SuppressWarnings("unused")
    private final EventAction<BlockClickToPlaceEvent> blockClickToPlaceEventAction = this.addEventAction(new EventAction<>(BlockClickToPlaceEvent.class, event -> {
        ItemStack currentItem = this.getInventory().getCurrentItem();

        if (currentItem == null) {
            return;
        }

        if (currentItem.getItem() instanceof ItemBow) {
            if (this.arrowsPerClick.getValue() > 1) {
                event.count = this.arrowsPerClick.getValue();
            }

            event.cancel();
        }
    }));
}
