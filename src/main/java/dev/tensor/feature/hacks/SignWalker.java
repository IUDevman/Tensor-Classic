package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.SignCollisionEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @since 06-09-2026
 */

@Hack.Info(name = "SignWalker", category = Category.Movement)
public final class SignWalker extends Hack {

    public final IntegerSetting offset = new IntegerSetting("Check Offset", -2, -3, -1);
    public final BooleanSetting sneak = new BooleanSetting("Sneak Down", false);

    @SuppressWarnings("unused")
    EventAction<SignCollisionEvent> signCollisionEventAction = this.addEventAction(new EventAction<>(SignCollisionEvent.class, event -> {

        if (this.sneak.getValue() && this.getMinecraft().currentScreen == null && Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindSneak.keyCode)) {
            return;
        }

        double playerY = this.getPlayer().posY;

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        if (freecam != null && freecam.isEnabled()) {
            playerY = freecam.oldY;
        }

        if (event.signY <= playerY + this.offset.getValue()) {
            event.cancel();
        }
    }));
}
