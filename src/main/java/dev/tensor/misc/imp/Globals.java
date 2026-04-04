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

    default boolean isMultiplayer() {
        return this.getWorld().multiplayerWorld;
    }

    default void resetWorld() {
        if (this.failsNullCheck()) {
            return;
        }

        this.getMinecraft().renderGlobal.loadRenderers();
    }

    default void sendPacket(Packet packet) {
        if (this.getMinecraft().getSendQueue() == null) {
            return;
        }

        this.getMinecraft().getSendQueue().addToSendQueue(packet);
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

    default void drawTracer(double targetX, double targetY, double targetZ, int lineWidth, Color color, int alpha) {
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha / 255F);

        Tessellator.instance.startDrawing(GL11.GL_LINES);
        Tessellator.instance.addVertex(0, 0, 0);
        Tessellator.instance.addVertex(targetX, targetY, targetZ);
        Tessellator.instance.draw();
    }

    int GL_WTF_IS_THIS = 32826;

    default void startItemGL() {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL_WTF_IS_THIS);

        RenderHelper.enableStandardItemLighting();

        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    default void finishItemGL() {
        RenderHelper.disableStandardItemLighting();

        GL11.glDisable(GL_WTF_IS_THIS);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }

    RenderItem renderItem = new RenderItem();
    RenderBlocks renderBlocks = new RenderBlocks();

    /*
    EXTREME JANK BELOW!!! Scaling this is HARD!
     */
    default void renderItem(ItemStack itemStack, int startX, int startY, int itemX, int itemY, double scale, double itemScale) {
        int itemID = itemStack.itemID;
        int itemIcon = itemStack.getIconIndex();

        if (itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemID].getRenderType())) {
            this.getRenderEngine().bindTexture(this.getRenderEngine().getTexture("/terrain.png"));

            GL11.glPushMatrix();

            GL11.glTranslatef((float) (startX), (float) (startY), -3.0F);
            GL11.glScalef((float) (10.0F * scale), (float) (10.0F * scale), (float) (10.0F * scale));

            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);

            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);

            int damageColor = Item.itemsList[itemID].getColorFromDamage(itemStack.getItemDamage());
            float r = (float) (damageColor >> 16 & 255) / 255.0F;
            float g = (float) (damageColor >> 8 & 255) / 255.0F;
            float b = (float) (damageColor & 255) / 255.0F;

            if (renderItem.field_27004_a) {
                GL11.glColor4f(r, g, b, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);

            renderBlocks.field_31088_b = renderItem.field_27004_a;
            renderBlocks.renderBlockOnInventory(Block.blocksList[itemID], itemStack.getItemDamage(), 1.0F);
            renderBlocks.field_31088_b = true;

            GL11.glPopMatrix();

            GL11.glTranslatef((float) (startX + (itemX * scale)), (float) (startY - (itemY * scale)), -3.0F);
            GL11.glScalef((float) ((1.0F * scale) - itemScale), (float) ((1.0F * scale) - itemScale), 0F);

        } else if (itemIcon >= 0) {

            GL11.glTranslatef((float) (startX + (itemX * scale)), (float) (startY - (itemY * scale)), -3.0F);
            GL11.glScalef((float) ((1.0F * scale) - itemScale), (float) ((1.0F * scale) - itemScale), 0F);

            GL11.glDisable(GL11.GL_LIGHTING);

            if (itemID < 256) {
                this.getRenderEngine().bindTexture(this.getRenderEngine().getTexture("/terrain.png"));
            } else {
                this.getRenderEngine().bindTexture(this.getRenderEngine().getTexture("/gui/items.png"));
            }

            int damageColor = Item.itemsList[itemID].getColorFromDamage(itemStack.getItemDamage());
            float r = (float) (damageColor >> 16 & 255) / 255.0F;
            float g = (float) (damageColor >> 8 & 255) / 255.0F;
            float b = (float) (damageColor & 255) / 255.0F;

            if (renderItem.field_27004_a) {
                GL11.glColor4f(r, g, b, 1.0F);
            }

            renderItem.renderTexturedQuad(0, 0, itemIcon % 16 * 16, itemIcon / 16 * 16, 16, 16);

            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    default void renderItemOverlay(ItemStack itemStack, int startX, int startY, double scale) {
        if (itemStack == null) {
            return;
        }

        GL11.glTranslatef((float) (startX * scale), (float) (startY * scale), -3);

        if (itemStack.stackSize > 1) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            this.drawString(String.valueOf(itemStack.stackSize), startX + 19 - 2 - this.getFontRenderer().getStringWidth(String.valueOf(itemStack.stackSize)), startY + 6 + 3, Color.WHITE.getRGB());

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        if (itemStack.isItemDamaged()) {
            int minWidth = Math.round(13.0F - itemStack.getItemDamageForDisplay() * 13.0F / itemStack.getMaxDamage());
            int dWidth = Math.round(255.0F - itemStack.getItemDamageForDisplay() * 255.0F / itemStack.getMaxDamage());

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);

            int color0 = 0;
            int color1 = 255 - dWidth << 16 | dWidth << 8;
            int color2 = (255 - dWidth) / 4 << 16 | 16128;

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setColorOpaque_I(color0);
            Tessellator.instance.addVertex(startX + 2, startY + 13, 0.0F);
            Tessellator.instance.addVertex(startX + 2, startY + 15, 0.0F);
            Tessellator.instance.addVertex(startX + 15, startY + 15, 0.0F);
            Tessellator.instance.addVertex(startX + 15, startY + 13, 0.0F);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setColorOpaque_I(color2);
            Tessellator.instance.addVertex(startX + 2, startY + 13, 0.0F);
            Tessellator.instance.addVertex(startX + 2, startY + 14, 0.0F);
            Tessellator.instance.addVertex(startX + 14, startY + 14, 0.0F);
            Tessellator.instance.addVertex(startX + 14, startY + 13, 0.0F);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setColorOpaque_I(color1);
            Tessellator.instance.addVertex(startX + 2, startY + 13, 0.0F);
            Tessellator.instance.addVertex(startX + 2, startY + 14, 0.0F);
            Tessellator.instance.addVertex(startX + 2 + minWidth, startY + 14, 0.0F);
            Tessellator.instance.addVertex(startX + 2 + minWidth, startY + 13, 0.0F);
            Tessellator.instance.draw();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    default void renderItemPercentage(ItemStack itemStack, int startX, int startY) {
        if (itemStack == null) {
            return;
        }

        GL11.glScalef(0.5F, 0.5F, 0.0F);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        int dColor = Math.round(255.0F - itemStack.getItemDamage() * 255.0F / itemStack.getMaxDamage());
        int color = 255 - dColor << 16 | dColor << 8;

        double dDamage = (double) itemStack.getItemDamage() / itemStack.getMaxDamage();
        int percentage = (int) (100 - (dDamage * 100));

        String string = percentage + "%";
        this.drawString(string, startX + 19 - 2 - this.getFontRenderer().getStringWidth(string), startY + 6 + 3, new Color(color >> 16 & 255, color >> 8 & 255, color & 255).getRGB());

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
}
