package dev.tensor.misc.imp;

import dev.tensor.TensorClassic;
import dev.tensor.feature.hacks.Search;
import dev.tensor.feature.hacks.Xray;
import dev.tensor.feature.managers.*;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;
import org.slf4j.Logger;

import java.awt.*;

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

    default boolean failsNullCheck() {
        return this.getMinecraft() == null || this.getPlayer() == null || this.getWorld() == null;
    }

    default boolean isSearchEnabled() {
        Search search = this.getHackManager().getHack(Search.class);

        if (search == null) {
            return false;
        }

        return search.isEnabled();
    }

    default boolean isXrayEnabled() {
        Xray xray = this.getXray();

        if (xray == null) {
            return false;
        }

        return xray.isEnabled();
    }

    default void resetWorld() {
        if (this.failsNullCheck()) {
            return;
        }

        this.getMinecraft().renderGlobal.loadRenderers();
    }

    default void sendPacket(Packet packet) {
        if (this.getMinecraft().getSendQueue() == null)  {
            return;
        }

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

    default Xray getXray() {
        return this.getHackManager().getHack(Xray.class);
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

    default float[] pitchAndYawTo(Entity entity) {
        Vector4f deltas = this.returnDeltas(entity);

        double angle = 180 / Math.PI;

        float pitch = (float) -(Math.atan2(deltas.y, deltas.w) * angle);
        float yaw = (float) ((Math.atan2(deltas.z, deltas.x) * angle) - 90);

        return new float[]{
                pitch,
                yaw
        };
    }

    default Vector4f returnDeltas(Entity entity) {
        double deltaX = entity.posX - this.getPlayer().posX;
        double deltaY = entity.posY + entity.getEyeHeight() - this.getPlayer().posY - this.getPlayer().getEyeHeight();
        double deltaZ = entity.posZ - this.getPlayer().posZ;

        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2));

        return new Vector4f((float) deltaX, (float) deltaY, (float) deltaZ, (float) distance);
    }

    default void startBlockGL() {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDepthMask(false);
    }

    default void finishBlockGL() {
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    default void drawOutlineBox(double x, double y, double z, int width, int lineWidth, Color color) {
        double wX = x + width;
        double wY = y + width;
        double wZ = z + width;

        this.drawOutlineBox(x, y, z, wX, wY, wZ, lineWidth, color);
    }

    default void drawOutlineBox(double x, double y, double z, double wX, double wY, double wZ, int lineWidth, Color color) {
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);

        Tessellator.instance.startDrawing(GL11.GL_LINE_STRIP);
        Tessellator.instance.addVertex(x, y, z);
        Tessellator.instance.addVertex(wX, y, z);
        Tessellator.instance.addVertex(wX, y, wZ);
        Tessellator.instance.addVertex(x, y, wZ);
        Tessellator.instance.addVertex(x, y, z);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_LINE_STRIP);
        Tessellator.instance.addVertex(x, wY, z);
        Tessellator.instance.addVertex(wX, wY, z);
        Tessellator.instance.addVertex(wX, wY, wZ);
        Tessellator.instance.addVertex(x, wY, wZ);
        Tessellator.instance.addVertex(x, wY, z);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_LINES);
        Tessellator.instance.addVertex(x, y, z);
        Tessellator.instance.addVertex(x, wY, z);
        Tessellator.instance.addVertex(wX, y, z);
        Tessellator.instance.addVertex(wX, wY, z);
        Tessellator.instance.addVertex(wX, y, wZ);
        Tessellator.instance.addVertex(wX, wY, wZ);
        Tessellator.instance.addVertex(x, y, wZ);
        Tessellator.instance.addVertex(x, wY, wZ);
        Tessellator.instance.draw();
    }

    default void drawFillBox(double x, double y, double z, int width, Color color) {
        double wX = x + width;
        double wY = y + width;
        double wZ = z + width;

        this.drawFillBox(x, y, z, wX, wY, wZ, color);
    }

    default void drawFillBox(double x, double y, double z, double wX, double wY, double wZ, Color color) {
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);

        Tessellator.instance.startDrawing(GL11.GL_QUADS);
        Tessellator.instance.addVertex(wX, y, wZ);
        Tessellator.instance.addVertex(wX, y, z);
        Tessellator.instance.addVertex(wX, wY, z);
        Tessellator.instance.addVertex(wX, wY, wZ);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_QUADS);
        Tessellator.instance.addVertex(x, y, z);
        Tessellator.instance.addVertex(x, y, wZ);
        Tessellator.instance.addVertex(x, wY, wZ);
        Tessellator.instance.addVertex(x, wY, z);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_QUADS);
        Tessellator.instance.addVertex(wX, y, z);
        Tessellator.instance.addVertex(x, y, z);
        Tessellator.instance.addVertex(x, wY, z);
        Tessellator.instance.addVertex(wX, wY, z);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_QUADS);
        Tessellator.instance.addVertex(x, y, wZ);
        Tessellator.instance.addVertex(wX, y, wZ);
        Tessellator.instance.addVertex(wX, wY, wZ);
        Tessellator.instance.addVertex(x, wY, wZ);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_QUADS);
        Tessellator.instance.addVertex(wX, wY, z);
        Tessellator.instance.addVertex(x, wY, z);
        Tessellator.instance.addVertex(x, wY, wZ);
        Tessellator.instance.addVertex(wX, wY, wZ);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_QUADS);
        Tessellator.instance.addVertex(wX, y, z);
        Tessellator.instance.addVertex(wX, y, wZ);
        Tessellator.instance.addVertex(x, y, wZ);
        Tessellator.instance.addVertex(x, y, z);
        Tessellator.instance.draw();
    }
}
