package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ViewBobEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.entity.EntityTypeList;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.EntityTypeListSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.*;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Hoosiers
 * @since 04-02-2026
 */

@Hack.Info(name = "Tracers", category = Category.Render, description = "Draws a line to nearby players or other entities.")
public final class Tracers extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.Tracers;
    }

    public final BooleanSetting players = new BooleanSetting("Players", true, "Enable to draw tracers to players.");
    public final BooleanSetting friends = new BooleanSetting("Friends", true, "Enable to draw tracers to friends.");
    public final BooleanSetting animals = new BooleanSetting("Animals", false, "Enable to draw tracers to animals. Modify what types in the list below.");
    public final EntityTypeListSetting customAnimals = new EntityTypeListSetting("Animals List", new EntityTypeList(EntityTypeList.ANIMALS()), new EntityTypeList(EntityTypeList.ANIMALS()), "List of animals to draw tracers to.");
    public final BooleanSetting hostiles = new BooleanSetting("Hostiles", false, "Enable to draw tracers to hostile mobs. Modify what types in the list below.");
    public final EntityTypeListSetting customHostiles = new EntityTypeListSetting("Hostiles List", new EntityTypeList(EntityTypeList.HOSTILES()), new EntityTypeList(EntityTypeList.HOSTILES()), "List of hostile mobs to draw tracers to.");
    public final BooleanSetting items = new BooleanSetting("Items", false, "Enable to draw tracers to items on the ground.");
    public final BooleanSetting misc = new BooleanSetting("Misc", false, "Enable to draw tracers to miscellaneous entities. Modify what types in the list below.");
    public final EntityTypeListSetting customMisc = new EntityTypeListSetting("Misc List", new EntityTypeList(EntityTypeList.EMPTY()), new EntityTypeList(EntityTypeList.MISC()), "List of miscellaneous entities to draw tracers to.");
    public final IntegerSetting tracerWidth = new IntegerSetting("Tracers Width", 1, 1, 5);
    public final BooleanSetting distanceColor = new BooleanSetting("Color By Distance", false, "Enable to color the render by how close the entity is to the player");
    public final ColorSetting playersColor = new ColorSetting("Player Color", Color.ORANGE, "Sets the color of tracers for players.");
    public final ColorSetting friendsColor = new ColorSetting("Friend Color", Color.CYAN, "Sets the color of tracers for friends.");
    public final ColorSetting animalsColor = new ColorSetting("Animal Color", Color.GREEN, "Sets the color of tracers for animals.");
    public final ColorSetting hostilesColor = new ColorSetting("Hostile Color", Color.RED, "Sets the color of tracers for hostile mobs.");
    public final ColorSetting itemColor = new ColorSetting("Item Color", Color.YELLOW, "Sets the color of tracers for items.");
    public final ColorSetting miscColor = new ColorSetting("Misc Color", Color.PINK, "Sets the color of tracers for miscellaneous entities.");

    private final ConcurrentHashMap<Entity, Color> entitiesWithColorToTrace = new ConcurrentHashMap<>();

    @Override
    public void onDisable(boolean isNull) {
        this.entitiesWithColorToTrace.clear();
    }

    @Override
    public void onClientTick() {
        this.entitiesWithColorToTrace.clear();

        double[] playerPos;

        if (this.distanceColor.getValue()) {
            HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

            if (freecam != null && freecam.isEnabled()) {
                playerPos = new double[]{freecam.oldX, freecam.oldY, freecam.oldZ};

            } else {
                playerPos = new double[]{this.getPlayer().posX, this.getPlayer().posY, this.getPlayer().posZ};
            }
        } else {
            playerPos = null;
        }

        this.getFilteredLoadedEntities().parallelStream().forEach(entity -> {
            if (entity instanceof EntityOtherPlayerMP) {

                EntityOtherPlayerMP otherPlayer = (EntityOtherPlayerMP) entity;

                if (this.friends.getValue() && this.getFriendManager().isFriend(otherPlayer.username)) {
                    this.entitiesWithColorToTrace.put(otherPlayer, this.getColorForEntity(otherPlayer, playerPos, this.friendsColor.getValueRainbow()));
                    return;
                }

                if (this.players.getValue()) {
                    this.entitiesWithColorToTrace.put(otherPlayer, this.getColorForEntity(otherPlayer, playerPos, this.playersColor.getValueRainbow()));
                    return;
                }

                return;
            }

            Class<? extends Entity> entityClass = entity.getClass();

            if (this.animals.getValue() && this.customAnimals.containsEntity(entityClass)) {
                this.entitiesWithColorToTrace.put(entity, this.getColorForEntity(entity, playerPos, this.animalsColor.getValueRainbow()));
                return;
            }

            if (this.hostiles.getValue() && this.customHostiles.containsEntity(entityClass)) {
                this.entitiesWithColorToTrace.put(entity, this.getColorForEntity(entity, playerPos, this.hostilesColor.getValueRainbow()));
                return;
            }

            if (this.items.getValue() && entity instanceof EntityItem) {
                this.entitiesWithColorToTrace.put(entity, this.getColorForEntity(entity, playerPos, this.itemColor.getValueRainbow()));
                return;
            }

            if (this.misc.getValue() && this.customMisc.containsEntity(entityClass)) {
                this.entitiesWithColorToTrace.put(entity, this.getColorForEntity(entity, playerPos, this.miscColor.getValueRainbow()));
            }
        });
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        this.entitiesWithColorToTrace.forEach((entity, color) -> this.renderTracerForEntity(entity, color, renderPartialTicks));
    }

    @SuppressWarnings("unused")
    private final EventAction<ViewBobEvent> viewBobEventAction = this.addHackEventAction(new EventAction<>(ViewBobEvent.class, event -> {
        if (!this.entitiesWithColorToTrace.isEmpty()) {
            event.cancel();
        }
    }));

    private Color getColorForEntity(Entity entity, double[] playerPos, Color defaultColor) {
        if (this.distanceColor.getValue() && playerPos != null) {
            return this.getDistanceColor((int) this.getDistance(entity.posX - playerPos[0], entity.posY - playerPos[1], entity.posZ - playerPos[2]));
        }

        return defaultColor;
    }

    private void renderTracerForEntity(Entity entity, Color color, float renderPartialTicks) {
        AxisAlignedBB bb = entity.boundingBox;

        double x = bb.minX + ((bb.maxX - bb.minX) / 2) - RenderManager.renderPosX + this.translateOffset(entity.lastTickPosX, entity.posX, renderPartialTicks);
        double y = bb.minY + ((bb.maxY - bb.minY) / 2) - RenderManager.renderPosY + this.translateOffset(entity.lastTickPosY, entity.posY, renderPartialTicks);
        double z = bb.minZ + ((bb.maxZ - bb.minZ) / 2) - RenderManager.renderPosZ + this.translateOffset(entity.lastTickPosZ, entity.posZ, renderPartialTicks);

        this.drawTracer(x, y, z, this.tracerWidth.getValue(), color, color.getAlpha());
    }
}
