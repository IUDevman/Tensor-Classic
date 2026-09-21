package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.Packet7UseEntityEvent;
import dev.tensor.backend.events.variable.RaytraceMOPEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.entity.EntityTypeList;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EntityTypeListSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.EntityWolf;

/**
 * @author Hoosiers
 * @since 09-09-2026
 */

@Hack.Info(name = "FriendlyFire", category = Category.Combat, description = "Prevents you from attacking designated entities.")
public final class FriendlyFire extends Hack {

    public final EnumSetting method = new EnumSetting("Method", Method.Both);
    public final BooleanSetting packetHit = new BooleanSetting("Packet Hit Only", true);
    public final BooleanSetting players = new BooleanSetting("Players", false);
    public final BooleanSetting ignoreFriends = new BooleanSetting("Friends", true);
    public final BooleanSetting ignorePets = new BooleanSetting("Ignore Pets", true);
    public final EnumSetting animals = new EnumSetting("Animals", Attack.None);
    public final EntityTypeListSetting animalsCustom = new EntityTypeListSetting("Animals List", new EntityTypeList(EntityTypeList.EMPTY()), new EntityTypeList(EntityTypeList.ANIMALS()));
    public final EnumSetting hostiles = new EnumSetting("Hostiles", Attack.None);
    public final EntityTypeListSetting hostilesCustom = new EntityTypeListSetting("Hostiles List", new EntityTypeList(EntityTypeList.EMPTY()), new EntityTypeList(EntityTypeList.HOSTILES()));
    public final EnumSetting misc = new EnumSetting("Misc", Attack.None);
    public final EntityTypeListSetting miscCustom = new EntityTypeListSetting("Misc List", new EntityTypeList(EntityTypeList.EMPTY()), new EntityTypeList(EntityTypeList.MISC_KA()));

    @SuppressWarnings("unused")
    private final EventAction<RaytraceMOPEvent> raytraceMOPEventAction = this.addHackEventAction(new EventAction<>(RaytraceMOPEvent.class, event -> {
        if (this.method.getValue().equals(Method.Packet)) {
            return;
        }

        if (event.raytraceHit != null && this.shouldProtectEntity(event.raytraceHit)) {
            event.cancel();
        }
    }));

    @SuppressWarnings("unused")
    private final EventAction<Packet7UseEntityEvent> packet7UseEntityEventAction = this.addHackEventAction(new EventAction<>(Packet7UseEntityEvent.class, event -> {
        if (this.method.getValue().equals(Method.Raytrace)) {
            return;
        }

        if (this.packetHit.getValue() && event.packet.isLeftClick != 1) {
            return;
        }

        int entityID = event.packet.targetEntity;

        Entity entity = this.getFilteredLoadedEntities().stream().filter(entity1 -> entity1.entityId == entityID).findFirst().orElse(null);

        if (entity != null && this.shouldProtectEntity(entity)) {
            event.cancel();
        }
    }));

    public boolean shouldProtectEntity(Entity entity) {
        if (entity instanceof EntityOtherPlayerMP) {
            if (this.players.getValue()) {
                return true;
            }

            if (!this.ignoreFriends.getValue()) {
                return false;
            }

            EntityOtherPlayerMP otherPlayerMP = (EntityOtherPlayerMP) entity;

            return this.getFriendManager().isFriend(otherPlayerMP.username);
        }

        Class<? extends Entity> entityClass = entity.getClass();

        if (this.hostilesCustom.getFullValue().containsEntity(entityClass)) {

            switch ((Attack) this.hostiles.getValue()) {
                case Custom:
                    return this.hostilesCustom.containsEntity(entityClass);
                case All:
                    return true;
                default:
                    return false;
            }
        }

        if (this.miscCustom.getFullValue().containsEntity(entityClass)) {

            switch ((Attack) this.misc.getValue()) {
                case Custom:
                    return this.miscCustom.containsEntity(entityClass);
                case All:
                    return true;
                default:
                    return false;
            }
        }

        if (this.animalsCustom.getFullValue().containsEntity(entityClass)) {

            if (entity instanceof EntityWolf) {

                String owner = ((EntityWolf) entity).getWolfOwner();

                if (owner.isEmpty()) {
                    return this.shouldProtectAnimal(entityClass);
                }

                if (owner.equalsIgnoreCase(this.getPlayer().username)) {
                    return !this.ignorePets.getValue() && this.shouldProtectAnimal(entityClass);
                }

                if (this.getFriendManager().isFriend(owner)) {
                    return !this.ignoreFriends.getValue() && this.players.getValue();
                }

                return this.players.getValue();

            } else {
                return this.shouldProtectAnimal(entityClass);
            }
        }

        return false;
    }

    private boolean shouldProtectAnimal(Class<? extends Entity> entityClass) {
        switch ((Attack) this.animals.getValue()) {
            case All:
                return true;
            case Custom:
                return this.animalsCustom.containsEntity(entityClass);
            case None:
                return false;
        }

        return false;
    }

    public enum Method {
        Raytrace,
        Packet,
        Both
    }

    public enum Attack {
        All,
        None,
        Custom
    }
}
