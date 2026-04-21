package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.ChatReceivedEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import dev.tensor.misc.imp.setting.settings.StringSetting;
import net.minecraft.src.Packet3Chat;

/**
 * @author Hoosiers
 * @since 03-21-2026
 */

@Hack.Info(name = "Greeter", category = Category.Misc)
public final class Greeter extends Hack {

    public final BooleanSetting stopForSpammer = new BooleanSetting("Pause For Spammer", true);
    public final IntegerSetting delay = new IntegerSetting("Tick Delay", 20, 0, 100);
    public final BooleanSetting greenText = new BooleanSetting("Greentext", true);
    public final BooleanSetting join = new BooleanSetting("Join", true);
    public final StringSetting joinMessage = new StringSetting("Join Message", "Welcome {name} to the server! Hope you have a good time! ;)");
    public final BooleanSetting leave = new BooleanSetting("Leave", true);
    public final StringSetting leaveMessage = new StringSetting("Leave Message", "Goodbye {name} from the server! Sorry to see you go! ;C");

    private int messageDelay = 0;

    @Override
    public void onEnable(boolean nullCheck) {
        this.messageDelay = this.delay.getValue();
    }

    @Override
    public void onClientTick() {
        this.messageDelay++;
    }

    @SuppressWarnings("unused")
    @EventTarget()
    public void onChatReceivedEvent(ChatReceivedEvent event) {
        if (this.shouldStopForSpammer()) {
            return;
        }

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

        if (this.messageDelay >= this.delay.getValue()) {
            String greeter = (this.greenText.getValue() ? ">" : "");

            if (messageType == Type.Join && this.join.getValue()) {
                greeter += this.joinMessage.getValue().replace("{name}", playerName);

            } else if (messageType == Type.Leave && this.leave.getValue()) {
                greeter += this.leaveMessage.getValue().replace("{name}", playerName);
            }

            if (this.messageDelay < this.delay.getValue()) {
                return;
            }

            if (greeter.length() > 100) {
                return;
            }

            this.sendPacket(new Packet3Chat(greeter));

            this.messageDelay = 0;
        }
    }

    private boolean shouldStopForSpammer() {
        if (!this.stopForSpammer.getValue()) {
            return false;
        }

        PhysicsSpammer physicsSpammer = this.getHackManager().getHack(PhysicsSpammer.class);

        return physicsSpammer.isEnabled();
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
