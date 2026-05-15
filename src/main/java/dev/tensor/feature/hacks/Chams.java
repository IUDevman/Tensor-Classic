package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.EntityLightValueEvent;
import dev.tensor.backend.events.variable.RenderEntityEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.entity.EntityTypeList;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EntityTypeListSetting;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityOtherPlayerMP;

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

    @SuppressWarnings("unused")
    EventAction<RenderEntityEvent> renderEntityEventAction = this.addEventAction(new EventAction<>(RenderEntityEvent.class, event -> {
        if (this.shouldRenderChams(event.entity)) {
            event.cancel();
        }
    }));

    @SuppressWarnings("unused")
    EventAction<EntityLightValueEvent> entityLightValueEventEAction = this.addEventAction(new EventAction<>(EntityLightValueEvent.class, event -> {
        if (this.shouldRenderChams(event.entity)) {
            event.cancel();
        }
    }));

    private boolean shouldRenderChams(Entity entity) {
        if (entity instanceof EntityOtherPlayerMP) {

            EntityOtherPlayerMP otherPlayer = (EntityOtherPlayerMP) entity;

            if (this.friends.getValue() && this.getFriendManager().isFriend(otherPlayer.username)) {
                return true;
            }

            return this.players.getValue();
        }

        Class<? extends Entity> entityClass = entity.getClass();

        if (this.animals.getValue() && this.customAnimals.containsEntity(entityClass)) {
            return true;
        }

        if (this.hostiles.getValue() && this.customHostiles.containsEntity(entityClass)) {
            return true;
        }

        if (this.items.getValue() && entity instanceof EntityItem) {
            return true;
        }

        return this.misc.getValue() && this.customMisc.containsEntity(entityClass);
    }
}
