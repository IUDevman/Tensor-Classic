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

@Hack.Info(name = "CustomClouds", category = Category.Render)
public final class CustomClouds extends Hack {

    public final BooleanSetting renderClouds = new BooleanSetting("Render Clouds", true);
    public final BooleanSetting customColor = new BooleanSetting("Custom Color", true);
    public final ColorNoAlphaSetting color = new ColorNoAlphaSetting("Color", new Color(255, 0, 0, 255));

    @SuppressWarnings("unused")
    private final EventAction<RenderSkyEvent> renderSkyEventAction = this.addEventAction(new EventAction<>(RenderSkyEvent.class, event -> {
        if (!event.type.equals(RenderSkyEvent.Type.CLOUDS)) {
            return;
        }

        if (!this.renderClouds.getValue()) {
            event.cancel();
            return;
        }

        if (this.customColor.getValue()) {
            event.setNewColor(this.color.getValueRainbow());
        }
    }));
}
