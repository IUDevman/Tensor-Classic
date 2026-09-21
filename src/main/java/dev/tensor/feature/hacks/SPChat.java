package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.extensions.TensorSPGuiChat;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @since 09-10-2026
 */

@Hack.Info(name = "SPChat", category = Category.Client, enabled = true, drawn = false, description = "Allows for commands to be used in singleplayer.")
public final class SPChat extends Hack {

    @Override
    public void onClientTick() {
        if (this.isMultiplayer()) {
            return;
        }

        if (this.getMinecraft().currentScreen != null) {
            return;
        }

        if (this.getPlayer().isDead) {
            return;
        }

        if (Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindChat.keyCode)) {
            this.getMinecraft().displayGuiScreen(new TensorSPGuiChat());
        }
    }
}
