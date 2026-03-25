package dev.tensor.feature.hacks;

import dev.tensor.backend.events.ChatReceivedEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.EncryptedStringSetting;
import net.minecraft.src.Packet3Chat;

@Hack.Info(name = "AutoLogin", category = Category.Misc)
public final class AutoLogin extends Hack {

    public final EncryptedStringSetting password = new EncryptedStringSetting("Password", "TEST");

    @SuppressWarnings("unused")
    @EventTarget
    public void onChatReceivedEvent(ChatReceivedEvent event) {

        String message = event.message;

        if (message.toLowerCase().contains("/login (your password)"))  {
            this.getTensorLogger().info("/login " + this.password.getValue());
            this.sendPacket(new Packet3Chat("/login " + this.password.getValue()));
        }
    }
}
