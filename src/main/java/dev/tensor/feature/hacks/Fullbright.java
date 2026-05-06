package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.LightValueEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventConstantCancellable;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import org.lwjgl.input.Keyboard;

@Hack.Info(name = "Fullbright", category = Category.Render, bind = Keyboard.KEY_B)
public final class Fullbright extends Hack {

    @Override
    public void onEnable(boolean isNull) {
        if (!isNull && !this.isXrayEnabled()) {
            this.resetWorld();
        }
    }

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull && !this.isXrayEnabled()) {
            this.resetWorld();
        }
    }

    @SuppressWarnings("unused")
    EventAction<LightValueEvent> lightValueEventAction = this.addEventAction(new EventAction<>(LightValueEvent.class, EventConstantCancellable::cancel));
}
