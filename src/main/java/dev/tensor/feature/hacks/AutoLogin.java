package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.ChatReceivedEvent;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.EncryptedStringSetting;
import net.minecraft.src.Packet3Chat;

@Hack.Info(name = "AutoLogin", category = Category.Misc)
public final class AutoLogin extends Hack {

    public final EncryptedStringSetting password = new EncryptedStringSetting("Password", "Set your password here. Config is encrypted!");

    @SuppressWarnings("unused")
    public void onChatReceivedEvent(ChatReceivedEvent event) {
        if (this.password.getValue().equals(this.password.getDefaultValue())) {
            return;
        }

        String message = event.message;

        if (message.toLowerCase().contains("/login (your password)")) {
            this.sendPacket(new Packet3Chat("/login " + this.password.getValue()));
        }
    }
}
