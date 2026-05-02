package dev.tensor.feature.hacks.hud;

import dev.tensor.feature.hacks.HephFreecam;
import dev.tensor.misc.helpers.entity.EntityPoint;
import dev.tensor.misc.helpers.entity.EntityTypeList;
import dev.tensor.misc.helpers.entity.FakePlayer;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.*;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityOtherPlayerMP;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Hoosiers
 * @since 05-01-2026
 */

@Hack.Info(name = "Radar", category = Category.HUD)
public final class Radar extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Shading_and_Images;
    }

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 498, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 0.75, 0.25, 2.00, 2);
    public final EnumSetting freecamMode = new EnumSetting("Freecam", Freecam.Camera);
    public final IntegerSetting radius = new IntegerSetting("Block Radius", 50, 10, 100);
    public final BooleanSetting players = new BooleanSetting("Players", true);
    public final BooleanSetting friends = new BooleanSetting("Friends", true);
    public final BooleanSetting animals = new BooleanSetting("Animals", true);
    public final EntityTypeListSetting customAnimals = new EntityTypeListSetting("Animals List", new EntityTypeList(EntityTypeList.ANIMALS()), new EntityTypeList(EntityTypeList.ANIMALS()));
    public final BooleanSetting hostiles = new BooleanSetting("Hostiles", true);
    public final EntityTypeListSetting customHostiles = new EntityTypeListSetting("Hostiles List", new EntityTypeList(EntityTypeList.HOSTILES()), new EntityTypeList(EntityTypeList.HOSTILES()));
    public final BooleanSetting items = new BooleanSetting("Items", false);
    public final BooleanSetting misc = new BooleanSetting("Misc", false);
    public final EntityTypeListSetting customMisc = new EntityTypeListSetting("Misc List", new EntityTypeList(EntityTypeList.EMPTY()), new EntityTypeList(EntityTypeList.MISC()));
    public final ColorSettingNoAlpha playersColor = new ColorSettingNoAlpha("Player Color", Color.ORANGE);
    public final ColorSettingNoAlpha friendsColor = new ColorSettingNoAlpha("Friend Color", Color.CYAN);
    public final ColorSettingNoAlpha animalsColor = new ColorSettingNoAlpha("Animal Color", Color.GREEN);
    public final ColorSettingNoAlpha hostilesColor = new ColorSettingNoAlpha("Hostile Color", Color.RED);
    public final ColorSettingNoAlpha itemColor = new ColorSettingNoAlpha("Item Color", Color.YELLOW);
    public final ColorSettingNoAlpha miscColor = new ColorSettingNoAlpha("Misc Color", Color.PINK);
    public final ColorSettingNoAlpha markerColors = new ColorSettingNoAlpha("Marker Color", Color.WHITE);
    public final ColorSetting fillColor = new ColorSetting("Fill Color", new Color(0, 0, 0, 150));
    public final ColorSetting outlineColor = new ColorSetting("Outline Color", new Color(62, 11, 10, 200));

    private final ArrayList<EntityPoint> pointsToRender = new ArrayList<>();

    private float playerYaw = 0;

    @Override
    public IntegerSetting getMinX() {
        return this.x;
    }

    @Override
    public IntegerSetting getMinY() {
        return this.y;
    }

    @Override
    public DoubleSetting getScale() {
        return this.scale;
    }

    @Override
    public int getWidth() {
        return 202;
    }

    @Override
    public int getHeight() {
        return 202;
    }

    @Override
    public int getWidthScaled() {
        return (int) (this.getWidth() * this.scale.getValue());
    }

    @Override
    public int getHeightScaled() {
        return (int) (this.getHeight() * this.scale.getValue());
    }

    @Override
    public void onDisable(boolean isNull) {
        this.pointsToRender.clear();
        this.playerYaw = 0;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onClientTick() {
        this.pointsToRender.clear();

        this.playerYaw = this.getPlayerYaw();

        double playerX = this.getPlayer().posX;
        double playerY = this.getPlayer().posY;
        double playerZ = this.getPlayer().posZ;

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        if (this.freecamMode.getValue().equals(Freecam.Player)) {
            if (freecam != null && freecam.isEnabled()) {
                playerX = freecam.oldX;
                playerY = freecam.oldY;
                playerZ = freecam.oldZ;
            }
        }

        int radius = this.radius.getValue();
        int radiusSq = (int) Math.pow(radius, 2);

        List entities = this.getWorld().loadedEntityList;

        for (Object object : entities) {
            Entity entity = (Entity) object;

            if (entity == null) {
                continue;
            }

            if (entity.ticksExisted < 1) {
                continue;
            }

            if (!entity.isEntityAlive()) {
                continue;
            }

            if (entity == this.getPlayer() || entity instanceof FakePlayer) {
                continue;
            }

            int color = this.getEntityColor(entity);

            if (color == -1) {
                continue;
            }

            if (!this.isEntityWithinRange(entity, playerX, playerZ, radiusSq)) {
                continue;
            }

            this.pointsToRender.add(new EntityPoint((int) (((double) this.getWidth() / 2) + (entity.posX - playerX) * 100 / radius), (int) (((double) this.getHeight() / 2) + (entity.posZ - playerZ) * 100 / radius), color, (int) (entity.posY - playerY)));
        }

        if (this.freecamMode.getValue().equals(Freecam.Camera)) {
            if (freecam != null && freecam.isEnabled()) {
                double freeX = freecam.oldX;
                double freeY = freecam.oldZ;

                double dX = freeX - playerX;
                double dY = freeY - playerZ;

                if (Math.pow(dX, 2) <= radiusSq && Math.pow(dY, 2) <= radiusSq) {
                    this.pointsToRender.add(new EntityPoint((int) (((double) this.getWidth() / 2) + dX * 100 / radius), (int) (((double) this.getHeight() / 2) + dY * 100 / radius), this.friendsColor.getValue().getRGB(), (int) (freecam.oldY - playerY)));
                }
            }
        }

        this.pointsToRender.sort(Comparator.comparing(EntityPoint::getWorldYDiff));
    }

    @Override
    public void render() {
        this.prepareScale(true);

        this.drawRectangle(1, 1, this.getWidth() - 1, this.getHeight() - 1, this.fillColor.getValue().getRGB());

        if (!this.pointsToRender.isEmpty()) {
            int radius = this.getPointRadius();

            this.pointsToRender.forEach(entityPoint -> entityPoint.render(radius));
        }

        this.drawCenteredString("N", this.getWidth() / 2, 5, this.markerColors.getValue().getRGB());
        this.drawCenteredString("S", this.getWidth() / 2, this.getHeight() - 11, this.markerColors.getValue().getRGB());
        this.drawCenteredString("E", this.getWidth() - 7, this.getHeight() / 2, this.markerColors.getValue().getRGB());
        this.drawCenteredString("W", 7, this.getHeight() / 2, this.markerColors.getValue().getRGB());

        this.drawRectangle(0, 0, 1, this.getHeight(), this.outlineColor.getValue().getRGB());
        this.drawRectangle(this.getWidth() - 1, 0, this.getWidth(), this.getHeight(), this.outlineColor.getValue().getRGB());
        this.drawRectangle(1, 0, this.getWidth() - 1, 1, this.outlineColor.getValue().getRGB());
        this.drawRectangle(1, this.getHeight() - 1, this.getWidth() - 1, this.getHeight(), this.outlineColor.getValue().getRGB());

        GL11.glTranslatef((float) this.getWidth() / 2, (float) this.getHeight() / 2, 0F);
        GL11.glRotatef(this.playerYaw, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef((float) -this.getWidth() / 2, (float) -this.getHeight() / 2, 0F);

        this.drawRectangle((this.getWidth() / 2) - 3, (this.getHeight() / 2) - 1, (this.getWidth() / 2) - 1, (this.getHeight() / 2) + 1, this.markerColors.getValue().getRGB());
        this.drawRectangle((this.getWidth() / 2) + 1, (this.getHeight() / 2) - 1, (this.getWidth() / 2) + 3, (this.getHeight() / 2) + 1, this.markerColors.getValue().getRGB());
        this.drawRectangle((this.getWidth() / 2) - 1, (this.getHeight() / 2) - 3, (this.getWidth() / 2) + 1, (this.getHeight() / 2) + 7, this.markerColors.getValue().getRGB());

        this.finishScale(true);
    }

    private float getPlayerYaw() {
        float yaw = this.getPlayer().rotationYaw;

        if (this.freecamMode.getValue().equals(Freecam.Player)) {
            HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

            if (freecam != null && freecam.isEnabled()) {
                yaw = freecam.oldYaw;
            }
        }

        while (yaw <= 0) {
            yaw += 360;
        }

        while (yaw > 360) {
            yaw -= 360;
        }

        return yaw;
    }

    private int getPointRadius() {
        int radius = 100 / this.radius.getValue();

        return Math.max(Math.min(radius, 3), 1);
    }

    private boolean isEntityWithinRange(Entity entity, double playerX, double playerZ, int radiusSq) {
        if (Math.pow(entity.posX - playerX, 2) > radiusSq) {
            return false;
        }

        return Math.pow(entity.posZ - playerZ, 2) <= radiusSq;
    }

    private int getEntityColor(Entity entity) {
        if (entity instanceof EntityOtherPlayerMP) {
            EntityOtherPlayerMP otherPlayer = (EntityOtherPlayerMP) entity;

            if (this.friends.getValue() && this.getFriendManager().isFriend(otherPlayer.username)) {
                return this.friendsColor.getValue().getRGB();
            }

            if (this.players.getValue()) {
                return this.playersColor.getValue().getRGB();
            }

            return -1;
        }

        String entityName = entity.getClass().getSimpleName();

        if (this.animals.getValue() && this.customAnimals.containsEntity(entityName)) {
            return this.animalsColor.getValue().getRGB();
        }

        if (this.hostiles.getValue() && this.customHostiles.containsEntity(entityName)) {
            return this.hostilesColor.getValue().getRGB();
        }

        if (this.items.getValue() && entity instanceof EntityItem) {
            return this.itemColor.getValue().getRGB();
        }

        if (this.misc.getValue() && this.customMisc.containsEntity(entityName)) {
            return this.miscColor.getValue().getRGB();
        }

        return -1;
    }

    private enum Freecam {
        Player,
        Camera
    }
}
