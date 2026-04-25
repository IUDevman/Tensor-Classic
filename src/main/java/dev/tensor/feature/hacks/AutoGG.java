package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
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

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 04-24-2026
 */

@Hack.Info(name = "AutoGG", category = Category.Combat)
public final class AutoGG extends Hack {

    public final BooleanSetting stopForSpammer = new BooleanSetting("Pause For Spammer", true);
    public final IntegerSetting delay = new IntegerSetting("Tick Delay", 20, 0, 100);
    public final BooleanSetting greenText = new BooleanSetting("Greentext", false);
    public final BooleanSetting finalHitOnly = new BooleanSetting("Final Hit Only", false);
    public final StringSetting message = new StringSetting("Message", "Good and fair fight, {NAME}... Better luck next time!");

    private final ArrayList<String> targets = new ArrayList<>();

    private int messageDelay = 0;

    @Override
    public void onEnable(boolean isNull) {
        this.messageDelay = this.delay.getValue();
    }

    @Override
    public void onDisable(boolean isNull) {
        this.targets.clear();
    }

    @Override
    public void onClientTick() {
        if (this.messageDelay < 101) {
            this.messageDelay++;
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onChangeWorldEvent(ChangeWorldEvent event) {
        this.targets.clear();
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onPlayerJoinLeaveWorldEvent(PlayerJoinLeaveWorldEvent event) {
        if (!event.type.equals(PlayerJoinLeaveWorldEvent.Type.Kill_or_Leave)) {
            return;
        }

        if (this.targets.isEmpty()) {
            return;
        }

        if (!event.otherPlayerMP.isDead) {
            return;
        }

        String entityName = event.otherPlayerMP.username;

        if (this.targets.contains(entityName)) {

            this.targets.remove(entityName);

            if (this.stopForSpammer.getValue()) {
                PhysicsSpammer spammer = this.getHackManager().getHack(PhysicsSpammer.class);

                if (spammer != null && spammer.isEnabled()) {
                    return;
                }
            }

            if (this.messageDelay >= this.delay.getValue()) {
                String ggMessage = (this.greenText.getValue() ? ">" : "") + this.message.getValue().replace("{NAME}", entityName);

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

            if (this.finalHitOnly.getValue()) {
                int health = otherPlayerMP.health;

                ItemStack currentItem = this.getInventory().getCurrentItem();
                int currentDamage = 1;

                if (currentItem != null) {
                    currentDamage = currentItem.getDamageVsEntity(otherPlayerMP);
                }

                if (health > 0 && health - currentDamage > 0) {
                    return;
                }
            }

            if (!this.targets.contains(otherPlayerMP.username))  {
                this.targets.add(otherPlayerMP.username);
            }
        }
    }
}
