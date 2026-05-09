package dev.tensor.feature.hacks;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@Hack.Info(name = "ClickGUI", category = Category.Client, bind = Keyboard.KEY_RSHIFT)
public final class ClickGUI extends Hack {

    @Override
    public boolean shouldDisableOnDisconnect() {
        return true;
    }

    public final BooleanSetting renderHUD = new BooleanSetting("Render HUD", true);
    public final IntegerSetting x = new IntegerSetting("X", 265, 2, 5000);
    public final IntegerSetting y = new IntegerSetting("Y", 135, 2, 2000);
    public final IntegerSetting scrollSpeedGUI = new IntegerSetting("Panel Scroll Speed", 15, 5, 30);
    public final IntegerSetting scrollSpeedWindow = new IntegerSetting("Window Scroll Speed", 20, 5, 30);
    public final ColorSetting fillColor = new ColorSetting("Fill Color", new Color(0, 0, 0, 150));
    public final ColorSetting selectedFillColor = new ColorSetting("Selected Fill Color", new Color(30, 30, 30, 150));
    public final ColorSetting outlineColor = new ColorSetting("Outline Color", new Color(62, 11, 10, 200));

    public TensorGUI tensorGUI = null;

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
