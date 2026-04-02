package dev.tensor.feature.hacks;

import dev.tensor.backend.events.RenderSkyEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.ColorSettingNoAlpha;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-01-2026
 */

@Hack.Info(name = "Custom Clouds", category = Category.Render)
public final class CustomClouds extends Hack {

    public final BooleanSetting renderClouds = new BooleanSetting("Render Clouds", true);
    public final BooleanSetting customColor = new BooleanSetting("Custom Color", true);
    public final ColorSettingNoAlpha color = new ColorSettingNoAlpha("Color", new Color(255, 0, 0, 255));

    @SuppressWarnings("unused")
    @EventTarget
    public void onRenderSkyEvent(RenderSkyEvent event) {

        if (!event.type.equals(RenderSkyEvent.Type.Clouds)) {
            return;
        }

        if (!this.renderClouds.getValue()) {
            event.cancel();
            return;
        }

        if (this.customColor.getValue()) {
            event.setNewColor(this.color.getValue());
        }
    }
}
