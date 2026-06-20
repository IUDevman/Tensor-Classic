package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.FOVModifierEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

@Hack.Info(name = "FOV", category = Category.Render)
public final class FOV extends Hack {

    public final IntegerSetting value = new IntegerSetting("Value", 90, 30, 120);

    @SuppressWarnings("unused")
    private final EventAction<FOVModifierEvent> fovModifierEventAction = this.addEventAction(new EventAction<>(FOVModifierEvent.class, event -> {

        event.FOV = this.value.getValue();
        event.cancel();
    }));
}
