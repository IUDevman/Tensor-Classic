package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.VelocityEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @since 05-08-2026
 */

/**
 * Pretty much just HephFreecam but without the camera part.
 * Ported over for the > rare < servers that do not use NoCheat
 *
 * @credit q7e
 * @source https://github.com/qe7/Hephaestus
 */

@Hack.Info(name = "BlatantFly", category = Category.Movement)
public final class BlatantFly extends Hack {

    public final IntegerSetting speed = new IntegerSetting("Speed", 10, 1, 20);

    @Override
    public void onClientTick() {
        this.getPlayer().onGround = true;

        this.getPlayer().motionY = 0;

        if (this.getMinecraft().currentScreen != null) {
            InventoryMove inventoryMove = this.getHackManager().getHack(InventoryMove.class);

            if (!(inventoryMove.isEnabled() && !inventoryMove.shouldNotMove())) {
                this.setPlayerFlyingSpeed(0.0D);
                return;
            }
        }

        if (Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindJump.keyCode)) {
            this.getPlayer().motionY += (this.speed.getValue() / 10.0D);
        } else if (Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindSneak.keyCode)) {
            this.getPlayer().motionY -= (this.speed.getValue() / 10.0D);
        }

        if (this.isPlayerMoving2D()) {
            this.setPlayerFlyingSpeed((this.speed.getValue() / 10.0D));
        } else {
            this.setPlayerFlyingSpeed(0.0D);
        }
    }

    @SuppressWarnings("unused")
    private final EventAction<VelocityEvent> velocityEventAction = this.addEventAction(new EventAction<>(VelocityEvent.class, event -> {
        if (event.type.equals(VelocityEvent.Type.BLOCK) || event.type.equals(VelocityEvent.Type.FLUID)) {
            event.cancel();
        }
    }));
}
