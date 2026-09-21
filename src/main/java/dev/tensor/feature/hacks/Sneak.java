package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import net.minecraft.src.Packet19EntityAction;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @since 04-01-2026
 */

@Hack.Info(name = "Sneak", category = Category.Movement, description = "Causes the player to sneak server-sided.")
public final class Sneak extends Hack {

    public final BooleanSetting limitSpeed = new BooleanSetting("Limit Speed", true);
    public final DoubleSetting speedLimiter = new DoubleSetting("Limit Multiplier", 0.5, 0.0, 1.0, 1);

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull && !Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindSneak.keyCode)) {
            this.sendPacket(new Packet19EntityAction(this.getPlayer(), 2));
        }
    }

    @Override
    public void onClientTick() {
        if (this.limitSpeed.getValue()) {

            //Vanilla multiplier is 0.3; Max multiplier on NoCheat servers is 0.5.
            this.getPlayer().motionX *= this.speedLimiter.getValue();
            this.getPlayer().motionZ *= this.speedLimiter.getValue();
        }

        this.sendPacket(new Packet19EntityAction(this.getPlayer(), 1));
    }
}
