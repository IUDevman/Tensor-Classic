package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.variable.AttackEntityEvent;
import dev.tensor.backend.events.variable.PlayerJoinLeaveWorldEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import dev.tensor.misc.imp.setting.settings.StringSetting;
import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.Packet3Chat;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 04-24-2026
 */

@Hack.Info(name = "AutoGG", category = Category.Combat, description = "Automatically sends a funny message whenever you kill a player.")
public final class AutoGG extends Hack {

    public final BooleanSetting stopForSpammer = new BooleanSetting("Pause For Spammer", true);
    public final IntegerSetting delay = new IntegerSetting("Tick Delay", 20, 0, 100);
    public final BooleanSetting greenText = new BooleanSetting("Greentext", false);
    public final BooleanSetting finalHitOnly = new BooleanSetting("Final Hit Only", true);
    public final StringSetting message = new StringSetting("Message", "Good and fair fight, {NAME}... Better luck next time!", "Message to be sent when AutoGG is triggered.");

    private final ArrayList<String> targets = new ArrayList<>();
    private String lastHitName = "";

    private int messageDelay = 0;

    @Override
    public void onEnable(boolean isNull) {
        this.messageDelay = this.delay.getValue();
    }

    @Override
    public void onDisable(boolean isNull) {
        this.targets.clear();
        this.lastHitName = "";
    }

    @Override
    public void onClientTick() {
        if (this.messageDelay < 101) {
            this.messageDelay++;
        }
    }

    @SuppressWarnings("unused")
    private final EventAction<ChangeWorldEvent> changeWorldEventAction = this.addHackEventAction(new EventAction<>(ChangeWorldEvent.class, event -> {
        this.targets.clear();
        this.lastHitName = "";
    }));

    @SuppressWarnings("unused")
    private final EventAction<PlayerJoinLeaveWorldEvent> playerJoinLeaveWorldEventAction = this.addHackEventAction(new EventAction<>(PlayerJoinLeaveWorldEvent.class, event -> {
        if (!event.type.equals(PlayerJoinLeaveWorldEvent.Type.KILL_OR_LEAVE)) {
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

            if (this.finalHitOnly.getValue() && !this.lastHitName.equalsIgnoreCase(entityName)) {
                this.targets.remove(entityName);
                return;
            }

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
    }));

    @SuppressWarnings("unused")
    private final EventAction<AttackEntityEvent> attackEntityEventAction = this.addHackEventAction(new EventAction<>(AttackEntityEvent.class, event -> {
        if (event.entityHit instanceof EntityOtherPlayerMP) {
            EntityOtherPlayerMP otherPlayerMP = (EntityOtherPlayerMP) event.entityHit;

            String username = otherPlayerMP.username;

            this.lastHitName = username;

            if (!this.targets.contains(username)) {
                this.targets.add(username);
            }
        }
    }));
}
