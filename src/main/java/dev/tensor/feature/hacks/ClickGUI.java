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
    public final IntegerSetting x = new IntegerSetting("X", 265, 2, 5000, "None", "Where the GUI should start to render on the screen X axis.");
    public final IntegerSetting y = new IntegerSetting("Y", 135, 2, 2000, "None", "Where the GUI should start to render on the screen Y axis.");
    public final IntegerSetting scrollSpeedGUI = new IntegerSetting("Panel Scroll Speed", 15, 5, 30, "None", "How fast hacks or settings scroll in the GUI.");
    public final IntegerSetting scrollSpeedWindow = new IntegerSetting("Window Scroll Speed", 20, 5, 30, "None", "How fast elements scroll in custom windows.");
    public final EnumSetting settingStyle = new EnumSetting("Setting Style", SettingStyle.New, new String[]{
            "This is how the setting text will render.",
            "New® Draws text in the center of the panel.",
            "Original® Draws text on the left like the original Tensor."
    });
    public final BooleanSetting showDescriptions = new BooleanSetting("Show Descriptions", true, "Enable to show hack/setting descriptions while holding shift.");
    public final BooleanSetting renderDescriptionsWithNoOpacity = new BooleanSetting("No Opacity Descriptions", false, "Renders descriptions without opacity for better readability.");
    public final ColorSetting fillColor = new ColorSetting("Fill Color", new Color(0, 0, 0, 150), "Sets the color of the GUI box shading.");
    public final ColorSetting selectedFillColor = new ColorSetting("Selected Fill Color", new Color(30, 30, 30, 150), "Sets the color of the GUI box shading when a box is selected or hovered.");
    public final ColorSetting outlineColor = new ColorSetting("Outline Color", new Color(62, 11, 10, 200), "Sets the color of the GUI outlines.");

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
