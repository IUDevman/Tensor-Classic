package dev.tensor.misc.imp;

import dev.tensor.TensorClassic;
import dev.tensor.feature.managers.*;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
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


    default GuiIngame getInGameGUI() {
        return this.getMinecraft().ingameGUI;
    }

    default boolean failsNullCheck() {
        return this.getMinecraft() == null || this.getPlayer() == null || this.getWorld() == null;
    }

    default void resetWorld() {
        if (this.failsNullCheck()) {
            return;
        }

        this.getMinecraft().renderGlobal.loadRenderers();
    }

    default void sendPacket(Packet packet) {
        this.getMinecraft().getSendQueue().addToSendQueue(packet);
    }

    default boolean isMultiplayer() {
        return this.getWorld().multiplayerWorld;
    }

    default TensorClassic getTensor() {
        return TensorClassic.getInstance();
    }

    default Logger getTensorLogger() {
        return TensorClassic.getLogger();
    }

    default EventManager getEventManager() {
        return (EventManager) this.getTensor().MANAGERS.stream().filter(manager -> manager instanceof EventManager).findFirst().orElse(null);
    }

    default ConfigManager getConfigManager() {
        return (ConfigManager) this.getTensor().MANAGERS.stream().filter(manager -> manager instanceof ConfigManager).findFirst().orElse(null);
    }

    default FriendManager getFriendManager() {
        return (FriendManager) this.getTensor().MANAGERS.stream().filter(manager -> manager instanceof FriendManager).findFirst().orElse(null);
    }

    default CustomGUIManager getCustomGUIManager() {
        return (CustomGUIManager) this.getTensor().MANAGERS.stream().filter(manager -> manager instanceof CustomGUIManager).findFirst().orElse(null);
    }

    default HackManager getHackManager() {
        return (HackManager) this.getTensor().MANAGERS.stream().filter(manager -> manager instanceof HackManager).findFirst().orElse(null);
    }

    default CommandManager getCommandManager() {
        return (CommandManager) this.getTensor().MANAGERS.stream().filter(manager -> manager instanceof CommandManager).findFirst().orElse(null);
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

    default String getPrefix() {
        return this.getColor(ChatColor.LIGHT_GRAY) + "[" + this.getColor(ChatColor.YELLOW) + TensorClassic.MOD_NAME + this.getColor(ChatColor.LIGHT_GRAY) + "] " + this.getColor(ChatColor.DEFAULT);
    }

    default void sendClientMessageWithPrefix(String message) {
        if (this.failsNullCheck()) {
            return;
        }

        String prefixMessage = this.getPrefix() + message;

        this.sendMessageWithLinesColor(prefixMessage);
    }

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

        this.getPlayer().addChatMessage(message);
    }

    default void drawRectangle(int minX, int minY, int maxX, int maxY, int color) {
        this.getCustomGUIManager().drawRectangle(minX, minY, maxX, maxY, color);
    }

    default void drawString(String text, int x, int y, int color) {
        this.getCustomGUIManager().drawString(text, x, y, color);
    }

    default void drawCenteredString(String text, int x, int y, int color) {
        this.getCustomGUIManager().drawCenteredString(text, x, y, color);
    }
}
