package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.GuiChatScreenEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.gui.TensorChatGUI;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.ColorSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-24-2026
 */

@Hack.Info(name = "CommandPreview", category = Category.Client, drawn = false, enabled = true)
public final class CommandPreview extends Hack {

    public final ColorSetting fillColor = new ColorSetting("Fill Color", new Color(0, 0, 0, 150));
    public final ColorSetting outlineColor = new ColorSetting("Outline Color", new Color(62, 11, 10, 200));
    public final ColorSetting textColor = new ColorSetting("Text Color", new Color(255, 255, 0, 255));

    private final TensorChatGUI tensorChatGUI = new TensorChatGUI();

    @SuppressWarnings("unused")
    private final EventAction<GuiChatScreenEvent> guiChatScreenEventAction = this.addHackEventAction(new EventAction<>(GuiChatScreenEvent.class, event -> {
        if (!this.getHackManager().getHack(Commands.class).isEnabled()) {
            return;
        }

        String message = event.message;

        if (message == null) {
            return;
        }

        String prefix = this.getHackManager().getHack(Commands.class).prefix.getValue();

        if (!message.startsWith(prefix)) {
            return;
        }

        tensorChatGUI.render(prefix, event.message, event.minX, event.minY, event.maxX, event.maxY, this.fillColor.getValueRainbow().getRGB(), this.outlineColor.getValueRainbow().getRGB(), this.textColor.getValueRainbow().getRGB());
    }));
}
