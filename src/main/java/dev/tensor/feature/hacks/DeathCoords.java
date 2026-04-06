package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import net.minecraft.src.GuiGameOver;

/**
 * @author Hoosiers
 * @since 04-05-2026
 */

@Hack.Info(name = "DeathCoords", category = Category.Misc)
public final class DeathCoords extends Hack {

    private boolean messaged = false;

    @Override
    public void onEnable(boolean isNull) {
        this.messaged = false;
    }

    @Override
    public void onClientTick() {

        if (this.getMinecraft().currentScreen instanceof GuiGameOver) {

            if (!this.messaged) {
                this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + "You died at x = " + this.getColor(ChatColor.AQUA)
                        + ((int) this.getPlayer().posX) + this.getColor(ChatColor.RED) + ", y = " + this.getColor(ChatColor.AQUA)
                        + ((int) this.getPlayer().posY) + this.getColor(ChatColor.RED) + ", z = " + this.getColor(ChatColor.AQUA)
                        + ((int) this.getPlayer().posZ) + this.getColor(ChatColor.RED) + "!");
            }

            this.messaged = true;

        } else {
            this.messaged = false;
        }
    }
}
