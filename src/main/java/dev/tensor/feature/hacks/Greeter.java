package dev.tensor.feature.hacks;

import dev.tensor.backend.events.ChatReceivedEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.StringSetting;
import net.minecraft.src.Packet3Chat;

/**
 * @author Hoosiers
 * @since 03-21-2026
 */

@Hack.Info(name = "Greeter", category = Category.Misc)
public final class Greeter extends Hack {

    public final BooleanSetting greenText = new BooleanSetting("Greentext", true);
    public final BooleanSetting join = new BooleanSetting("Join", true);
    public final StringSetting joinMessage = new StringSetting("Join Message", "Welcome {name} to the server! Hope you have a good time! ;)");
    public final BooleanSetting leave = new BooleanSetting("Leave", true);
    public final StringSetting leaveMessage = new StringSetting("Leave Message", "Goodbye {name} from the server! Sorry to see you go! ;C");

    @SuppressWarnings("unused")
    @EventTarget
    public void onChatReceivedEvent(ChatReceivedEvent event) {

        String message = event.message;

        if (message == null) {
            return;
        }

        Type messageType = this.getType(message.toLowerCase());

        if (messageType == Type.None) {
            return;
        }

        String[] split = message.split(" ");

        if (split.length < 1) {
            return;
        }

        String playerName = split[0];

        if (playerName.startsWith("§")) {
            playerName = playerName.substring(2);
        }

        if (playerName.equalsIgnoreCase(this.getPlayer().username)) {
            return;
        }

        String greeter = (this.greenText.getValue() ? ">" : "");

        if (messageType == Type.Join && this.join.getValue()) {

            greeter += this.joinMessage.getValue().replace("{name}", playerName);

            this.sendPacket(new Packet3Chat(greeter));

        } else if (messageType == Type.Leave && this.leave.getValue()) {

            greeter += this.leaveMessage.getValue().replace("{name}", playerName);

            this.sendPacket(new Packet3Chat(greeter));
        }
    }

    private Type getType(String message) {
        if (message.contains("joined the game.")) {
            return Type.Join;
        } else if (message.contains("left the game.")) {
            return Type.Leave;
        } else {
            return Type.None;
        }
    }

    private enum Type {
        Join,
        Leave,
        None
    }
}
