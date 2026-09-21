package dev.tensor.feature.hacks;

import dev.tensor.backend.mixins.accessors.PlayerControllerMPAccessor;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import net.minecraft.src.PlayerControllerMP;

/**
 * @author Hoosiers
 * @since 04-16-2026
 */

@Hack.Info(name = "NoBreakDelay", category = Category.Player, description = "Negates the 5-tick block delay after breaking a block.")
public final class NoBreakDelay extends Hack {

    @Override
    public void onClientTick() {
        if (this.getMinecraft().playerController instanceof PlayerControllerMP) {

            PlayerControllerMP controllerMP = (PlayerControllerMP) this.getMinecraft().playerController;

            if (((PlayerControllerMPAccessor) controllerMP).tensor_getBlockHitDelay() > 0) {
                ((PlayerControllerMPAccessor) controllerMP).tensor_setBlockHitDelay(0);
            }
        }
    }
}
