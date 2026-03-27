package dev.tensor.feature.hacks;

import dev.tensor.backend.events.LightValueEvent;
import dev.tensor.misc.event.EventTarget;
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
    @EventTarget
    public void onLightValueEvent(LightValueEvent event) {
        event.cancel();
    }
}
