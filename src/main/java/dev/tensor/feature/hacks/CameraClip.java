package dev.tensor.feature.hacks;

import dev.tensor.backend.events.CameraClipEvent;
import dev.tensor.backend.events.MouseEvent;
import dev.tensor.backend.mixins.accessors.EntityRendererAccessor;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;

/**
 * @author Hoosiers
 * @since 04-12-2026
 */

@Hack.Info(name = "CameraClip", category = Category.Render)
public final class CameraClip extends Hack {

    public final BooleanSetting clipThroughWalls = new BooleanSetting("Clip Through Walls", true);
    public final BooleanSetting distance = new BooleanSetting("Custom Distance", true);
    public final DoubleSetting distanceValue = new DoubleSetting("Distance Length", 10.0, 1.0, 50.0, 1);
    public final BooleanSetting scroll = new BooleanSetting("Scroll Distance", true);
    public final DoubleSetting scrollSensitivity = new DoubleSetting("Scroll Sensitivity", 0.4, 0.1, 1.0, 1);

    private final float defaultDistance = 4.0F;

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull && ((EntityRendererAccessor) this.getMinecraft().entityRenderer).getCameraDistance() != this.defaultDistance) {
            ((EntityRendererAccessor) this.getMinecraft().entityRenderer).setCameraDistance(this.defaultDistance);
        }
    }

    @Override
    public void onClientTick() {
        if (!this.getMinecraft().gameSettings.thirdPersonView) {
            return;
        }

        if (this.distance.getValue()) {

            float newDistance = this.getNewDistance();

            if (((EntityRendererAccessor) this.getMinecraft().entityRenderer).getCameraDistance() != newDistance) {
                ((EntityRendererAccessor) this.getMinecraft().entityRenderer).setCameraDistance(newDistance);
            }
        } else if (((EntityRendererAccessor) this.getMinecraft().entityRenderer).getCameraDistance() != this.defaultDistance) {
            ((EntityRendererAccessor) this.getMinecraft().entityRenderer).setCameraDistance(defaultDistance);
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onCameraClipEvent(CameraClipEvent event) {
        if (this.clipThroughWalls.getValue()) {
            event.cancel();
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onMouseEvent(MouseEvent event) {
        if (event.dWheel == 0) {
            return;
        }

        if (!this.scroll.getValue() || !this.distance.getValue()) {
            return;
        }

        if (!this.getMinecraft().gameSettings.thirdPersonView) {
            return;
        }

        double newDistance = this.distanceValue.getValue() + ((this.scrollSensitivity.getValue() / 100) * event.dWheel);
        this.distanceValue.setValue(newDistance);
    }

    private float getNewDistance() {
        double distance = this.distanceValue.getValue();

        return (float) distance;
    }
}
