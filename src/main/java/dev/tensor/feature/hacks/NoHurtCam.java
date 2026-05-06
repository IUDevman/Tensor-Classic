package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.HurtCameraEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventConstantCancellable;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Hack.Info(name = "NoHurtCam", category = Category.Render)
public final class NoHurtCam extends Hack {

    @SuppressWarnings("unused")
    EventAction<HurtCameraEvent> hurtCameraEventAction = this.addEventAction(new EventAction<>(HurtCameraEvent.class, EventConstantCancellable::cancel));
}
