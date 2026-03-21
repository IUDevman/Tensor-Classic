package dev.tensor.feature.hacks;

import dev.tensor.backend.events.HurtCameraEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Hack.Info(name = "NoHurtCam", category = Category.Render)
public final class NoHurtCam extends Hack {

    @SuppressWarnings("unused")
    @EventTarget
    public void onHurtCameraEvent(HurtCameraEvent event) {
        event.cancel();
    }
}
