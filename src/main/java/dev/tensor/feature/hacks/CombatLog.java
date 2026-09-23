package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.DamageOrAttackPlayerEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet255KickDisconnect;
import net.minecraft.src.Packet3Chat;

/**
 * @author Hoosiers
 * @since 09-17-2026
 */

@Hack.Info(name = "CombatLog", category = Category.Combat, description = "Automatically logs out if you take damage or have low health.")
public final class CombatLog extends Hack {

    public final EnumSetting mode = new EnumSetting("Mode", Mode.Health, new String[]{
            "This is what triggers the hack to log out.",
            "Player Attack® Logs out if attacked by a player.",
            "Damage® Logs out if any damage is taken.",
            "Health® Logs out if below a defined health.",
            "Health No Food® Logs out if below a defined health and no food is in the inventory for AutoEat."
    });
    public final DoubleSetting health = new DoubleSetting("Min Health", 4.0, 0.5, 10.0, 1);
    public final EnumSetting type = new EnumSetting("Type", Type.Disconnect, new String[]{
            "This is how the player leaves the game.",
            "Disconnect® Standard disconnect packet. Can sometimes bug relogging.",
            "Kick® Sends an invalid chat message, causing the server to kick you."
    });

    @Override
    public void onClientTick() {
        if (!this.isMultiplayer()) {
            return;
        }

        if (this.getPlayer().ticksExisted <= 10) {
            return;
        }

        if (this.getPlayer().isDead) {
            return;
        }

        if (!this.mode.getValue().equals(Mode.Health) && !this.mode.getValue().equals(Mode.Health_No_Food)) {
            return;
        }

        if (this.getPlayer().health <= this.health.getValue()) {

            if (this.mode.getValue().equals(Mode.Health)) {
                this.disconnect();
                return;
            }

            if (this.mode.getValue().equals(Mode.Health_No_Food) && this.isOutOfFood()) {
                this.disconnect();
            }
        }
    }

    @SuppressWarnings("unused")
    private final EventAction<DamageOrAttackPlayerEvent> damageOrAttackPlayerEvenAction = this.addHackEventAction(new EventAction<>(DamageOrAttackPlayerEvent.class, event -> {
        if (this.getPlayer().ticksExisted <= 10) {
            return;
        }

        if (this.getPlayer().isDead) {
            return;
        }

        switch ((Mode) this.mode.getValue()) {
            case Health:
            case Health_No_Food:
                return;
            case Player_Attack:
                if (event.type.equals(DamageOrAttackPlayerEvent.Type.Attack)) {
                    this.disconnect();
                }
                break;
            case Damage:
                if (event.type.equals(DamageOrAttackPlayerEvent.Type.Damage)) {
                    this.disconnect();
                }
                break;
        }
    }));

    private void disconnect() {
        switch ((Type) this.type.getValue()) {
            case Kick:
                this.sendPacketBypass(new Packet3Chat(""));
                break;
            default:
                this.sendPacket(new Packet255KickDisconnect("Quitting"));
                break;
        }
    }

    private boolean isOutOfFood() {
        boolean hasEmpty = false;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = this.getPlayer().inventory.mainInventory[i];

            if (stack == null || stack.getItem() == null) {
                hasEmpty = true;
                continue;
            }

            if (stack.getItem() instanceof ItemFood) {
                return false;
            }
        }

        if (hasEmpty) {
            AutoEat autoEat = this.getHackManager().getHack(AutoEat.class);

            if (autoEat != null && autoEat.isEnabled()) {
                for (int i = 9; i < 36; i++) {
                    ItemStack stack = this.getPlayer().inventory.mainInventory[i];

                    if (stack == null || stack.getItem() == null) {
                        continue;
                    }

                    if (stack.getItem() instanceof ItemFood) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public enum Mode {
        Player_Attack,
        Damage,
        Health,
        Health_No_Food
    }

    public enum Type {
        Disconnect,
        Kick
    }
}
