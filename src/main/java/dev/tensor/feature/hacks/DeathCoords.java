package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.ChatColor;
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
                this.sendClientMessageWithHeader(ChatColor.RED.text() + "You died at x = " + ChatColor.AQUA.text()
                        + ((int) this.getPlayer().posX) + ChatColor.RED.text() + ", y = " + ChatColor.AQUA.text()
                        + ((int) this.getPlayer().posY) + ChatColor.RED.text() + ", z = " + ChatColor.AQUA.text()
                        + ((int) this.getPlayer().posZ) + ChatColor.RED.text() + "!");
            }

            this.messaged = true;

        } else {
            this.messaged = false;
        }
    }
}
