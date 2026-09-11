package dev.tensor.misc.helpers.extensions;

import dev.tensor.misc.imp.Globals;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.GuiChat;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @since 09-10-2026
 */

public final class TensorSPGuiChat extends GuiChat implements Globals {

    private final String allowedCharacters = ChatAllowedCharacters.allowedCharacters;

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char keyChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.getMinecraft().displayGuiScreen(null);
            return;
        }

        if (keyCode == Keyboard.KEY_RETURN) {
            String trimmed = this.message.trim();

            if (!trimmed.isEmpty()) {
                this.getCommandManager().processCommand(trimmed);
            }

            this.getMinecraft().displayGuiScreen(null);
            return;
        }

        if (keyCode == Keyboard.KEY_BACK && !this.message.isEmpty()) {
            this.message = this.message.substring(0, this.message.length() - 1);
            return;
        }

        if (this.allowedCharacters.indexOf(keyChar) >= 0 && this.message.length() < 100) {
            this.message = this.message + keyChar;
        }
    }
}
