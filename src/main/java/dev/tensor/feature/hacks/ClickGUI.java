package dev.tensor.feature.hacks;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-11-2026
 */

@Hack.Info(name = "ClickGUI", category = Category.Client, bind = Keyboard.KEY_RSHIFT, description = "Opens the GUI to adjust client hacks and settings.")
public final class ClickGUI extends Hack {

    @Override
    public boolean shouldDisableOnDisconnect() {
        return true;
    }

    @Override
    public void reset(boolean toggle) {
        super.reset(false);
    }

    public final BooleanSetting renderHUD = new BooleanSetting("Render HUD", true, "Allows for HUD Objects to be rendered while the GUI is open.");
    public final IntegerSetting x = new IntegerSetting("X", 265, 2, 5000);
    public final IntegerSetting y = new IntegerSetting("Y", 135, 2, 2000);
    public final IntegerSetting scrollSpeedGUI = new IntegerSetting("Panel Scroll Speed", 15, 5, 30);
    public final IntegerSetting scrollSpeedWindow = new IntegerSetting("Window Scroll Speed", 20, 5, 30);
    public final EnumSetting settingStyle = new EnumSetting("Setting Style", SettingStyle.New);
    public final BooleanSetting showDescriptions = new BooleanSetting("Show Descriptions", true, "Enable to show hack/setting descriptions while holding shift.");
    public final ColorSetting fillColor = new ColorSetting("Fill Color", new Color(0, 0, 0, 150));
    public final ColorSetting selectedFillColor = new ColorSetting("Selected Fill Color", new Color(30, 30, 30, 150));
    public final ColorSetting outlineColor = new ColorSetting("Outline Color", new Color(62, 11, 10, 200));

    public TensorGUI tensorGUI = null;

    @Override
    public void onEnable(boolean isNull) {
        if (isNull) {
            return;
        }

        if (this.tensorGUI == null || this.tensorGUI.mainMenuObject != null) {
            this.tensorGUI = new TensorGUI(this, null);
        }

        this.getMinecraft().displayGuiScreen(this.tensorGUI);
    }

    public boolean shouldRenderOnTheLeft() {
        return this.settingStyle.getValue().equals(SettingStyle.Original);
    }

    public enum SettingStyle {
        New,
        Original
    }
}
