package dev.tensor.misc.imp;

import dev.tensor.TensorClassic;
import dev.tensor.backend.mixins.accessors.GuiIngameAccessor;
import dev.tensor.backend.mixins.accessors.NetClientHandlerAccessor;
import dev.tensor.backend.mixins.accessors.NetworkManagerAccessor;
import dev.tensor.feature.hacks.AutoReconnect;
import dev.tensor.feature.hacks.HephFreecam;
import dev.tensor.feature.hacks.Search;
import dev.tensor.feature.hacks.Xray;
import dev.tensor.feature.managers.*;
import dev.tensor.misc.helpers.rotation.Rotation;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.util.vector.Vector4f;
import org.slf4j.Logger;

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

    default RotationManager getRotationManager() {
        return (RotationManager) this.getTensor().MANAGERS.get(RotationManager.class);
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

    default boolean isSearchEnabled() {
        Search search = this.getHackManager().getHack(Search.class);

        if (search == null) {
            return false;
        }

        return search.isEnabled();
    }

    default boolean isAutoReconnectEnabled() {
        AutoReconnect autoReconnect = this.getAutoReconnect();

        if (autoReconnect == null) {
            return false;
        }

        return autoReconnect.isEnabled();
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

    default String getColor(ChatColor COLOR) {
        switch (COLOR) {
            case DARK_RED:
                return "§4";
            case RED:
                return "§c";
            case GOLD:
                return "§6";
            case YELLOW:
                return "§e";
            case DARK_GREEN:
                return "§2";
            case GREEN:
                return "§a";
            case AQUA:
                return "§b";
            case DARK_AQUA:
                return "§3";
            case BLUE:
                return "§9";
            case DARK_BLUE:
                return "§1";
            case PINK:
                return "§d";
            case PURPLE:
                return "§5";
            case LIGHT_GRAY:
                return "§7";
            case GRAY:
                return "§8";
            case DARK_GRAY:
                return "§9";
            case BLACK:
                return "§0";
            default:
                return "§f";
        }
    }

    enum ChatColor {
        DARK_RED,
        RED,
        GOLD,
        YELLOW,
        DARK_GREEN,
        GREEN,
        AQUA,
        DARK_AQUA,
        BLUE,
        DARK_BLUE,
        PINK,
        PURPLE,
        LIGHT_GRAY,
        GRAY,
        DARK_GRAY,
        BLACK,
        WHITE,
        DEFAULT
    }

    default String getHeader() {
        return this.getColor(ChatColor.LIGHT_GRAY) + "[" + this.getColor(ChatColor.YELLOW) + TensorClassic.MOD_NAME + this.getColor(ChatColor.LIGHT_GRAY) + "] " + this.getColor(ChatColor.DEFAULT);
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

    default Rotation pitchAndYawTo(Entity entity) {
        return this.pitchAndYawTo(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
    }

    default Rotation pitchAndYawTo(double x, double y, double z) {
        Vector4f deltas = this.returnDeltas(x, y, z);

        double angle = 180 / Math.PI;

        float pitch = (float) -(Math.atan2(deltas.y, deltas.w) * angle);
        float yaw = (float) ((Math.atan2(deltas.z, deltas.x) * angle) - 90);

        return new Rotation(pitch, yaw);
    }

    default Vector4f returnDeltas(double x, double y, double z) {
        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        double deltaX = x - this.getPlayer().posX;
        double deltaY = y - this.getPlayer().posY - this.getPlayer().getEyeHeight();
        double deltaZ = z - this.getPlayer().posZ;

        if (freecam != null && freecam.isEnabled()) {
            deltaX = x - freecam.oldX;
            deltaY = y - freecam.oldY - this.getPlayer().getEyeHeight();
            deltaZ = z - freecam.oldZ;
        }

        double distance = this.getDistance(deltaX, deltaY, deltaZ);

        return new Vector4f((float) deltaX, (float) deltaY, (float) deltaZ, (float) distance);
    }

    default double getDistance(double deltaX, double deltaY, double deltaZ) {
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2));
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
}
