package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.GeneralPacketEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.packet.PacketTypeList;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.PacketTypeListSetting;

/**
 * @author Hoosiers
 * @since 05-24-2026
 */

@Hack.Info(name = "CancelPackets", category = Category.Misc, description = "Selectively block packets shared between the client and the server. May break other features of the client, use with caution!")
public final class CancelPackets extends Hack {

    public final BooleanSetting outbound = new BooleanSetting("Filter Outbound", true, "Enable to filter sent packets as defined in the list.");
    public final PacketTypeListSetting outboundList = new PacketTypeListSetting("Outbound List", new PacketTypeList(PacketTypeList.NONE()), new PacketTypeList(PacketTypeList.OUTBOUND()), "Add packets to the list to ignore when sent.");
    public final BooleanSetting inbound = new BooleanSetting("Filter Inbound", true, "Enable to filter received packets as defined in the list.");
    public final PacketTypeListSetting inboundList = new PacketTypeListSetting("Inbound List", new PacketTypeList(PacketTypeList.NONE()), new PacketTypeList(PacketTypeList.INBOUND()), "Add packets to the list to ignore when received.");

    @SuppressWarnings("unused")
    private final EventAction<GeneralPacketEvent> generalPacketEventAction = this.addHackEventAction(new EventAction<>(GeneralPacketEvent.class, event -> {
        if (this.inbound.getValue() && event.type.equals(GeneralPacketEvent.Type.INBOUND)) {

            if (this.inboundList.containsPacket(event.packet)) {
                event.cancel();
            }

            return;
        }

        if (this.outbound.getValue() && event.type.equals(GeneralPacketEvent.Type.OUTBOUND)) {

            if (this.outboundList.containsPacket(event.packet)) {
                event.cancel();
            }
        }
    }));
}
