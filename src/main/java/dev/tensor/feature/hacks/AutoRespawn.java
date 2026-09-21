package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import net.minecraft.src.GuiGameOver;

/**
 * @author Hoosiers
 * @since 04-05-2026
 */

@Hack.Info(name = "AutoRespawn", category = Category.Misc, description = "Automatically respawns the player after death")
public final class AutoRespawn extends Hack {

    @Override
    public void onClientTick() {
        if (this.getMinecraft().currentScreen instanceof GuiGameOver) {
            this.getPlayer().respawnPlayer();
        }
    }
}
