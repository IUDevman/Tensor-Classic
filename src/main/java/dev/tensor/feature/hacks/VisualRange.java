package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.variable.PlayerJoinLeaveWorldEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.helpers.block.BlockPos;
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

    public final BooleanSetting antiSpam = new BooleanSetting("Anti Spam", true);
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
    @EventTarget
    public void onChangeWorldEvent(ChangeWorldEvent event) {
        this.loadedEntityNames.clear();
        this.startUpTicks = 0;
        this.shouldMessage = false;
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onPlayerJoinLeaveWorldEvent(PlayerJoinLeaveWorldEvent event) {
        String name = event.otherPlayerMP.username;

        if (event.otherPlayerMP.isDead) {
            this.loadedEntityNames.remove(name);
            return;
        }

        boolean isFriend = this.getFriendManager().isFriend(name);

        BlockPos blockPos = new BlockPos((int) event.otherPlayerMP.posX, (int) event.otherPlayerMP.posY, (int) event.otherPlayerMP.posZ);

        switch (event.type) {
            case Join:
                if (!this.loadedEntityNames.contains(name)) {
                    this.loadedEntityNames.add(name);

                    if (this.shouldMessage()) {
                        this.sendVRMessage(isFriend, name, blockPos, true);
                    }
                }
                break;
            case Kill_or_Leave:
                if (this.loadedEntityNames.contains(name)) {
                    this.loadedEntityNames.remove(name);

                    if (this.shouldMessage()) {
                        this.sendVRMessage(isFriend, name, blockPos, false);
                    }
                }
                break;
        }
    }

    private boolean shouldMessage() {
        if (!this.antiSpam.getValue()) {
            return true;
        }

        return this.shouldMessage;
    }

    private void sendVRMessage(boolean friend, String name, BlockPos position, boolean firstSeen) {
        String player = (friend ? "Friend" : "Player") + " ";
        String seen = " has " + (firstSeen ? this.getColor(ChatColor.GREEN) + "entered" : this.getColor(ChatColor.RED) + "left") + this.getColor(ChatColor.DEFAULT) + " your visual range ";

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

        String coords = "at [" + this.getColor(ChatColor.YELLOW) + position.x + this.getColor(ChatColor.DEFAULT) +
                ", " + this.getColor(ChatColor.YELLOW) + position.y + this.getColor(ChatColor.DEFAULT) + ", " +
                this.getColor(ChatColor.YELLOW) + position.z + this.getColor(ChatColor.DEFAULT) + "; d = " +
                this.getColor(ChatColor.YELLOW) + distance + this.getColor(ChatColor.DEFAULT) + "]";

        String toSend = player + (friend ? this.getColor((ChatColor) this.friendColor.getValue()) : this.getColor((ChatColor) this.playerColor.getValue())) + name + this.getColor(ChatColor.DEFAULT) + seen + coords + "!";

        this.sendClientMessageWithHeader(toSend);
    }
}
