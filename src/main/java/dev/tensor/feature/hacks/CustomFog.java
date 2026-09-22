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

@Hack.Info(name = "CustomFog", category = Category.Render, description = "Modifies fog rendering and color.")
public final class CustomFog extends Hack {

    public final BooleanSetting renderSkyFog = new BooleanSetting("Render Sky Fog", false, "Disable to not render terrain fog.");
    public final BooleanSetting renderFluidFog = new BooleanSetting("Render Fluid Fog", true, "Disable to not render the 'fog' when in fluids.");
    public final BooleanSetting fogColor = new BooleanSetting("Custom Color (Ugly)", true, "Enable to render fog in a custom shitty color.");
    public final ColorNoAlphaSetting color = new ColorNoAlphaSetting("Color", new Color(255, 105, 180, 255));

    @SuppressWarnings("unused")
    private final EventAction<RenderSkyEvent> renderSkyEventAction = this.addHackEventAction(new EventAction<>(RenderSkyEvent.class, event -> {
        if (event.type.equals(RenderSkyEvent.Type.FOG_FLUIDS) && !this.renderFluidFog.getValue()) {
            event.cancel();
            return;
        }

        if (event.type.equals(RenderSkyEvent.Type.FOG_SKY)) {
            if (!this.renderSkyFog.getValue()) {
                event.cancel();
                return;
            }

            if (this.fogColor.getValue()) {
                event.setNewColor(this.color.getValueRainbow());
            }
        }
    }));
}
