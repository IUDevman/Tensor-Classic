package dev.tensor.feature.hacks;

import dev.tensor.misc.gui.TensorHUDEditor;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

@Hack.Info(name = "HUDEditor", category = Category.Client, bind = Keyboard.KEY_GRAVE)
public final class HUDEditor extends Hack {

    public final BooleanSetting renderObject = new BooleanSetting("Render Object", true);
    public final BooleanSetting drawOutline = new BooleanSetting("Outline Overlay", false);
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
}
