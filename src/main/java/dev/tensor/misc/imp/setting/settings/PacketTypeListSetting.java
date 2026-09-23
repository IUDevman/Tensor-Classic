package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.helpers.packet.PacketTypeList;
import dev.tensor.misc.imp.setting.Setting;
import net.minecraft.src.Packet;

/**
 * @author Hoosiers
 * @since 05-24-2026
 */

public class PacketTypeListSetting implements Setting<PacketTypeList> {

    private final String NAME;

    private final PacketTypeList VALUE;

    private final PacketTypeList FULL_VALUE;

    private final String DESCRIPTION;

    public PacketTypeListSetting(String NAME, PacketTypeList VALUE, PacketTypeList FULL_VALUE, String DESCRIPTION) {
        this.NAME = NAME;

        this.VALUE = VALUE;
        this.FULL_VALUE = FULL_VALUE;

        this.DESCRIPTION = DESCRIPTION;
    }

    @Override
    public String getValueTypeName() {
        return "PacketTypeList";
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Deprecated
    @Override
    public PacketTypeList getDefaultValue() {
        return null;
    }


    @Override
    public PacketTypeList getValue() {
        return this.VALUE;
    }

    public PacketTypeList getFullValue() {
        return this.FULL_VALUE;
    }

    @Override
    public String getValueString(boolean color) {
        return "";
    }

    @Deprecated
    @Override
    public void setValue(PacketTypeList VALUE) {

    }

    @Override
    public void setValueFromString(String string) throws Exception {
        this.getValue().loadPacketListFromString(string);
    }

    public void addPacket(Packet packet) {
        this.getValue().addToPacketList(packet);
        this.hasChanged();
    }

    public void addPacket(Class<? extends Packet> packet) {
        this.getValue().addToPacketList(packet);
        this.hasChanged();
    }

    public void addPacket(String convertedName) {
        this.getValue().addToPacketList(convertedName);
        this.hasChanged();
    }

    public void removePacket(Packet packet) {
        this.getValue().removeFromPacketList(packet);
        this.hasChanged();
    }

    public void removePacket(Class<? extends Packet> packet) {
        this.getValue().removeFromPacketList(packet);
        this.hasChanged();
    }

    public void removePacket(String convertedName) {
        this.getValue().removeFromPacketList(convertedName);
    }

    public boolean containsPacket(Packet packet) {
        return this.getValue().containsPacket(packet);
    }

    public boolean containsPacket(Class<? extends Packet> packet) {
        return this.getValue().containsPacket(packet);
    }

    public boolean containsPacket(String convertedName) {
        return this.getValue().containsPacket(convertedName);
    }

    public void hasChanged() {

    }

    @Override
    public void load(JsonElement jsonElement) {
        try {
            String value = jsonElement.getAsString();

            if (!value.equalsIgnoreCase("null")) {
                this.setValueFromString(value);
            }
        } catch (Exception ignored) {
            this.getTensorLogger().log("Failed to load PacketTypeListSetting: " + this.getName() + " for hack: " + this.getHackNameForSetting() + "!", TensorLogger.Level.ERROR);
        }
    }

    @Override
    public void save(JsonObject jsonObject) {
        String value = this.getValue().savePacketListToString();

        if (value == null || value.isEmpty() || value.equals(" ") || value.equals("_")) {
            value = "null";
        }

        jsonObject.add(this.getName(), new JsonPrimitive(value));
    }

    @Override
    public void reset() {

    }

    @Override
    public String getDescription() {
        return this.DESCRIPTION;
    }
}
