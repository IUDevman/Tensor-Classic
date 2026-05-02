package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.entity.EntityTypeList;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.*;
import net.minecraft.src.*;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * @author Hoosiers
 * @since 03-31-2026
 */

@Hack.Info(name = "EntityESP", category = Category.Render)
public final class EntityESP extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.EntityESP;
    }

    public final BooleanSetting players = new BooleanSetting("Players", true);
    public final BooleanSetting friends = new BooleanSetting("Friends", true);
    public final BooleanSetting animals = new BooleanSetting("Animals", true);
    public final EntityTypeListSetting customAnimals = new EntityTypeListSetting("Animals List", new EntityTypeList(EntityTypeList.ANIMALS()), new EntityTypeList(EntityTypeList.ANIMALS()));
    public final BooleanSetting hostiles = new BooleanSetting("Hostiles", true);
    public final EntityTypeListSetting customHostiles = new EntityTypeListSetting("Hostiles List", new EntityTypeList(EntityTypeList.HOSTILES()), new EntityTypeList(EntityTypeList.HOSTILES()));
    public final BooleanSetting items = new BooleanSetting("Items", false);
    public final BooleanSetting misc = new BooleanSetting("Misc", false);
    public final EntityTypeListSetting customMisc = new EntityTypeListSetting("Misc List", new EntityTypeList(EntityTypeList.EMPTY()), new EntityTypeList(EntityTypeList.MISC()));
    public final ColorSetting playersColor = new ColorSetting("Player Color", Color.ORANGE);
    public final ColorSetting friendsColor = new ColorSetting("Friend Color", Color.CYAN);
    public final ColorSetting animalsColor = new ColorSetting("Animal Color", Color.GREEN);
    public final ColorSetting hostilesColor = new ColorSetting("Hostile Color", Color.RED);
    public final ColorSetting itemColor = new ColorSetting("Item Color", Color.YELLOW);
    public final ColorSetting miscColor = new ColorSetting("Misc Color", Color.PINK);
    public final EnumSetting mode = new EnumSetting("Render", Mode.Outline);
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5);
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 50, 0, 255);

    private final HashMap<Entity, Color> entitiesWithColorToRender = new HashMap<>();

    @Override
    public void onDisable(boolean isNull) {
        this.entitiesWithColorToRender.clear();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onClientTick() {
        this.entitiesWithColorToRender.clear();

        List entities = this.getWorld().loadedEntityList;

        for (int i = 0; i < entities.size(); i++) {

            Entity entity = (Entity) entities.get(i);

            if (entity == null) {
                continue;
            }

            if (entity instanceof EntityLiving) {
                if (entity.ticksExisted < 1) {
                    continue;
                }

                if (!entity.isEntityAlive()) {
                    continue;
                }
            }

            if (entity instanceof EntityOtherPlayerMP) {

                EntityOtherPlayerMP otherPlayer = (EntityOtherPlayerMP) entity;

                if (this.friends.getValue() && this.getFriendManager().isFriend(otherPlayer.username)) {
                    this.entitiesWithColorToRender.put(otherPlayer, this.friendsColor.getValue());
                    continue;
                }

                if (this.players.getValue()) {
                    this.entitiesWithColorToRender.put(otherPlayer, this.playersColor.getValue());
                    continue;
                }

                continue;
            }

            String entityName = entity.getClass().getSimpleName();

            if (this.animals.getValue() && this.customAnimals.containsEntity(entityName)) {
                this.entitiesWithColorToRender.put(entity, this.animalsColor.getValue());
                continue;
            }

            if (this.hostiles.getValue() && this.customHostiles.containsEntity(entityName)) {
                this.entitiesWithColorToRender.put(entity, this.hostilesColor.getValue());
                continue;
            }

            if (this.items.getValue() && entity instanceof EntityItem) {
                this.entitiesWithColorToRender.put(entity, this.itemColor.getValue());
                continue;
            }

            if (this.misc.getValue() && this.customMisc.containsEntity(entityName)) {
                this.entitiesWithColorToRender.put(entity, this.miscColor.getValue());
            }
        }
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        this.entitiesWithColorToRender.forEach((entity, color) -> this.renderESPForEntity(entity, color, renderPartialTicks));
    }

    private void renderESPForEntity(Entity entity, Color color, float renderPartialTicks) {
        AxisAlignedBB bb = entity.boundingBox;

        double dX = this.translateOffset(entity.lastTickPosX, entity.posX, renderPartialTicks);
        double dY = this.translateOffset(entity.lastTickPosY, entity.posY, renderPartialTicks);
        double dZ = this.translateOffset(entity.lastTickPosZ, entity.posZ, renderPartialTicks);

        double x = bb.minX - RenderManager.renderPosX + dX;
        double y = bb.minY - RenderManager.renderPosY + dY;
        double z = bb.minZ - RenderManager.renderPosZ + dZ;

        double wX = bb.maxX - RenderManager.renderPosX + dX;
        double wY = bb.maxY - RenderManager.renderPosY + dY;
        double wZ = bb.maxZ - RenderManager.renderPosZ + dZ;

        switch ((Mode) this.mode.getValue()) {
            case Outline:
                this.drawOutlineBox(x, y, z, wX, wY, wZ, this.lineWidth.getValue(), color);
                break;
            case Fill:
                this.drawFillBox(x, y, z, wX, wY, wZ, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                break;
            case Both:
                this.drawFillBox(x, y, z, wX, wY, wZ, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                this.drawOutlineBox(x, y, z, wX, wY, wZ, this.lineWidth.getValue(), color);
                break;
        }
    }

    public enum Mode {
        Outline,
        Fill,
        Both
    }
}
