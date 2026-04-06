package dev.tensor.feature.hacks;

import dev.tensor.backend.events.MovementClipEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import net.minecraft.src.Material;

/**
 * @author Hoosiers
 * @since 04-05-2026
 */

@Hack.Info(name = "SafeWalk", category = Category.Movement)
public final class SafeWalk extends Hack {

    public final BooleanSetting fluids = new BooleanSetting("Fluids", false);

    @SuppressWarnings("unused")
    @EventTarget
    public void onMovementClipEvent(MovementClipEvent event) {

        if (!this.fluids.getValue() && this.isPlayerInFluids()) {
            return;
        }

        event.cancel();
    }

    private boolean isPlayerInFluids() {
        boolean isInWater = this.getPlayer().isInWater();
        boolean isInLava = this.getPlayer().isInsideOfMaterial(Material.lava);

        return isInWater || isInLava;
    }
}
