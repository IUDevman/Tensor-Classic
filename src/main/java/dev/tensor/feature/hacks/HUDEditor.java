package dev.tensor.feature.hacks;

import dev.tensor.misc.gui.TensorHUDEditor;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

@Hack.Info(name = "HUDEditor", category = Category.Client, bind = Keyboard.KEY_GRAVE, description = "Opens custom HUD editor to move around client HUD objects with your mouse.")
public final class HUDEditor extends Hack {

    @Override
    public boolean shouldDisableOnDisconnect() {
        return true;
    }

    public final BooleanSetting showDescriptions = new BooleanSetting("Show Descriptions", true, "Enable to show HUD Object descriptions when shift is held.");
    public final EnumSetting render = new EnumSetting("Render", RenderType.Both);
    public final ColorSetting fillColor = new ColorSetting("Fill Color", new Color(0, 0, 0, 150));
    public final ColorSetting outlineColor = new ColorSetting("Outline Color", new Color(62, 11, 10, 200));

    private TensorHUDEditor tensorHUDEditor = null;

    @Override
    public void onEnable(boolean isNull) {
        if (isNull) {
            return;
        }

        if (this.getHackManager().getHack(ClickGUI.class).isEnabled()) {
            this.getHackManager().getHack(ClickGUI.class).toggle();
        }

        if (this.tensorHUDEditor == null) {
            this.tensorHUDEditor = new TensorHUDEditor(this);
        }

        this.getMinecraft().displayGuiScreen(this.tensorHUDEditor);
    }

    public boolean isRenderingInEditor() {
        if (!this.isEnabled()) {
            return false;
        }

        return !this.render.getValue().equals(RenderType.Outline);
    }

    public enum RenderType {
        Outline,
        Object,
        Both
    }
}
