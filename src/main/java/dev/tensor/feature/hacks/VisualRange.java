package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.variable.PlayerJoinLeaveWorldEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.helpers.block.BoxPos;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 04-24-2026
 */

@Hack.Info(name = "VisualRange", category = Category.Combat)
public final class VisualRange extends Hack {

    public final BooleanSetting antiSpam = new BooleanSetting("Anti Spam", false);
    public final IntegerSetting startUpDelayTicks = new IntegerSetting("Anti Spam Ticks", 5, 1, 20);
    public final EnumSetting playerColor = new EnumSetting("Player Color", ChatColor.GOLD);
    public final EnumSetting friendColor = new EnumSetting("Friend Color", ChatColor.AQUA);

    private final ArrayList<String> loadedEntityNames = new ArrayList<>();

    private int startUpTicks = 0;
    private boolean shouldMessage = false;

    @Override
    public void onDisable(boolean isNull) {
        this.loadedEntityNames.clear();

        this.startUpTicks = 0;
        this.shouldMessage = false;
    }

    @Override
    public void onClientTick() {
        if (!this.shouldMessage && this.startUpTicks < this.startUpDelayTicks.getValue()) {
            this.startUpTicks++;
        } else if (!this.shouldMessage) {
            this.shouldMessage = true;
        }
    }

    @SuppressWarnings("unused")
    private final EventAction<ChangeWorldEvent> changeWorldEventAction = this.addEventAction(new EventAction<>(ChangeWorldEvent.class, event -> this.loadedEntityNames.clear()));

    @SuppressWarnings("unused")
    private final EventAction<PlayerJoinLeaveWorldEvent> playerJoinLeaveWorldEventAction = this.addEventAction(new EventAction<>(PlayerJoinLeaveWorldEvent.class, event -> {
        String name = event.otherPlayerMP.username;

        if (event.otherPlayerMP.isDead || event.otherPlayerMP.health <= 0) {
            this.loadedEntityNames.remove(name);
            return;
        }

        boolean isFriend = this.getFriendManager().isFriend(name);

        BoxPos boxPos = new BoxPos((int) event.otherPlayerMP.posX, (int) event.otherPlayerMP.posY, (int) event.otherPlayerMP.posZ);

        switch (event.type) {
            case JOIN:
                if (!this.loadedEntityNames.contains(name)) {
                    this.loadedEntityNames.add(name);

                    if (this.shouldMessage()) {
                        this.sendVRMessage(isFriend, name, boxPos, true);
                    }
                }
                break;
            case KILL_OR_LEAVE:
                if (this.loadedEntityNames.contains(name)) {
                    this.loadedEntityNames.remove(name);

                    if (this.shouldMessage()) {
                        this.sendVRMessage(isFriend, name, boxPos, false);
                    }
                }
                break;
        }
    }));

    private boolean shouldMessage() {
        if (!this.antiSpam.getValue()) {
            return true;
        }

        return this.shouldMessage;
    }

    private void sendVRMessage(boolean friend, String name, BoxPos position, boolean firstSeen) {
        String player = (friend ? "Friend" : "Player") + " ";
        String seen = " has " + (firstSeen ? ChatColor.GREEN.text() + "entered" : ChatColor.RED.text() + "left") + ChatColor.DEFAULT.text() + " your visual range ";

        double playerX = this.getPlayer().posX;
        double playerY = this.getPlayer().posY;
        double playerZ = this.getPlayer().posZ;

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        if (freecam != null && freecam.isEnabled()) {
            playerX = freecam.oldX;
            playerY = freecam.oldY;
            playerZ = freecam.oldZ;
        }

        int distance = (int) this.getDistance(position.x - playerX, position.y - playerY, position.z - playerZ);

        String coords = "at [" + ChatColor.YELLOW.text() + position.x + ChatColor.DEFAULT.text() +
                ", " + ChatColor.YELLOW.text() + position.y + ChatColor.DEFAULT.text() + ", " +
                ChatColor.YELLOW.text() + position.z + ChatColor.DEFAULT.text() + "; d = " +
                ChatColor.YELLOW.text() + distance + ChatColor.DEFAULT.text() + "]";

        String toSend = player + (friend ? ((ChatColor) this.friendColor.getValue()).text() : ((ChatColor) this.playerColor.getValue()).text()) + name + ChatColor.DEFAULT.text() + seen + coords + "!";

        this.sendClientMessageWithHeader(toSend);
    }
}
