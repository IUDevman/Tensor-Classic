package dev.tensor.misc.helpers.packet;

import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Hoosiers
 * @since 05-24-2026
 */

public final class PacketTypeList {

    private final ArrayList<String> packetListDefault;
    private final ArrayList<String> packetList;

    public PacketTypeList(ArrayList<String> packetList) {
        this.packetListDefault = new ArrayList<>(packetList);
        this.packetList = new ArrayList<>(packetList);
    }

    public ArrayList<String> getPacketList() {
        return this.packetList;
    }

    public void addToPacketList(Packet packet) {
        this.addToPacketList(packet.getClass());
    }

    public void addToPacketList(Class<? extends Packet> packet) {
        String name = PacketName.CONVERTER.convert(packet);

        this.addToPacketList(name);
    }

    public void addToPacketList(String convertedName) {
        if (!this.packetList.contains(convertedName)) {
            this.packetList.add(convertedName);
        }
    }

    public void removeFromPacketList(Packet packet) {
        this.removeFromPacketList(packet.getClass());
    }

    public void removeFromPacketList(Class<? extends Packet> packet) {
        String name = PacketName.CONVERTER.convert(packet);

        this.removeFromPacketList(name);
    }

    public void removeFromPacketList(String convertedName) {
        this.packetList.remove(convertedName);
    }

    public void clearPacketList() {
        this.packetList.clear();
    }

    public void resetPacketList() {
        this.clearPacketList();
        this.packetList.addAll(this.packetListDefault);
    }

    public boolean containsPacket(Packet packet) {
        return this.containsPacket(packet.getClass());
    }

    public boolean containsPacket(Class<? extends Packet> packet) {
        String name = PacketName.CONVERTER.convert(packet);

        return this.containsPacket(name);
    }

    public boolean containsPacket(String convertedName) {
        return this.packetList.contains(convertedName);
    }

    public void loadPacketListFromString(String string) {
        this.clearPacketList();

        if (string.equalsIgnoreCase("null")) {
            return;
        }

        String[] pooledString = string.split("_");

        for (int i = 0; i < pooledString.length; i++) {
            this.addToPacketList(pooledString[i]);
        }
    }

    public String savePacketListToString() {
        if (this.packetList.isEmpty()) {
            return "null";
        }

        AtomicReference<String> list = new AtomicReference<>("");

        this.packetList.forEach(packet -> list.set(list.get() + packet + "_"));
        list.set(list.get().substring(0, list.get().length() - 1));

        return list.get();
    }

    private static final ArrayList<String> INBOUND = new ArrayList<>();

    public static ArrayList<String> INBOUND() {
        if (INBOUND.isEmpty()) {
            INBOUND.add(PacketName.CONVERTER.convert(Packet0KeepAlive.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet1Login.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet2Handshake.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet3Chat.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet4UpdateTime.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet5PlayerInventory.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet6SpawnPosition.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet8UpdateHealth.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet9Respawn.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet10Flying.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet11PlayerPosition.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet12PlayerLook.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet13PlayerLookMove.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet17Sleep.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet18Animation.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet20NamedEntitySpawn.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet21PickupSpawn.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet22Collect.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet23VehicleSpawn.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet24MobSpawn.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet25EntityPainting.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet28EntityVelocity.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet29DestroyEntity.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet30Entity.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet31RelEntityMove.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet32EntityLook.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet33RelEntityMoveLook.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet34EntityTeleport.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet38EntityStatus.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet39AttachEntity.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet40EntityMetadata.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet50PreChunk.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet51MapChunk.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet52MultiBlockChange.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet53BlockChange.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet54PlayNoteBlock.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet60Explosion.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet61DoorChange.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet70Bed.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet71Weather.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet100OpenWindow.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet101CloseWindow.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet103SetSlot.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet104WindowItems.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet105UpdateProgressbar.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet106Transaction.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet130UpdateSign.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet131MapData.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet200Statistic.class));
            INBOUND.add(PacketName.CONVERTER.convert(Packet255KickDisconnect.class));
        }

        return new ArrayList<>(INBOUND);
    }

    private static final ArrayList<String> OUTBOUND = new ArrayList<>();

    public static ArrayList<String> OUTBOUND() {
        if (OUTBOUND.isEmpty()) {

            OUTBOUND.add(PacketName.CONVERTER.convert(Packet0KeepAlive.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet1Login.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet2Handshake.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet3Chat.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet7UseEntity.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet9Respawn.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet10Flying.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet11PlayerPosition.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet12PlayerLook.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet13PlayerLookMove.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet14BlockDig.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet15Place.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet16BlockItemSwitch.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet18Animation.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet19EntityAction.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet27Position.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet101CloseWindow.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet102WindowClick.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet106Transaction.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet130UpdateSign.class));
            OUTBOUND.add(PacketName.CONVERTER.convert(Packet255KickDisconnect.class));
        }

        return new ArrayList<>(OUTBOUND);
    }

    public static ArrayList<String> NONE() {
        return new ArrayList<>();
    }
}
