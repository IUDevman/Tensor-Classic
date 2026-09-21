package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import net.minecraft.src.EntityPlayer;

/**
 * @author Hoosiers
 * @since 05-02-2026
 */

@Hack.Info(name = "Capes", category = Category.Client, enabled = true, description = "Gives you and any defined players the custom Tensor cape.")
public final class Capes extends Hack {

    @Override
    public void onEnable(boolean isNull) {
        if (isNull) {
            return;
        }

        this.refreshCapes();
    }

    @Override
    public void onDisable(boolean isNull) {
        if (isNull) {
            return;
        }

        this.refreshCapes();
    }

    public void refreshCapes() {
        this.getFilteredLoadedEntities().parallelStream().forEach(entity -> {
            if (entity instanceof EntityPlayer) {
                entity.updateCloak();
            }
        });
    }
}
