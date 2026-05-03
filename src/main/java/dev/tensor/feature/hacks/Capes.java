package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;

import java.util.List;

/**
 * @author Hoosiers
 * @since 05-02-2026
 */

@Hack.Info(name = "Capes", category = Category.Client, enabled = true)
public final class Capes extends Hack {

    @Override
    public void onEnable(boolean isNull) {
        if (isNull) {
            return;
        }

        this.updateCapes();
    }

    @Override
    public void onDisable(boolean isNull) {
        if (isNull) {
            return;
        }

        this.updateCapes();
    }

    private void updateCapes() {
        List entities = this.getWorld().loadedEntityList;

        for (Object object : entities) {

            Entity entity = (Entity) object;

            if (entity == null) {
                continue;
            }

            if (entity instanceof EntityPlayer) {
                entity.updateCloak();
            }
        }
    }
}
