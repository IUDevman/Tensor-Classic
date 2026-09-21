package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;

/**
 * @author Hoosiers
 * @since 04-02-2026
 */

@Hack.Info(name = "Step", category = Category.Movement, description = "Automatically steps up blocks.")
public final class Step extends Hack {

    //Step height of 1 for full blocks, 1.2 for block + trapdoor, 1.5 for half slab (this and above blocked by NoCheat)
    public final DoubleSetting height = new DoubleSetting("Height", 1.2, 0.5, 2.0, 1);

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull) {
            this.getPlayer().stepHeight = 0.5F;
        }
    }

    @Override
    public void onClientTick() {
        double newValue = this.height.getValue();

        this.getPlayer().stepHeight = (float) newValue;
    }
}
