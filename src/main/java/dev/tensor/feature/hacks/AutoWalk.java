package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @since 04-02-2026
 */

@Hack.Info(name = "AutoWalk", category = Category.Movement)
public final class AutoWalk extends Hack {

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull) {
            if (this.getMinecraft().currentScreen != null) {
                this.updateMove(false);
                return;
            }

            if (!Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindForward.keyCode)) {
                this.updateMove(false);
            }
        }
    }

    @Override
    public void onClientTick() {
        if (this.getMinecraft().currentScreen != null) {
            this.updateMove(true);
            return;
        }

        this.updateMove(!Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindBack.keyCode));
    }

    private void updateMove(boolean enabled) {
        this.getPlayer().handleKeyPress(this.getMinecraft().gameSettings.keyBindForward.keyCode, enabled);
    }
}
