package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.BlockClickToPlaceEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import net.minecraft.src.EnumMovingObjectType;

/**
 * @author Hoosiers
 * @since 04-16-2026
 */

/**
 * @credit FreeCliff
 * @source https://github.com/FreeCliff/rose-babric
 */

@Hack.Info(name = "RoseFastPlace", category = Category.Player, description = "Allows you to place blocks fast. From Rose by FreeCliff.")
public final class RoseFastPlace extends Hack {

    @SuppressWarnings("unused")
    private final EventAction<BlockClickToPlaceEvent> blockClickToPlaceEventAction = this.addHackEventAction(new EventAction<>(BlockClickToPlaceEvent.class, event -> {
        if (this.getMinecraft().objectMouseOver == null) {
            return;
        }

        if (!this.getMinecraft().objectMouseOver.typeOfHit.equals(EnumMovingObjectType.TILE)) {
            return;
        }

        event.cancel();
    }));
}
