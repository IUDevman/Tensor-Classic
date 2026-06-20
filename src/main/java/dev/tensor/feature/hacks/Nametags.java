package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.NameTagEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventConstantCancellable;
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
import java.util.concurrent.CopyOnWriteArrayList;

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

    @Override
    public boolean overridesBlockGL() {
        return true;
    }

    public final BooleanSetting friends = new BooleanSetting("Friends", true);
    public final BooleanSetting darkenSneak = new BooleanSetting("Darken Sneak", false);
    public final BooleanSetting showHealth = new BooleanSetting("Show Health", false);
    public final BooleanSetting showDistance = new BooleanSetting("Show Distance", true);
    public final BooleanSetting showItems = new BooleanSetting("Show Items", true);
    public final EnumSetting durability = new EnumSetting("Durability", Durability.Percentage);
    public final BooleanSetting background = new BooleanSetting("Background", false);
    public final IntegerSetting distanceMultiplier = new IntegerSetting("Distance Multiplier", 6, 2, 10);
    public final ColorSetting textColor = new ColorSetting("Text Color", Color.ORANGE);
    public final ColorSetting friendColor = new ColorSetting("Friend Color", Color.CYAN);
    public final ColorSetting backgroundColor = new ColorSetting("Background Color", new Color(0, 0, 0, 150));

    private final CopyOnWriteArrayList<EntityPlayer> playersToRender = new CopyOnWriteArrayList<>();

    @Override
    public void onDisable(boolean isNull) {
        this.playersToRender.clear();
    }

    @Override
    public void onClientTick() {
        this.playersToRender.clear();

        this.getFilteredLoadedEntities().parallelStream().forEach(entity -> {
            if (entity instanceof EntityOtherPlayerMP || entity instanceof FakePlayer) {
                this.playersToRender.add((EntityPlayer) entity);
            }
        });
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        double x = this.getPlayer().posX;
        double y = this.getPlayer().posY;
        double z = this.getPlayer().posZ;

        if (this.showDistance.getValue()) {
            HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

            if (freecam != null && freecam.isEnabled()) {
                x = freecam.oldX;
                y = freecam.oldY;
                z = freecam.oldZ;
            }
        }

        final double[] distance = new double[]{x, y, z};

        this.playersToRender.forEach(entityPlayer -> this.renderNameTagForPlayer(entityPlayer, renderPartialTicks, distance));
    }

    @SuppressWarnings("unused")
    private final EventAction<NameTagEvent> nameTagEventAction = this.addEventAction(new EventAction<>(NameTagEvent.class, EventConstantCancellable::cancel));

    private void renderNameTagForPlayer(EntityPlayer entity, float renderPartialTicks, double[] playerCoords) {
        String username = entity.username;

        Color textColor = this.friends.getValue() && this.getFriendManager().isFriend(username) ? this.friendColor.getValueRainbow() : username.equalsIgnoreCase(this.getPlayer().username) ? this.friendColor.getValueRainbow() : this.textColor.getValueRainbow();
        Color fillColor = this.backgroundColor.getValueRainbow();

        String health = "";
        String healthPost = "";

        boolean showDistanceForEntity = this.showDistance.getValue() && !username.equalsIgnoreCase(this.getPlayer().username);
        int distanceVal = showDistanceForEntity ? this.getDistanceTo(entity, playerCoords) : 0;

        String distancePre = " [";
        String distance = showDistanceForEntity ? String.valueOf(distanceVal) : "";
        String distancePost = "]";

        Color healthColor = null;

        if (this.showHealth.getValue()) {
            username += " [";
            health = String.valueOf(entity.health);
            healthPost = "]";
            healthColor = this.getHealthColor(entity);
        }

        String combined = username + health + healthPost + distance + (showDistanceForEntity ? distancePre + distancePost : "");

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

        int healthStartX = -(this.getFontRenderer().getStringWidth(combined) / 2) + this.getFontRenderer().getStringWidth(username);
        int healthEndX = 0;

        if (this.showHealth.getValue() && healthColor != null) {
            healthEndX = this.getFontRenderer().getStringWidth(health);

            this.drawString(health, healthStartX, 0, healthColor.getRGB());
            this.drawString(healthPost, healthStartX + healthEndX, 0, textColor.getRGB());

            healthEndX += this.getFontRenderer().getStringWidth(healthPost);
        }

        if (showDistanceForEntity) {
            int distanceStart = healthStartX + healthEndX;

            this.drawString(distancePre, distanceStart, 0, textColor.getRGB());

            distanceStart += this.getFontRenderer().getStringWidth(distancePre);

            this.drawString(distance, distanceStart, 0, this.getDistanceColor(distanceVal).getRGB());

            distanceStart += this.getFontRenderer().getStringWidth(distance);

            this.drawString(distancePost, distanceStart, 0, textColor.getRGB());
        }

        if (this.showItems.getValue()) {
            int handInt = entity.inventory.currentItem;

            ItemStack hand = entity.inventory.getCurrentItem();

            if (handInt == 9) {
                hand = entity.inventory.mainInventory[9];
            }

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

    private int getDistanceTo(EntityPlayer entity, double[] playerCoords) {
        return (int) this.getDistance(entity.posX - playerCoords[0], entity.posY - playerCoords[1], entity.posZ - playerCoords[2]);
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
