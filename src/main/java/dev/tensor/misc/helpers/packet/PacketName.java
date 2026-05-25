package dev.tensor.misc.helpers.packet;

import net.minecraft.src.*;

import java.util.HashMap;

/**
 * @author Hoosiers
 * @since 05-24-2026
 */

public final class PacketName {

    public static final PacketName CONVERTER = new PacketName();

    private final HashMap<Class<? extends Packet>, String> nameMap = new HashMap<>();

    private PacketName() {
        this.nameMap.put(Packet0KeepAlive.class, "Packet0KeepAlive");
        this.nameMap.put(Packet1Login.class, "Packet1Login");
        this.nameMap.put(Packet2Handshake.class, "Packet2Handshake");
        this.nameMap.put(Packet3Chat.class, "Packet3Chat");
        this.nameMap.put(Packet4UpdateTime.class, "Packet4UpdateTime");
        this.nameMap.put(Packet5PlayerInventory.class, "Packet5PlayerInventory");
        this.nameMap.put(Packet6SpawnPosition.class, "Packet6SpawnPosition");
        this.nameMap.put(Packet7UseEntity.class, "Packet7UseEntity");
        this.nameMap.put(Packet8UpdateHealth.class, "Packet8UpdateHealth");
        this.nameMap.put(Packet9Respawn.class, "Packet9Respawn");
        this.nameMap.put(Packet10Flying.class, "Packet10Flying");
        this.nameMap.put(Packet11PlayerPosition.class, "Packet11PlayerPosition");
        this.nameMap.put(Packet12PlayerLook.class, "Packet12PlayerLook");
        this.nameMap.put(Packet13PlayerLookMove.class, "Packet13PlayerLookMove");
        this.nameMap.put(Packet14BlockDig.class, "Packet14BlockDig");
        this.nameMap.put(Packet15Place.class, "Packet15Place");
        this.nameMap.put(Packet16BlockItemSwitch.class, "Packet16BlockItemSwitch");
        this.nameMap.put(Packet17Sleep.class, "Packet17Sleep");
        this.nameMap.put(Packet18Animation.class, "Packet18Animation");
        this.nameMap.put(Packet19EntityAction.class, "Packet19EntityAction");
        this.nameMap.put(Packet20NamedEntitySpawn.class, "Packet20NamedEntitySpawn");
        this.nameMap.put(Packet21PickupSpawn.class, "Packet21PickupSpawn");
        this.nameMap.put(Packet22Collect.class, "Packet22Collect");
        this.nameMap.put(Packet23VehicleSpawn.class, "Packet23VehicleSpawn");
        this.nameMap.put(Packet24MobSpawn.class, "Packet24MobSpawn.class");
        this.nameMap.put(Packet25EntityPainting.class, "Packet25EntityPainting");
        this.nameMap.put(Packet27Position.class, "Packet27Position");
        this.nameMap.put(Packet28EntityVelocity.class, "Packet28EntityVelocity");
        this.nameMap.put(Packet29DestroyEntity.class, "Packet29DestroyEntity");
        this.nameMap.put(Packet30Entity.class, "Packet30Entity");
        this.nameMap.put(Packet31RelEntityMove.class, "Packet31RelEntityMove");
        this.nameMap.put(Packet32EntityLook.class, "Packet32EntityLook");
        this.nameMap.put(Packet33RelEntityMoveLook.class, "Packet33RelEntityMoveLook");
        this.nameMap.put(Packet34EntityTeleport.class, "Packet34EntityTeleport");
        this.nameMap.put(Packet38EntityStatus.class, "Packet38EntityStatus");
        this.nameMap.put(Packet39AttachEntity.class, "Packet39AttachEntity");
        this.nameMap.put(Packet40EntityMetadata.class, "Packet40EntityMetadata");
        this.nameMap.put(Packet50PreChunk.class, "Packet50PreChunk");
        this.nameMap.put(Packet51MapChunk.class, "Packet51MapChunk");
        this.nameMap.put(Packet52MultiBlockChange.class, "Packet52MultiBlockChange");
        this.nameMap.put(Packet53BlockChange.class, "Packet53BlockChange");
        this.nameMap.put(Packet54PlayNoteBlock.class, "Packet54PlayNoteBlock");
        this.nameMap.put(Packet60Explosion.class, "Packet60Explosion");
        this.nameMap.put(Packet61DoorChange.class, "Packet61DoorChange");
        this.nameMap.put(Packet70Bed.class, "Packet70Bed");
        this.nameMap.put(Packet71Weather.class, "Packet71Weather");
        this.nameMap.put(Packet100OpenWindow.class, "Packet100OpenWindow");
        this.nameMap.put(Packet101CloseWindow.class, "Packet101CloseWindow");
        this.nameMap.put(Packet102WindowClick.class, "Packet102WindowClick");
        this.nameMap.put(Packet103SetSlot.class, "Packet103SetSlot");
        this.nameMap.put(Packet104WindowItems.class, "Packet104WindowItems");
        this.nameMap.put(Packet105UpdateProgressbar.class, "Packet105UpdateProgressbar");
        this.nameMap.put(Packet106Transaction.class, "Packet106Transaction");
        this.nameMap.put(Packet130UpdateSign.class, "Packet130UpdateSign");
        this.nameMap.put(Packet131MapData.class, "Packet131MapData");
        this.nameMap.put(Packet200Statistic.class, "Packet200Statistic");
        this.nameMap.put(Packet255KickDisconnect.class, "Packet255KickDisconnect");
    }

    public String convert(Class<? extends Packet> toConvert) {
        String result = this.nameMap.get(toConvert);

        if (result == null || result.isEmpty()) {
            return toConvert.getSimpleName();
        }

        return result;
    }
}
