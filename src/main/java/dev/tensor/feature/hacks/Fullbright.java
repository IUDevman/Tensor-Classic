package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Hack.Info(name = "Fullbright", category = Category.Render, bind = Keyboard.KEY_B, description = "Makes the world bright.")
public final class Fullbright extends Hack {

    public final BooleanSetting ugandaMode = new BooleanSetting("Uganda Mode", false) {

        @Override
        public void setValue(Boolean VALUE) {
            super.setValue(VALUE);

            if (!this.failsNullCheck() && !this.isXrayEnabled()) {
                Fullbright fullbright = this.getHackManager().getHack(Fullbright.class);

                if (fullbright != null && fullbright.isEnabled()) {
                    fullbright.fillLightMap(VALUE);
                    this.resetWorld();
                }
            }
        }
    };

    private boolean shouldTriggerLightMapUpdate = false;

    @Override
    public void onEnable(boolean isNull) {
        if (!isNull && !this.isXrayEnabled()) {
            this.fillLightMap(this.ugandaMode.getValue());
            this.resetWorld();
            return;
        }

        this.shouldTriggerLightMapUpdate = true;
    }

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull && !this.isXrayEnabled()) {
            this.resetLightMap();
            this.resetWorld();
        }

        this.shouldTriggerLightMapUpdate = false;
    }

    @Override
    public void onClientTick() {
        if (this.shouldTriggerLightMapUpdate && !this.isXrayEnabled()) {
            this.fillLightMap(this.ugandaMode.getValue());
            this.shouldTriggerLightMapUpdate = false;
        }
    }

    @SuppressWarnings("unused")
    private final EventAction<ChangeWorldEvent> changeWorldEventAction = this.addHackEventAction(new EventAction<>(ChangeWorldEvent.class, EventPriority.Highest, event -> {
        if (!this.isXrayEnabled()) {
            this.fillLightMap(this.ugandaMode.getValue());
            this.resetWorld();
        }
    }));
}
