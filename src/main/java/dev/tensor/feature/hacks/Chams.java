package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.variable.EntityLightValueEvent;
import dev.tensor.backend.events.variable.RenderEntityEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.entity.EntityTypeList;
import dev.tensor.misc.helpers.entity.FakePlayer;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.render.RenderStatic;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EntityTypeListSetting;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.RenderManager;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 05-14-2026
 */

@Hack.Info(name = "Chams", category = Category.Render)
public final class Chams extends Hack {

    public final BooleanSetting players = new BooleanSetting("Players", true);
    public final BooleanSetting friends = new BooleanSetting("Friends", true);
    public final BooleanSetting animals = new BooleanSetting("Animals", true);
    public final EntityTypeListSetting customAnimals = new EntityTypeListSetting("Animals List", new EntityTypeList(EntityTypeList.ANIMALS()), new EntityTypeList(EntityTypeList.ANIMALS()));
    public final BooleanSetting hostiles = new BooleanSetting("Hostiles", true);
    public final EntityTypeListSetting customHostiles = new EntityTypeListSetting("Hostiles List", new EntityTypeList(EntityTypeList.HOSTILES()), new EntityTypeList(EntityTypeList.HOSTILES()));
    public final BooleanSetting items = new BooleanSetting("Items", false);
    public final BooleanSetting misc = new BooleanSetting("Misc", false);
    public final EntityTypeListSetting customMisc = new EntityTypeListSetting("Misc List", new EntityTypeList(EntityTypeList.EMPTY()), new EntityTypeList(EntityTypeList.MISC()));
    public final BooleanSetting frustrum = new BooleanSetting("Frustrum Check", true);

    private final ArrayList<Integer> cached_Entity_IDs = new ArrayList<>();

    @Override
    public void onDisable(boolean isNull) {
        this.resetEntityCache();
    }

    @SuppressWarnings("unused")
    private final EventAction<RenderEntityEvent> renderEntityEventAction = this.addHackEventAction(new EventAction<>(RenderEntityEvent.class, event -> {
        if (this.shouldRenderChams(event.entity)) {
            event.cancel();
        }
    }));

    @SuppressWarnings("unused")
    private final EventAction<EntityLightValueEvent> entityLightValueEventAction = this.addHackEventAction(new EventAction<>(EntityLightValueEvent.class, event -> {
        if (this.cached_Entity_IDs.contains(event.entityID)) {
            event.cancel();
        }
    }));

    @SuppressWarnings("unused")
    private final EventAction<ChangeWorldEvent> changeWorldEventAction = this.addHackEventAction(new EventAction<>(ChangeWorldEvent.class, event -> {
        this.resetEntityCache();
    }));

    private boolean shouldRenderChams(Entity entity) {
        if (this.cached_Entity_IDs.contains(entity.entityId)) {
            return true;
        }

        if (entity instanceof FakePlayer) {
            return this.passesFrustrumCheck(entity, this.players.getValue());
        }

        if (entity instanceof EntityOtherPlayerMP) {

            EntityOtherPlayerMP otherPlayer = (EntityOtherPlayerMP) entity;

            if (this.friends.getValue() && this.getFriendManager().isFriend(otherPlayer.username)) {
                return this.passesFrustrumCheck(entity, true);
            }

            return this.passesFrustrumCheck(entity, this.players.getValue());
        }

        Class<? extends Entity> entityClass = entity.getClass();

        if (this.animals.getValue() && this.customAnimals.containsEntity(entityClass)) {
            return this.passesFrustrumCheck(entity, true);
        }

        if (this.hostiles.getValue() && this.customHostiles.containsEntity(entityClass)) {
            return this.passesFrustrumCheck(entity, true);
        }

        if (this.items.getValue() && entity instanceof EntityItem) {
            return this.passesFrustrumCheck(entity, true);
        }

        if (this.misc.getValue()) {
            return this.passesFrustrumCheck(entity, this.customMisc.containsEntity(entityClass));

        } else return false;
    }

    private boolean passesFrustrumCheck(Entity entity, boolean returnValue) {
        if (returnValue && this.frustrum.getValue()) {
            RenderStatic.setFrustrumPosition(RenderManager.renderPosX, RenderManager.renderPosY, RenderManager.renderPosZ);

            if (!RenderStatic.isBoundingBoxInFrustum(entity.boundingBox)) {
                return false;
            }
        }

        if (returnValue) {
            this.cached_Entity_IDs.add(entity.entityId);
        }

        return returnValue;
    }

    public void resetEntityCache() {
        this.cached_Entity_IDs.clear();
    }
}
