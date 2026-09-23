package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.ColorNoAlphaSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.RenderManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Hoosiers
 * @since 04-06-2026
 */

@Hack.Info(name = "WolfOwner", category = Category.Render, description = "Renders a nametag of the player owner over pet wolves.")
public final class WolfOwner extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.WolfOwner;
    }

    @Override
    public boolean overridesBlockGL() {
        return true;
    }

    public final DoubleSetting scale = new DoubleSetting("Scale", 1.00, 0.10, 2.00, 2);
    public final IntegerSetting distanceMultiplier = new IntegerSetting("Distance Multiplier", 6, 2, 10);
    public final ColorNoAlphaSetting color = new ColorNoAlphaSetting("Color", Color.RED, "Sets the color of text for tags.");
    public final ColorNoAlphaSetting friendColor = new ColorNoAlphaSetting("Friend Color", Color.CYAN, "Sets the color of text for tags if the owner is a friend.");

    private final ConcurrentHashMap<EntityWolf, Color> wolvesWithColorToRender = new ConcurrentHashMap<>();

    @Override
    public void onDisable(boolean isNull) {
        this.wolvesWithColorToRender.clear();
    }

    @Override
    public void onClientTick() {
        this.wolvesWithColorToRender.clear();

        this.getFilteredLoadedEntities().parallelStream().forEach(entity -> {
            if (entity instanceof EntityWolf) {

                EntityWolf entityWolf = (EntityWolf) entity;

                String owner = entityWolf.getWolfOwner();

                if (owner != null && !owner.isEmpty()) {
                    Color color1 = owner.equalsIgnoreCase(this.getPlayer().username) ? this.friendColor.getValueRainbow() : this.getFriendManager().isFriend(owner) ? this.friendColor.getValueRainbow() : this.color.getValueRainbow();

                    this.wolvesWithColorToRender.put(entityWolf, color1);
                }
            }
        });
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        this.wolvesWithColorToRender.forEach((entityWolf, color1) -> this.renderNameTagForWolf(entityWolf, color1, renderPartialTicks));
    }

    private void renderNameTagForWolf(EntityWolf entity, Color color, float renderPartialTicks) {
        AxisAlignedBB bb = entity.boundingBox;

        double x = bb.minX + ((bb.maxX - bb.minX) / 2) + this.translateOffset(entity.lastTickPosX, entity.posX, renderPartialTicks);
        double y = bb.maxY + 0.5 + this.translateOffset(entity.lastTickPosY, entity.posY, renderPartialTicks);
        double z = bb.minZ + ((bb.maxZ - bb.minZ) / 2) + this.translateOffset(entity.lastTickPosZ, entity.posZ, renderPartialTicks);

        double distanceSq = this.getPlayer().getDistanceSq(x, y, z);

        double scale = -Math.max(0.0416667D * this.scale.getValue(), 0.0416667D * this.scale.getValue() * Math.min(this.distanceMultiplier.getValue(), (distanceSq / 512)));

        GL11.glPushMatrix();

        GL11.glTranslated(x - RenderManager.renderPosX, y - RenderManager.renderPosY, z - RenderManager.renderPosZ);

        GL11.glRotated(-RenderManager.instance.playerViewY, 0.0D, 1.0D, 0.0D);
        GL11.glRotated(RenderManager.instance.playerViewX, 1.0D, 0.0D, 0.0D);

        GL11.glScaled(scale, scale, scale);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);

        String owner1 = "Owner: " + entity.getWolfOwner();
        this.drawString(owner1, -this.getFontRenderer().getStringWidth(owner1) / 2, 0, color.getRGB());

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);

        GL11.glPopMatrix();
    }
}
