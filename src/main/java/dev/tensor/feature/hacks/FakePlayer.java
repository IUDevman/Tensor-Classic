package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.betafix.BetaFix;
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

@Hack.Info(name = "FakePlayer", category = Category.Misc, description = "Spawns a fake player. Useful for testing NameTags or other renders.")
public final class FakePlayer extends Hack {

    @Override
    public boolean shouldDisableOnDisconnect() {
        return true;
    }

    @Override
    public boolean shouldDisableOnDeath() {
        return true;
    }

    @Override
    public boolean shouldDisableOnWorldChange() {
        return true;
    }

    public final BooleanSetting geared = new BooleanSetting("Geared", true) {

        @Override
        public void setValue(Boolean VALUE) {
            super.setValue(VALUE);

            if (this.failsNullCheck()) {
                return;
            }

            FakePlayer fakePlayer = this.getHackManager().getHack(FakePlayer.class);

            if (fakePlayer != null && fakePlayer.isEnabled() && fakePlayer.fakePlayer != null) {

                if (VALUE) {
                    fakePlayer.fakePlayer.inventory.mainInventory[0] = new ItemStack(Item.swordDiamond);
                    fakePlayer.fakePlayer.inventory.armorInventory[0] = new ItemStack(Item.bootsDiamond);
                    fakePlayer.fakePlayer.inventory.armorInventory[1] = new ItemStack(Item.legsDiamond);
                    fakePlayer.fakePlayer.inventory.armorInventory[2] = new ItemStack(Item.plateDiamond);
                    fakePlayer.fakePlayer.inventory.armorInventory[3] = new ItemStack(Item.helmetDiamond);

                } else {
                    fakePlayer.fakePlayer.inventory.mainInventory[0] = null;
                    fakePlayer.fakePlayer.inventory.armorInventory[0] = null;
                    fakePlayer.fakePlayer.inventory.armorInventory[1] = null;
                    fakePlayer.fakePlayer.inventory.armorInventory[2] = null;
                    fakePlayer.fakePlayer.inventory.armorInventory[3] = null;
                }
            }
        }
    };
    public final StringSetting name = new StringSetting("Name", "Go_Hoosiers") {

        @Override
        public void setValue(String VALUE) {
            super.setValue(VALUE);

            if (this.failsNullCheck()) {
                return;
            }

            FakePlayer fakePlayer = this.getHackManager().getHack(FakePlayer.class);

            if (fakePlayer != null && fakePlayer.isEnabled() && fakePlayer.fakePlayer != null) {
                fakePlayer.fakePlayer.username = VALUE;
                fakePlayer.fakePlayer.skinUrl = BetaFix.getSkinURL(VALUE);
            }
        }
    };

    public EntityPlayer fakePlayer = null;

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
            this.fakePlayer.skinUrl = BetaFix.getSkinURL(this.name.getValue());

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

    @Override
    public void onClientTick() {
        if (this.fakePlayer != null) {
            this.fakePlayer.motionX = 0;
            this.fakePlayer.motionY = 0;
            this.fakePlayer.motionZ = 0;
        }
    }
}
