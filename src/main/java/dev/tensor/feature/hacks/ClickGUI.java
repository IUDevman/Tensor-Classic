package dev.tensor.feature.hacks;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@Hack.Info(name = "ClickGUI", category = Category.Client, bind = Keyboard.KEY_RSHIFT)
public final class ClickGUI extends Hack {

    public final IntegerSetting x = new IntegerSetting("X", 265, 2, this.getMinecraft().displayWidth + 36);
    public final IntegerSetting y = new IntegerSetting("Y", 135, 2, this.getMinecraft().displayHeight - 42);
    public final ColorSetting fillColor = new ColorSetting("Fill Color", new Color(0, 0, 0, 150));
    public final ColorSetting selectedFillColor = new ColorSetting("Selected Fill Color", new Color(30, 30, 30, 150));
    public final ColorSetting outlineColor = new ColorSetting("Outline Color", new Color(62, 11, 10, 200));
    public final ColorSetting textColor = new ColorSetting("Text Color", new Color(255, 255, 255, 255));
    public final ColorSetting enabledTextColor = new ColorSetting("Enabled Hack Text Color", Color.YELLOW);

    TensorGUI tensorGUI = null;

    @Override
    public void onEnable(boolean isNull) {

        if (isNull) {
            return;
        }

        if (this.tensorGUI == null) {
            tensorGUI = new TensorGUI(this);
        }

        this.getMinecraft().displayGuiScreen(tensorGUI);
    }
}
