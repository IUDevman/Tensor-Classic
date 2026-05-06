package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.BlockClickToPlaceEvent;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import net.minecraft.src.EnumMovingObjectType;

/**
 * @author Hoosiers
 * @credit FreeCliff
 * @source https://github.com/FreeCliff/rose-babric
 * @since 04-16-2026
 */

@Hack.Info(name = "RoseFastPlace", category = Category.Player)
public final class RoseFastPlace extends Hack {

    @SuppressWarnings("unused")
    public void onBlockClickToPlaceEvent(BlockClickToPlaceEvent event) {
        if (this.getMinecraft().objectMouseOver == null) {
            return;
        }

        if (!this.getMinecraft().objectMouseOver.typeOfHit.equals(EnumMovingObjectType.TILE)) {
            return;
        }

        event.cancel();
    }
}
