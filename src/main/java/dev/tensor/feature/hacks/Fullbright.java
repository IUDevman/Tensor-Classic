package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.LightValueEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import org.lwjgl.input.Keyboard;

@Hack.Info(name = "Fullbright", category = Category.Render, bind = Keyboard.KEY_B)
public final class Fullbright extends Hack {

    public final BooleanSetting ugandaMode = new BooleanSetting("Uganda Mode", false) {

        @Override
        public void setValue(Boolean VALUE) {
            super.setValue(VALUE);

            if (!this.failsNullCheck() && !this.isXrayEnabled()) {
                Fullbright fullbright = this.getHackManager().getHack(Fullbright.class);

                if (fullbright != null && fullbright.isEnabled()) {
                    this.resetWorld();
                }
            }
        }
    };

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
    EventAction<LightValueEvent> lightValueEventAction = this.addEventAction(new EventAction<>(LightValueEvent.class, event -> {
        if (this.ugandaMode.getValue() && !this.isXrayEnabled()) {
            event.darkMode = true;
        }

        event.cancel();
    }));
}
