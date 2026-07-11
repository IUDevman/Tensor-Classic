package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.entity.FakePlayer;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.ColorNoAlphaSetting;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.RenderManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Hoosiers
 * @since 07-10-2026
 */

@Hack.Info(name = "EpsteinESP", category = Category.Render)
public final class EpsteinESP extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.EpsteinESP;
    }

    public final BooleanSetting friends = new BooleanSetting("Friends", true);
    public final ColorNoAlphaSetting color = new ColorNoAlphaSetting("Color", Color.BLACK);
    public final ColorNoAlphaSetting friendColor = new ColorNoAlphaSetting("Fried Color", Color.BLACK);

    private final ConcurrentHashMap<EntityPlayer, Integer> playersToRenderWithColor = new ConcurrentHashMap<>();

    @Override
    public void onDisable(boolean isNull) {
        this.playersToRenderWithColor.clear();
    }

    @Override
    public void onClientTick() {
        this.playersToRenderWithColor.clear();

        this.getFilteredLoadedEntities().forEach(entity -> {

            if (entity instanceof FakePlayer) {
                this.playersToRenderWithColor.put((EntityPlayer) entity, this.color.getValue().getRGB());
            }

            if (entity instanceof EntityOtherPlayerMP) {
                EntityOtherPlayerMP otherPlayer = (EntityOtherPlayerMP) entity;

                boolean friend = this.getFriendManager().isFriend(otherPlayer.username);

                if (friend && !this.friends.getValue()) {
                    return;
                }

                this.playersToRenderWithColor.put(otherPlayer, friend ? this.friendColor.getValue().getRGB() : this.color.getValue().getRGB());
            }
        });
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        if (this.playersToRenderWithColor.isEmpty()) {
            return;
        }

        this.playersToRenderWithColor.forEach((entityPlayer, integer) -> this.drawEpsteinBox(entityPlayer, integer, renderPartialTicks));
    }

    private void drawEpsteinBox(EntityPlayer player, int color, float renderPartialTicks) {
        GL11.glPushMatrix();

        AxisAlignedBB bb = player.boundingBox;

        double x = bb.minX + ((bb.maxX - bb.minX) / 2) + this.translateOffset(player.lastTickPosX, player.posX, renderPartialTicks);
        double y = bb.maxY + 0.6 + this.translateOffset(player.lastTickPosY, player.posY, renderPartialTicks);
        double z = bb.minZ + ((bb.maxZ - bb.minZ) / 2) + this.translateOffset(player.lastTickPosZ, player.posZ, renderPartialTicks);

        GL11.glTranslated(x - RenderManager.renderPosX, y - RenderManager.renderPosY, z - RenderManager.renderPosZ);

        GL11.glRotated(-RenderManager.instance.playerViewY, 0.0D, 1.0D, 0.0D);
        GL11.glRotated(RenderManager.instance.playerViewX, 1.0D, 0.0D, 0.0D);

        GL11.glScaled(-0.0266666688F, -0.0266666688F, -0.0266666688F);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);

        this.drawRectangle(-25, 0, 25, 100, color);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);

        GL11.glPopMatrix();
    }
}
