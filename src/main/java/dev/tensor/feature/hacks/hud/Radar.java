package dev.tensor.feature.hacks.hud;

import dev.tensor.feature.hacks.HephFreecam;
import dev.tensor.feature.hacks.LogoutSpots;
import dev.tensor.feature.hacks.Waypoints;
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
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 05-01-2026
 */

@Hack.Info(name = "Radar", category = Category.HUD, description = "Displays a 2D representation of entities, logout spots, and waypoints relative to your position.")
public final class Radar extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Shading_and_Images;
    }

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 498, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 0.75, 0.25, 2.00, 2);
    public final EnumSetting freecamMode = new EnumSetting("Freecam", Freecam.Camera, new String[]{
            "This is how the hack defines player coords.",
            "Player® Sets player coordinates independent of camera position.",
            "Camera® Sets coordinates to the player position even if in Freecam."
    });
    public final IntegerSetting radius = new IntegerSetting("Block Radius", 50, 10, 100);
    public final BooleanSetting players = new BooleanSetting("Players", true, "Enable to show players on the radar.");
    public final BooleanSetting friends = new BooleanSetting("Friends", true, "Enable to show friends on the radar.");
    public final BooleanSetting animals = new BooleanSetting("Animals", true, "Enable to show animals on the radar. Modify what types in the list below.");
    public final EntityTypeListSetting customAnimals = new EntityTypeListSetting("Animals List", new EntityTypeList(EntityTypeList.ANIMALS()), new EntityTypeList(EntityTypeList.ANIMALS()), "List of animals that should show up on the radar.");
    public final BooleanSetting hostiles = new BooleanSetting("Hostiles", true, "Enable to show hostile mobs on the radar. Modify what types in the list below.");
    public final EntityTypeListSetting customHostiles = new EntityTypeListSetting("Hostiles List", new EntityTypeList(EntityTypeList.HOSTILES()), new EntityTypeList(EntityTypeList.HOSTILES()), "List of hostile mobs that should show up on the radar.");
    public final BooleanSetting items = new BooleanSetting("Items", false, "Enable to show items on the radar.");
    public final BooleanSetting misc = new BooleanSetting("Misc", false, "Enable to show miscellaneous entities on the radar. Modify what types in the list below.");
    public final EntityTypeListSetting customMisc = new EntityTypeListSetting("Misc List", new EntityTypeList(EntityTypeList.EMPTY()), new EntityTypeList(EntityTypeList.MISC()), "List of miscellaneous entities that should show up on the radar.");
    public final BooleanSetting logoutSpots = new BooleanSetting("Logout Spots", false, "Enable to show Tensor logout spots on the radar.");
    public final BooleanSetting waypoints = new BooleanSetting("Waypoints", false, "Enable to show Tensor waypoints on the radar.");
    public final EnumSetting playerMarker = new EnumSetting("Player Marker", PlayerMarker.Big_Point, new String[]{
            "This defines what the marker for players looks like.",
            "Point® Players appear as the same point used for other entities.",
            "Big Point® Players appear as a point that is larger than other entities.",
            "Plus® Players appear as a plus symbol."
    });
    public final ColorNoAlphaSetting playersColor = new ColorNoAlphaSetting("Player Color", Color.ORANGE, "Sets the color of player markers.");
    public final ColorNoAlphaSetting friendsColor = new ColorNoAlphaSetting("Friend Color", Color.CYAN, "Sets the color of friend markers.");
    public final ColorNoAlphaSetting animalsColor = new ColorNoAlphaSetting("Animal Color", Color.GREEN, "Sets the color of animal markers.");
    public final ColorNoAlphaSetting hostilesColor = new ColorNoAlphaSetting("Hostile Color", Color.RED, "Sets the color of hostile mob markers.");
    public final ColorNoAlphaSetting itemColor = new ColorNoAlphaSetting("Item Color", Color.YELLOW, "Sets the color of item markers.");
    public final ColorNoAlphaSetting miscColor = new ColorNoAlphaSetting("Misc Color", Color.PINK, "Sets the color of miscellaneous entity markers.");
    public final ColorNoAlphaSetting waypointColor = new ColorNoAlphaSetting("Waypoint Color", Color.LIGHT_GRAY, "Sets the color of Tensor waypoint markers.");
    public final ColorNoAlphaSetting markerColors = new ColorNoAlphaSetting("Marker Color", Color.WHITE, "Sets the color of the center compass and direction markers.");
    public final ColorSetting fillColor = new ColorSetting("Fill Color", new Color(0, 0, 0, 150), "Sets the color of the HUD object fill.");
    public final ColorSetting outlineColor = new ColorSetting("Outline Color", new Color(62, 11, 10, 200), "Sets the color of HUD object outlines.");

    private final CopyOnWriteArrayList<EntityPoint> pointsToRender = new CopyOnWriteArrayList<>();

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

        int sphereRadius = this.getPointRadius();
        boolean shouldPlayerPlus = false;
        double bigPointMultiplier = 1;

        switch ((PlayerMarker) this.playerMarker.getValue()) {
            case Point:
                break;
            case Big_Point:
                bigPointMultiplier = 1.5;
                break;
            case Plus:
                shouldPlayerPlus = true;
                break;
        }

        int radius = this.radius.getValue();
        int radiusSq = (int) this.squaredD(radius * 1.05);

        final double finalBigPointMultiplier = bigPointMultiplier;
        final boolean finalShouldPlayerPlus = shouldPlayerPlus;

        final double finalPlayerX1 = playerX;
        final double finalPlayerY1 = playerY;
        final double finalPlayerZ1 = playerZ;

        this.getFilteredLoadedEntities().parallelStream().forEach(entity -> {
            if (entity == this.getPlayer() || entity instanceof FakePlayer) {
                return;
            }

            int color = this.getEntityColor(entity);

            if (color == -1) {
                return;
            }

            if (!this.isEntityWithinRange(entity, finalPlayerX1, finalPlayerZ1, radiusSq)) {
                return;
            }

            boolean point = false;
            double pointMultiplier = 1;

            if (entity instanceof EntityOtherPlayerMP) {
                point = finalShouldPlayerPlus;

                if (finalBigPointMultiplier != 1) {
                    pointMultiplier = finalBigPointMultiplier;
                }
            }

            this.pointsToRender.add(new EntityPoint((int) (((double) this.getWidth() / 2) + (entity.posX - finalPlayerX1) * 100 / radius), (int) (((double) this.getHeight() / 2) + (entity.posZ - finalPlayerZ1) * 100 / radius), color, (int) (entity.posY - finalPlayerY1), point, (int) (sphereRadius * pointMultiplier)));
        });

        if (this.logoutSpots.getValue()) {
            LogoutSpots logoutSpots1 = this.getHackManager().getHack(LogoutSpots.class);

            if (logoutSpots1 != null && logoutSpots1.isEnabled()) {
                if (!logoutSpots1.playersLogged.isEmpty()) {

                    final double finalPlayerX = playerX;
                    final double finalPlayerY = playerY;
                    final double finalPlayerZ = playerZ;

                    logoutSpots1.playersLogged.parallelStream().forEach(logoutSpot -> {
                        if (this.isWithinRange(logoutSpot.coords.x, logoutSpot.coords.z, finalPlayerX, finalPlayerZ, radiusSq)) {
                            int color = this.getFriendManager().isFriend(logoutSpot.username) ? this.friendsColor.getValueRainbow().getRGB() : this.playersColor.getValueRainbow().getRGB();

                            this.pointsToRender.add(new EntityPoint((int) (((double) this.getWidth() / 2) + (logoutSpot.coords.x - finalPlayerX) * 100 / radius), (int) (((double) this.getHeight() / 2) + (logoutSpot.coords.z - finalPlayerZ) * 100 / radius), color, (int) (logoutSpot.coords.y - finalPlayerY), true, sphereRadius));
                        }
                    });
                }
            }
        }

        if (this.waypoints.getValue()) {
            Waypoints waypoints1 = this.getHackManager().getHack(Waypoints.class);

            if (waypoints1 != null && waypoints1.isEnabled()) {
                if (!waypoints1.waypointsToRender.isEmpty()) {

                    int color = this.waypointColor.getValueRainbow().getRGB();

                    final double finalPlayerX = playerX;
                    final double finalPlayerY = playerY;
                    final double finalPlayerZ = playerZ;

                    waypoints1.waypointsToRender.parallelStream().forEach(waypoint -> {
                        if (this.isWithinRange(waypoint.getX(), waypoint.getZ(), finalPlayerX, finalPlayerZ, radiusSq)) {
                            this.pointsToRender.add(new EntityPoint((int) (((double) this.getWidth() / 2) + (waypoint.getX() - finalPlayerX) * 100 / radius), (int) (((double) this.getHeight() / 2) + (waypoint.getZ() - finalPlayerZ) * 100 / radius), color, (int) (waypoint.getY() - finalPlayerY), true, sphereRadius));
                        }
                    });
                }
            }
        }

        if (this.freecamMode.getValue().equals(Freecam.Camera)) {
            if (freecam != null && freecam.isEnabled()) {
                double freeX = freecam.oldX;
                double freeY = freecam.oldZ;

                double dX = freeX - playerX;
                double dY = freeY - playerZ;

                if (this.squaredD(dX) <= radiusSq && this.squaredD(dY) <= radiusSq) {
                    this.pointsToRender.add(new EntityPoint((int) (((double) this.getWidth() / 2) + dX * 100 / radius), (int) (((double) this.getHeight() / 2) + dY * 100 / radius), this.friendsColor.getValueRainbow().getRGB(), (int) (freecam.oldY - playerY), true, sphereRadius));
                }
            }
        }

        this.pointsToRender.sort(Comparator.comparing(EntityPoint::getWorldYDiff));
    }

    @Override
    public void render() {
        this.prepareScale(true);

        this.drawRectangle(1, 1, this.getWidth() - 1, this.getHeight() - 1, this.fillColor.getValueRainbow().getRGB());

        AtomicInteger prevColor = new AtomicInteger(0);

        if (!this.pointsToRender.isEmpty()) {
            this.startFlatGL(false);

            this.pointsToRender.forEach(entityPoint -> {

                int color = entityPoint.getColor();

                if (this.pointsToRender.get(0) == entityPoint) {
                    this.prepRectangle(color);
                    prevColor.set(color);

                } else if (color != prevColor.get()) {
                    this.swapRectangleColor(color);
                    prevColor.set(color);
                }

                entityPoint.render();
            });

            this.finishRectangle();
            this.finishFlatGL(false);
        }

        this.drawCenteredString("N", this.getWidth() / 2, 5, this.markerColors.getValueRainbow().getRGB());
        this.drawCenteredString("S", this.getWidth() / 2, this.getHeight() - 11, this.markerColors.getValueRainbow().getRGB());
        this.drawCenteredString("E", this.getWidth() - 7, this.getHeight() / 2, this.markerColors.getValueRainbow().getRGB());
        this.drawCenteredString("W", 7, this.getHeight() / 2, this.markerColors.getValueRainbow().getRGB());

        this.drawOutlinedRectangle(0, 0, this.getWidth(), this.getHeight(), 1, 0, this.outlineColor.getValueRainbow().getRGB(), false);

        GL11.glTranslatef((float) this.getWidth() / 2, (float) this.getHeight() / 2, 0F);
        GL11.glRotatef(this.playerYaw, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef((float) -this.getWidth() / 2, (float) -this.getHeight() / 2, 0F);

        this.startFlatGL(false);

        this.prepRectangle(this.markerColors.getValueRainbow().getRGB());

        this.genRectangleVertices((this.getWidth() / 2) - 3, (this.getHeight() / 2) - 1, (this.getWidth() / 2) - 1, (this.getHeight() / 2) + 1);
        this.genRectangleVertices((this.getWidth() / 2) + 1, (this.getHeight() / 2) - 1, (this.getWidth() / 2) + 3, (this.getHeight() / 2) + 1);
        this.genRectangleVertices((this.getWidth() / 2) - 1, (this.getHeight() / 2) - 3, (this.getWidth() / 2) + 1, (this.getHeight() / 2) + 7);

        this.finishRectangle();

        this.finishFlatGL(false);

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

    private boolean isWithinRange(double targetX, double targetZ, double playerX, double playerZ, int radiusSq) {
        if (this.squaredD(targetX - playerX) > radiusSq) {
            return false;
        }

        return this.squaredD(targetZ - playerZ) <= radiusSq;
    }

    private boolean isEntityWithinRange(Entity entity, double playerX, double playerZ, int radiusSq) {
        return this.isWithinRange(entity.posX, entity.posZ, playerX, playerZ, radiusSq);
    }

    private int getEntityColor(Entity entity) {
        if (entity instanceof EntityOtherPlayerMP) {
            EntityOtherPlayerMP otherPlayer = (EntityOtherPlayerMP) entity;

            if (this.friends.getValue() && this.getFriendManager().isFriend(otherPlayer.username)) {
                return this.friendsColor.getValueRainbow().getRGB();
            }

            if (this.players.getValue()) {
                return this.playersColor.getValueRainbow().getRGB();
            }

            return -1;
        }

        Class<? extends Entity> entityClass = entity.getClass();

        if (this.animals.getValue() && this.customAnimals.containsEntity(entityClass)) {
            return this.animalsColor.getValueRainbow().getRGB();
        }

        if (this.hostiles.getValue() && this.customHostiles.containsEntity(entityClass)) {
            return this.hostilesColor.getValueRainbow().getRGB();
        }

        if (this.items.getValue() && entity instanceof EntityItem) {
            return this.itemColor.getValueRainbow().getRGB();
        }

        if (this.misc.getValue() && this.customMisc.containsEntity(entityClass)) {
            return this.miscColor.getValueRainbow().getRGB();
        }

        return -1;
    }

    private enum Freecam {
        Player,
        Camera
    }

    private enum PlayerMarker {
        Point,
        Big_Point,
        Plus
    }
}
