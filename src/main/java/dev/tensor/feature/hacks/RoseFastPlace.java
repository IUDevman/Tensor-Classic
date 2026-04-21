package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.BlockClickToPlaceEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @credit FreeCliff
 * @source https://github.com/FreeCliff/rose-babric
 * @since 04-16-2026
 */

@Hack.Info(name = "RoseFastPlace", category = Category.Player)
public final class RoseFastPlace extends Hack {

    @SuppressWarnings("unused")
    @EventTarget
    public void onBlockClickToPlaceEvent(BlockClickToPlaceEvent event) {
        event.cancel();
    }
}
