package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.RenderSkyEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.ColorNoAlphaSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-01-2026
 */

@Hack.Info(name = "CustomFog", category = Category.Render)
public final class CustomFog extends Hack {

    public final BooleanSetting renderSkyFog = new BooleanSetting("Render Sky Fog", false);
    public final BooleanSetting renderFluidFog = new BooleanSetting("Render Fluid Fog", true);
    public final BooleanSetting fogColor = new BooleanSetting("Custom Color (Ugly)", true);
    public final ColorNoAlphaSetting color = new ColorNoAlphaSetting("Color", new Color(255, 105, 180, 255));

    @SuppressWarnings("unused")
    EventAction<RenderSkyEvent> renderSkyEventAction = this.addEventAction(new EventAction<>(RenderSkyEvent.class, event -> {
        if (event.type.equals(RenderSkyEvent.Type.Fog_Fluids) && !this.renderFluidFog.getValue()) {
            event.cancel();
            return;
        }

        if (event.type.equals(RenderSkyEvent.Type.Fog)) {
            if (!this.renderSkyFog.getValue()) {
                event.cancel();
                return;
            }

            if (this.fogColor.getValue()) {
                event.setNewColor(this.color.getValue());
            }
        }
    }));
}
