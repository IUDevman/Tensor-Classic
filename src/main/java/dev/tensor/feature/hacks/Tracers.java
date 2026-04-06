package dev.tensor.feature.hacks;

import dev.tensor.backend.events.ViewBobEvent;
import dev.tensor.misc.helpers.entity.EntityTypeList;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.EntityTypeListSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.*;

import java.awt.*;
import java.util.List;

/**
 * @author Hoosiers
 * @since 04-02-2026
 */

@Hack.Info(name = "Tracers", category = Category.Render)
public final class Tracers extends Hack {

    public final BooleanSetting players = new BooleanSetting("Players", true);
    public final BooleanSetting friends = new BooleanSetting("Friends", true);
    public final BooleanSetting animals = new BooleanSetting("Animals", false);
    public final EntityTypeListSetting customAnimals = new EntityTypeListSetting("Animals List", new EntityTypeList(EntityTypeList.ANIMALS()), new EntityTypeList(EntityTypeList.ANIMALS()));
    public final BooleanSetting hostiles = new BooleanSetting("Hostiles", false);
    public final EntityTypeListSetting customHostiles = new EntityTypeListSetting("Hostiles List", new EntityTypeList(EntityTypeList.HOSTILES()), new EntityTypeList(EntityTypeList.HOSTILES()));
    public final BooleanSetting items = new BooleanSetting("Items", false);
    public final BooleanSetting misc = new BooleanSetting("Misc", false);
    public final EntityTypeListSetting customMisc = new EntityTypeListSetting("Misc List", new EntityTypeList(EntityTypeList.EMPTY()), new EntityTypeList(EntityTypeList.MISC()));
    public final IntegerSetting tracerWidth = new IntegerSetting("Tracers Width", 1, 1, 5);
    public final ColorSetting playersColor = new ColorSetting("Player Color", Color.ORANGE);
    public final ColorSetting friendsColor = new ColorSetting("Friend Color", Color.CYAN);
    public final ColorSetting animalsColor = new ColorSetting("Animal Color", Color.GREEN);
    public final ColorSetting hostilesColor = new ColorSetting("Hostile Color", Color.RED);
    public final ColorSetting itemColor = new ColorSetting("Item Color", Color.YELLOW);
    public final ColorSetting miscColor = new ColorSetting("Misc Color", Color.PINK);

    @SuppressWarnings("unused")
    @EventTarget
    public void onViewBobEvent(ViewBobEvent event) {
        event.cancel();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onRender3D() {

        List entities = this.getWorld().loadedEntityList;

        for (int i = 0; i < entities.size(); i++) {

            Entity entity = (Entity) entities.get(i);

            if (entity == null) {
                continue;
            }

            if (entity instanceof EntityOtherPlayerMP) {

                EntityOtherPlayerMP otherPlayer = (EntityOtherPlayerMP) entity;

                if (this.friends.getValue() && this.getFriendManager().isFriend(otherPlayer.username)) {
                    this.renderTracerForEntity(otherPlayer, this.friendsColor.getValue());
                    continue;
                }

                if (this.players.getValue()) {
                    this.renderTracerForEntity(otherPlayer, this.playersColor.getValue());
                    continue;
                }

                continue;
            }

            String entityName = entity.getClass().getSimpleName();

            if (this.animals.getValue() && this.customAnimals.containsEntity(entityName)) {
                this.renderTracerForEntity(entity, this.animalsColor.getValue());
                continue;
            }

            if (this.hostiles.getValue() && this.customHostiles.containsEntity(entityName)) {
                this.renderTracerForEntity(entity, this.hostilesColor.getValue());
                continue;
            }

            if (this.items.getValue() && entity instanceof EntityItem) {
                this.renderTracerForEntity(entity, this.itemColor.getValue());
                continue;
            }

            if (this.misc.getValue() && this.customMisc.containsEntity(entityName)) {
                this.renderTracerForEntity(entity, this.miscColor.getValue());
            }
        }
    }

    private void renderTracerForEntity(Entity entity, Color color) {
        AxisAlignedBB bb = entity.boundingBox;

        double x = bb.minX + ((bb.maxX - bb.minX) / 2) - RenderManager.renderPosX;
        double y = bb.minY + ((bb.maxY - bb.minY) / 2) - RenderManager.renderPosY;
        double z = bb.minZ + ((bb.maxZ - bb.minZ) / 2) - RenderManager.renderPosZ;

        this.drawTracer(x, y, z, this.tracerWidth.getValue(), color, color.getAlpha());
    }
}
