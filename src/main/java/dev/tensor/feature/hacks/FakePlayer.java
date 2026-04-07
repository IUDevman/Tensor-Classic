package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.StringSetting;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

/**
 * @author Hoosiers
 * @since 04-06-2026
 */

@Hack.Info(name = "FakePlayer", category = Category.Misc)
public final class FakePlayer extends Hack {

    public final BooleanSetting geared = new BooleanSetting("Geared", true);
    public final StringSetting name = new StringSetting("Name", "Hoosiers");

    private EntityPlayer fakePlayer = null;

    @Override
    public void onEnable(boolean isNull) {
        if (!isNull) {

            this.fakePlayer = new dev.tensor.misc.helpers.entity.FakePlayer(this.getWorld());

            if (this.geared.getValue()) {
                this.fakePlayer.inventory.mainInventory[0] = new ItemStack(Item.swordDiamond);
                this.fakePlayer.inventory.armorInventory[0] = new ItemStack(Item.bootsDiamond);
                this.fakePlayer.inventory.armorInventory[1] = new ItemStack(Item.legsDiamond);
                this.fakePlayer.inventory.armorInventory[2] = new ItemStack(Item.plateDiamond);
                this.fakePlayer.inventory.armorInventory[3] = new ItemStack(Item.helmetDiamond);
            }

            this.fakePlayer.username = this.name.getValue();
            this.fakePlayer.setPosition(this.getPlayer().posX, this.getPlayer().posY, this.getPlayer().posZ);

            this.getWorld().entityJoinedWorld(this.fakePlayer);
        }
    }

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull && this.fakePlayer != null) {
            this.getWorld().setEntityDead(fakePlayer);
        }

        this.fakePlayer = null;
    }
}
