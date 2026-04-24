package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.AttackEntityEvent;
import dev.tensor.backend.events.variable.PlayerJoinLeaveWorldEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import dev.tensor.misc.imp.setting.settings.StringSetting;
import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet3Chat;

/**
 * @author Hoosiers
 * @since 04-24-2026
 */

@Hack.Info(name = "AutoGG", category = Category.Combat)
public final class AutoGG extends Hack {

    public final BooleanSetting stopForSpammer = new BooleanSetting("Pause For Spammer", true);
    public final IntegerSetting delay = new IntegerSetting("Tick Delay", 20, 0, 100);
    public final BooleanSetting greenText = new BooleanSetting("Greentext", false);
    public final BooleanSetting finalHitOnly = new BooleanSetting("Final Hit Only", true);
    public final StringSetting message = new StringSetting("Message", "Good and fair fight, {NAME}... You can't handle the strength of my {WEAPON}}!");

    private String prevTargeted = "";
    private String prevWeapon = "Hand";

    private int messageDelay = 0;

    @Override
    public void onEnable(boolean isNull) {
        this.messageDelay = this.delay.getValue();
    }

    @Override
    public void onDisable(boolean isNull) {
        this.prevTargeted = "";
        this.prevWeapon = "Hand";
    }

    @Override
    public void onClientTick() {
        if (this.messageDelay < 1000) {
            this.messageDelay++;
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onPlayerJoinLeaveWorldEvent(PlayerJoinLeaveWorldEvent event) {
        if (!event.type.equals(PlayerJoinLeaveWorldEvent.Type.Kill_or_Leave)) {
            return;
        }

        if (this.prevTargeted.isEmpty()) {
            return;
        }

        if (!event.otherPlayerMP.isDead) {
            return;
        }

        String entityName = event.otherPlayerMP.username;

        if (this.prevTargeted.equalsIgnoreCase(entityName)) {

            this.prevTargeted = "";
            this.prevWeapon = "Hand";

            if (this.stopForSpammer.getValue()) {
                PhysicsSpammer spammer = this.getHackManager().getHack(PhysicsSpammer.class);

                if (spammer != null && spammer.isEnabled()) {
                    return;
                }
            }

            if (this.messageDelay >= this.delay.getValue()) {
                String ggMessage = (this.greenText.getValue() ? ">" : "") + this.message.getValue().replace("{NAME}", entityName).replace("{WEAPON}", this.prevWeapon);

                if (ggMessage.length() > 100) {
                    return;
                }

                this.sendPacket(new Packet3Chat(ggMessage));

                this.messageDelay = 0;
            }
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onAttackEntityEvent(AttackEntityEvent event) {
        if (event.entityHit instanceof EntityOtherPlayerMP) {
            EntityOtherPlayerMP otherPlayerMP = (EntityOtherPlayerMP) event.entityHit;

            ItemStack currentItem = this.getInventory().getCurrentItem();

            if (this.finalHitOnly.getValue()) {
                int health = otherPlayerMP.health;

                int damage = 1;

                if (currentItem != null) {
                    damage = currentItem.getDamageVsEntity(otherPlayerMP);
                }

                if (health - damage > 0) {
                    return;
                }
            }

            this.prevTargeted = otherPlayerMP.username;

            if (currentItem == null) {
                this.prevWeapon = "Hand";

            } else {
                this.prevWeapon = currentItem.getItemName();
            }
        }
    }
}
