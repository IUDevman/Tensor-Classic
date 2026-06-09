package dev.tensor.feature.hacks;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.ColorNoAlphaSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 06-08-2026
 */

@Hack.Info(name = "MainMenu", category = Category.Client, enabled = true, messages = false, drawn = false)
public final class MainMenu extends Hack {

    public final ColorNoAlphaSetting color = new ColorNoAlphaSetting("Color", new Color(176, 24, 24, 255));

    @Override
    public boolean isToggleable() {
        return !(this.getMinecraft().currentScreen instanceof TensorGUI && this.failsNullCheck());
    }

    public float[] getColorArray() {
        return new float[]{this.color.getValueRainbow().getRed() / 255F, this.color.getValueRainbow().getGreen() / 255F, this.color.getValueRainbow().getBlue() / 255F};
    }
}
