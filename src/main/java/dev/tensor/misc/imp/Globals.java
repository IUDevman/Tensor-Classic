package dev.tensor.misc.imp;

import dev.tensor.TensorClassic;
import dev.tensor.backend.mixins.accessors.GuiIngameAccessor;
import dev.tensor.backend.mixins.accessors.NetClientHandlerAccessor;
import dev.tensor.backend.mixins.accessors.NetworkManagerAccessor;
import dev.tensor.feature.hacks.AutoReconnect;
import dev.tensor.feature.hacks.HephFreecam;
import dev.tensor.feature.hacks.Xray;
import dev.tensor.feature.managers.*;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.Event;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.helpers.rotation.Rotation;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.slf4j.Logger;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public interface Globals {

    default Minecraft getMinecraft() {
        return (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
    }

    default EntityPlayerSP getPlayer() {
        return this.getMinecraft().thePlayer;
    }

    default World getWorld() {
        return this.getMinecraft().theWorld;
    }

    default FontRenderer getFontRenderer() {
        return this.getMinecraft().fontRenderer;
    }

    default RenderEngine getRenderEngine() {
        return this.getMinecraft().renderEngine;
    }

    default GuiIngame getInGameGUI() {
        return this.getMinecraft().ingameGUI;
    }

    default InventoryPlayer getInventory() {
        return this.getPlayer().inventory;
    }

    default TensorClassic getTensor() {
        return TensorClassic.getInstance();
    }

    default Logger getTensorLogger() {
        return TensorClassic.getLogger();
    }

    default EventManager getEventManager() {
        return (EventManager) this.getTensor().MANAGERS.get(EventManager.class);
    }

    default ConfigManager getConfigManager() {
        return (ConfigManager) this.getTensor().MANAGERS.get(ConfigManager.class);
    }

    default FriendManager getFriendManager() {
        return (FriendManager) this.getTensor().MANAGERS.get(FriendManager.class);
    }

    default CapeManager getCapeManager() {
        return (CapeManager) this.getTensor().MANAGERS.get(CapeManager.class);
    }

    default DiscordManager getDiscordManager() {
        return (DiscordManager) this.getTensor().MANAGERS.get(DiscordManager.class);
    }

    default RotationManager getRotationManager() {
        return (RotationManager) this.getTensor().MANAGERS.get(RotationManager.class);
    }

    default EntityListManager getEntityListManager() {
        return (EntityListManager) this.getTensor().MANAGERS.get(EntityListManager.class);
    }

    default HackManager getHackManager() {
        return (HackManager) this.getTensor().MANAGERS.get(HackManager.class);
    }

    default CommandManager getCommandManager() {
        return (CommandManager) this.getTensor().MANAGERS.get(CommandManager.class);
    }

    default AutoReconnect getAutoReconnect() {
        return this.getHackManager().getHack(AutoReconnect.class);
    }

    default Xray getXray() {
        return this.getHackManager().getHack(Xray.class);
    }

    default boolean isXrayEnabled() {
        Xray xray = this.getXray();

        if (xray == null) {
            return false;
        }

        return xray.isEnabled();
    }

    default boolean isAutoReconnectEnabled() {
        AutoReconnect autoReconnect = this.getAutoReconnect();

        if (autoReconnect == null) {
            return false;
        }

        return autoReconnect.isEnabled();
    }

    default boolean shouldCallEvent(Class<? extends Event<?>> eventClass) {
        return this.getEventManager().shouldCall(eventClass);
    }

    default <T extends Event<T>> EventAction<T> addEventAction(EventAction<T> action) {
        this.getEventManager().addAction(action);
        return action;
    }

    default ArrayList<Entity> getFilteredLoadedEntities() {
        return this.getEntityListManager().getFilteredEntities();
    }

    default boolean failsNullCheck() {
        return this.getMinecraft() == null || this.getPlayer() == null || this.getWorld() == null;
    }

    default boolean isMultiplayer() {
        return this.getWorld().multiplayerWorld;
    }

    default void sendPacket(Packet packet) {
        if (this.getMinecraft().getSendQueue() == null) {
            return;
        }

        this.getMinecraft().getSendQueue().addToSendQueue(packet);
    }

    default void sendPacketBypass(Packet packet) {
        if (this.getMinecraft().getSendQueue() == null) {
            return;
        }

        NetworkManager networkManager = ((NetClientHandlerAccessor) this.getMinecraft().getSendQueue()).tensor_getNetManager();

        if (networkManager == null) {
            return;
        }

        networkManager.addToSendQueue(packet);
    }

    default void resetWorld() {
        if (this.failsNullCheck()) {
            return;
        }

        this.getMinecraft().renderGlobal.loadRenderers();
    }

    default String getHeader() {
        return ChatColor.LIGHT_GRAY.text() + "[" + ChatColor.YELLOW.text() + TensorClassic.MOD_NAME + ChatColor.LIGHT_GRAY.text() + "] " + ChatColor.DEFAULT.text();
    }

    default void sendClientMessageWithHeader(String message) {
        if (this.failsNullCheck()) {
            return;
        }

        String prefixMessage = this.getHeader() + message;

        this.sendMessageWithLinesColor(prefixMessage);
    }

    @SuppressWarnings("unchecked")
    default void sendMessageWithLinesColor(String message) {

        while (this.getFontRenderer().getStringWidth(message) > 320) {

            int firstEnd = 1;

            while (firstEnd < message.length() && this.getFontRenderer().getStringWidth(message.substring(0, firstEnd + 1)) <= 320) {
                firstEnd++;
            }

            String firstMessage = message.substring(0, firstEnd);

            this.sendMessageWithLinesColor(firstMessage);

            int firstMessageColor = firstMessage.lastIndexOf('§') + 1;

            String previousMessageColor = "";

            if (firstMessageColor != 0 && firstMessageColor != firstMessage.length()) {
                previousMessageColor = "§" + firstMessage.charAt(firstMessageColor);
            }

            message = previousMessageColor + message.substring(firstEnd);
        }

        ((GuiIngameAccessor) this.getInGameGUI()).tensor_getChatMessageList().add(0, new ChatLine(message));

        while (((GuiIngameAccessor) this.getInGameGUI()).tensor_getChatMessageList().size() > 50) {
            ((GuiIngameAccessor) this.getInGameGUI()).tensor_getChatMessageList().remove(((GuiIngameAccessor) this.getInGameGUI()).tensor_getChatMessageList().size() - 1);
        }
    }

    default Rotation getRotationTo(Entity entity) {
        return this.getRotationTo(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
    }

    default Rotation getRotationTo(double x, double y, double z) {
        double[] deltas = this.returnDeltas(x, y, z);

        double angle = 180 / Math.PI;

        float pitch = (float) -(Math.atan2(deltas[1], deltas[3]) * angle);
        float yaw = (float) ((Math.atan2(deltas[2], deltas[0]) * angle) - 90);

        return new Rotation(pitch, yaw);
    }

    default double[] returnDeltas(double x, double y, double z) {
        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        double deltaX = x - this.getPlayer().posX;
        double deltaY = y - this.getPlayer().posY - this.getPlayer().getEyeHeight();
        double deltaZ = z - this.getPlayer().posZ;

        if (freecam != null && freecam.isEnabled()) {
            deltaX = x - freecam.oldX;
            deltaY = y - freecam.oldY - this.getPlayer().getEyeHeight();
            deltaZ = z - freecam.oldZ;
        }

        return new double[]{
                deltaX,
                deltaY,
                deltaZ,
                this.getDistance(deltaX, deltaY, deltaZ)
        };
    }

    default double getDistance(double deltaX, double deltaY, double deltaZ) {
        return Math.sqrt(this.getDistanceSq(deltaX, deltaY, deltaZ));
    }

    default double getDistanceSq(double deltaX, double deltaY, double deltaZ) {
        return this.squaredD(deltaX) + this.squaredD(deltaY) + this.squaredD(deltaZ);
    }

    default double squaredD(double input) {
        return input * input;
    }

    default double getDistanceSqWithFreecam(double targetX, double targetY, double targetZ) {
        double pX = this.getPlayer().posX;
        double pY = this.getPlayer().posY;
        double pZ = this.getPlayer().posZ;

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        if (freecam != null && freecam.isEnabled()) {
            pX = freecam.oldX;
            pY = freecam.oldY;
            pZ = freecam.oldZ;
        }

        double dX = targetX - pX;
        double dY = targetY - pY;
        double dZ = targetZ - pZ;

        return this.getDistanceSq(dX, dY, dZ);
    }

    default int[][] getPlaceDirections(int startX, int startY, int startZ) {
        return new int[][]{
                {startX, startY + 1, startZ, 0},
                {startX, startY - 1, startZ, 1},
                {startX, startY, startZ + 1, 2},
                {startX, startY, startZ - 1, 3},
                {startX + 1, startY, startZ, 4},
                {startX - 1, startY, startZ, 5}
        };
    }

    default boolean isPlayerMoving2D() {
        return this.getPlayer().motionX != 0 || this.getPlayer().motionZ != 0;
    }

    default void setPlayerFlyingSpeed(double moveSpeed) {
        float yaw = this.getPlayer().rotationYaw;

        float strafeSpeed = this.getPlayer().movementInput.moveStrafe;
        float forwardSpeed = this.getPlayer().movementInput.moveForward;

        if (forwardSpeed != 0.0F) {

            if (strafeSpeed > 0.0F) {
                yaw += ((forwardSpeed > 0.0F) ? -45 : 45);

            } else if (strafeSpeed < 0.0F) {
                yaw += ((forwardSpeed > 0.0F) ? 45 : -45);

            }

            strafeSpeed = 0.0F;

            if (forwardSpeed > 0.0F) {
                forwardSpeed = 1.0F;

            } else if (forwardSpeed < 0.0F) {
                forwardSpeed = -1.0F;

            }
        }

        if (strafeSpeed > 0.0F) {
            strafeSpeed = 1.0F;

        } else if (strafeSpeed < 0.0F) {
            strafeSpeed = -1.0F;

        }

        double motionX = Math.cos(Math.toRadians((yaw + 90.0F)));
        double motionZ = Math.sin(Math.toRadians((yaw + 90.0F)));

        this.getPlayer().motionX = forwardSpeed * moveSpeed * motionX + strafeSpeed * moveSpeed * motionZ;
        this.getPlayer().motionZ = forwardSpeed * moveSpeed * motionZ - strafeSpeed * moveSpeed * motionX;
    }

    default String getWaypointServerName() {
        if (!this.isMultiplayer()) {
            return "Singleplayer";
        }

        return this.getServerIP();
    }

    default String getServerIP() {
        String hostName = ((NetworkManagerAccessor) ((NetClientHandlerAccessor) this.getMinecraft().getSendQueue()).tensor_getNetManager()).tensor_getNetworkSocket().getInetAddress().getHostName();

        if (hostName == null) {
            hostName = ((NetworkManagerAccessor) ((NetClientHandlerAccessor) this.getMinecraft().getSendQueue()).tensor_getNetManager()).tensor_getNetworkSocket().getInetAddress().getHostAddress();
        }

        if (hostName == null) {
            return "Penis";
        }

        return hostName;
    }

    default boolean isBlockOnBullshitListForSwap(int blockID) {
        if (blockID == Block.obsidian.blockID) {
            return true;
        } else if (blockID == Block.brick.blockID) {
            return true;
        } else if (blockID == Block.mobSpawner.blockID) {
            return true;
        } else if (blockID == Block.glowStone.blockID) {
            return true;
        } else if (blockID == Block.stairCompactCobblestone.blockID) {
            return true;
        } else if (blockID == Block.stoneOvenIdle.blockID) {
            return true;
        } else if (blockID == Block.stoneOvenActive.blockID) {
            return true;
        } else if (blockID == Block.oreRedstone.blockID) {
            return true;
        } else return blockID == Block.oreRedstoneGlowing.blockID;
    }

    default int getMaxXZForRenderDistance() {
        switch (this.getMinecraft().gameSettings.renderDistance) {
            case 1:
                return 128;
            case 2:
                return 64;
            case 3:
                return 32;
            default:
                return 256;
        }
    }
}
