package dev.tensor.feature.hacks;

import dev.tensor.misc.gui.TensorHUDEditor;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

@Hack.Info(name = "HUDEditor", category = Category.Client, bind = Keyboard.KEY_GRAVE)
public final class HUDEditor extends Hack {

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
