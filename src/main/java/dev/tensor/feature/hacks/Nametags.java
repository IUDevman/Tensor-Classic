package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.NameTagEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.helpers.entity.FakePlayer;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hoosiers
 * @since 04-06-2026
 */

@Hack.Info(name = "Nametags", category = Category.Render)
public final class Nametags extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.Nametags;
    }

    public final BooleanSetting friends = new BooleanSetting("Friends", true);
    public final BooleanSetting darkenSneak = new BooleanSetting("Darken Sneak", false);
    public final BooleanSetting showHealth = new BooleanSetting("Show Health", false);
    public final BooleanSetting showItems = new BooleanSetting("Show Items", true);
    public final EnumSetting durability = new EnumSetting("Durability", Durability.Percentage);
    public final BooleanSetting background = new BooleanSetting("Background", false);
    public final IntegerSetting distanceMultiplier = new IntegerSetting("Distance Multiplier", 6, 2, 10);
    public final ColorSetting textColor = new ColorSetting("Text Color", Color.ORANGE);
    public final ColorSetting friendColor = new ColorSetting("Friend Color", Color.CYAN);
    public final ColorSetting backgroundColor = new ColorSetting("Background Color", new Color(0, 0, 0, 150));

    private final ArrayList<EntityPlayer> playersToRender = new ArrayList<>();

    @Override
    public void onDisable(boolean isNull) {
        this.playersToRender.clear();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onClientTick() {
        this.playersToRender.clear();

        List entities = this.getWorld().loadedEntityList;

        for (int i = 0; i < entities.size(); i++) {

            Entity entity = (Entity) entities.get(i);

            if (entity == null) {
                continue;
            }

            if (!entity.isEntityAlive()) {
                continue;
            }

            if (entity instanceof EntityOtherPlayerMP || entity instanceof FakePlayer) {
                this.playersToRender.add((EntityPlayer) entity);
            }
        }
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        this.playersToRender.forEach(entityPlayer -> this.renderNameTagForPlayer(entityPlayer, renderPartialTicks));
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onNameTagEvent(NameTagEvent event) {
        event.cancel();
    }

    private void renderNameTagForPlayer(EntityPlayer entity, float renderPartialTicks) {
        String username = entity.username;

        Color textColor = this.friends.getValue() && this.getFriendManager().isFriend(username) ? this.friendColor.getValue() : this.textColor.getValue();
        Color fillColor = this.backgroundColor.getValue();

        String health = "";
        String healthPost = "";

        Color healthColor = null;

        if (this.showHealth.getValue()) {
            username += " [";
            health = String.valueOf(entity.health);
            healthPost = "]";
            healthColor = this.getHealthColor(entity);
        }

        String combined = username + health + healthPost;

        if (this.darkenSneak.getValue() && entity.isSneaking()) {
            if (textColor == Color.BLACK) {
                textColor = textColor.brighter();
            } else {
                textColor = textColor.darker();
            }

            if (healthColor != null) {
                healthColor = healthColor.darker();
            }

            if (fillColor == Color.BLACK) {
                fillColor = fillColor.brighter();
            } else {
                fillColor = fillColor.darker();
            }
        }

        AxisAlignedBB bb = entity.boundingBox;

        double x = bb.minX + ((bb.maxX - bb.minX) / 2) + this.translateOffset(entity.lastTickPosX, entity.posX, renderPartialTicks);
        double y = bb.maxY + 0.6 + this.translateOffset(entity.lastTickPosY, entity.posY, renderPartialTicks);
        double z = bb.minZ + ((bb.maxZ - bb.minZ) / 2) + this.translateOffset(entity.lastTickPosZ, entity.posZ, renderPartialTicks);

        double distanceSq = this.getPlayer().getDistanceSq(x, y, z);

        double scale = -Math.max(0.0416667D, 0.0416667D * Math.min(this.distanceMultiplier.getValue(), (distanceSq / 512)));

        GL11.glPushMatrix();

        GL11.glTranslated(x - RenderManager.renderPosX, y - RenderManager.renderPosY, z - RenderManager.renderPosZ);

        GL11.glRotated(-RenderManager.instance.playerViewY, 0.0D, 1.0D, 0.0D);
        GL11.glRotated(RenderManager.instance.playerViewX, 1.0D, 0.0D, 0.0D);

        GL11.glScaled(scale, scale, scale);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);

        if (this.background.getValue()) {
            this.drawRectangle(-this.getFontRenderer().getStringWidth(combined) / 2 - 1, -1, this.getFontRenderer().getStringWidth(combined) / 2 + 1, 9, fillColor.getRGB());
        }

        this.drawString(username, -this.getFontRenderer().getStringWidth(combined) / 2, 0, textColor.getRGB());

        if (this.showHealth.getValue() && healthColor != null) {
            this.drawString(health, -(this.getFontRenderer().getStringWidth(combined) / 2) + this.getFontRenderer().getStringWidth(username), 0, healthColor.getRGB());
            this.drawString(healthPost, -(this.getFontRenderer().getStringWidth(combined) / 2) + this.getFontRenderer().getStringWidth(username) + this.getFontRenderer().getStringWidth(health), 0, textColor.getRGB());
        }

        if (this.showItems.getValue()) {
            ItemStack hand = entity.inventory.getCurrentItem();
            ItemStack[] armor = entity.inventory.armorInventory;

            RenderHelper.enableStandardItemLighting();

            if (hand != null) {
                this.renderItem3D(hand, -40, -17, 1);
                this.renderOverlay(hand, -40, -17, 1);
            }

            if (armor[3] != null) {
                this.renderItem3D(armor[3], -24, -17, 1);
                this.renderOverlay(armor[3], -24, -17, 1);
            }

            if (armor[2] != null) {
                this.renderItem3D(armor[2], -8, -17, 1);
                this.renderOverlay(armor[2], -8, -17, 1);
            }

            if (armor[1] != null) {
                this.renderItem3D(armor[1], 8, -17, 1);
                this.renderOverlay(armor[1], 8, -17, 1);
            }

            if (armor[0] != null) {
                this.renderItem3D(armor[0], 24, -17, 1);
                this.renderOverlay(armor[0], 24, -17, 1);
            }

            RenderHelper.disableStandardItemLighting();
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);

        GL11.glPopMatrix();
    }

    private void renderOverlay(ItemStack itemStack, int startX, int startY, double scale) {
        switch ((Durability) this.durability.getValue()) {
            case None:
                break;
            case Vanilla:
                this.renderItemOverlay3D(itemStack, startX, startY);
                break;
            case Percentage:
                int offsetX = itemStack.getItem().isDamagable() ? 7 : 0;

                this.renderItemPercentage3D(itemStack, startX + offsetX, startY, 0.6 * scale);
                break;
        }
    }

    private Color getHealthColor(EntityPlayer entity) {
        int dColor = Math.round(255.0F - (20 - entity.health) * 255.0F / 20);
        int color = 255 - dColor << 16 | dColor << 8;

        return new Color(color >> 16 & 255, color >> 8 & 255, color & 255);
    }

    private enum Durability {
        None,
        Vanilla,
        Percentage
    }
}
