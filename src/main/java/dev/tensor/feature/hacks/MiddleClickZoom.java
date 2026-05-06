package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.SmoothCameraEvent;
import dev.tensor.backend.events.variable.FOVModifierEvent;
import dev.tensor.backend.events.variable.MouseEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import org.lwjgl.input.Mouse;

/**
 * @author Hoosiers
 * @since 05-03-2026
 */

@Hack.Info(name = "MiddleClickZoom", category = Category.Render)
public final class MiddleClickZoom extends Hack {

    public final BooleanSetting smoothCamera = new BooleanSetting("Smooth Camera", true);
    public final DoubleSetting multiplier = new DoubleSetting("Multiplier", 0.30, 0.01, 0.60, 2);
    public final BooleanSetting scroll = new BooleanSetting("Scroll Zoom", false);
    public final DoubleSetting scrollSensitivity = new DoubleSetting("Scroll Sensitivity", 0.4, 0.1, 1.0, 1);

    private boolean isZoomed = false;

    @Override
    public void onEnable(boolean isNull) {
        this.isZoomed = false;
    }

    @Override
    public void onClientTick() {
        if (this.getMinecraft().gameSettings.thirdPersonView) {
            this.isZoomed = false;
            return;
        }

        if (Mouse.isButtonDown(2) && this.getMinecraft().currentScreen == null) {
            this.isZoomed = true;

        } else {
            this.isZoomed = false;
        }
    }

    @SuppressWarnings("unused")
    EventAction<MouseEvent> mouseEventAction = this.addEventAction(new EventAction<>(MouseEvent.class, event -> {
        if (!this.isZoomed) {
            return;
        }

        if (event.dWheel == 0) {
            return;
        }

        if (!this.scroll.getValue()) {
            return;
        }

        double multiplier = this.multiplier.getValue() + ((this.scrollSensitivity.getValue() / 1000) * event.dWheel);
        this.multiplier.setValue(multiplier);
    }));

    @SuppressWarnings("unused")
    EventAction<FOVModifierEvent> fovModifierEventAction = this.addEventAction(new EventAction<>(FOVModifierEvent.class, EventPriority.Lowest, event -> {
        if (this.isZoomed) {
            float fov = (float) (event.FOV * this.multiplier.getValue());

            if (fov < 1) {
                fov = 1;
            }

            event.FOV = fov;
            event.cancel();
        }
    }));

    @SuppressWarnings("unused")
    EventAction<SmoothCameraEvent> smoothCameraEventAction = this.addEventAction(new EventAction<>(SmoothCameraEvent.class, event -> {
        if (this.smoothCamera.getValue() && this.isZoomed) {
            event.cancel();
        }
    }));
}
