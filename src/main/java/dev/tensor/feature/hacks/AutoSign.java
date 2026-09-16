package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.SignEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.StringSetting;
import net.minecraft.src.Packet130UpdateSign;

/**
 * @author Hoosiers
 * @since 04-17-2026
 */

@Hack.Info(name = "AutoSign", category = Category.Misc)
public final class AutoSign extends Hack {

    public final StringSetting lineOne = new StringSetting("Line 1", "Tensor :)");
    public final StringSetting lineTwo = new StringSetting("Line 2", "Tensor :D");
    public final StringSetting lineThree = new StringSetting("Line 3", "Tensor :)");
    public final StringSetting lineFour = new StringSetting("Line 4", "Tensor :D");

    @SuppressWarnings("unused")
    private final EventAction<SignEvent> signEventAction = this.addHackEventAction(new EventAction<>(SignEvent.class, event -> {
        event.tileEntitySign.signText[0] = this.lineOne.getValue();
        event.tileEntitySign.signText[1] = this.lineTwo.getValue();
        event.tileEntitySign.signText[2] = this.lineThree.getValue();
        event.tileEntitySign.signText[3] = this.lineFour.getValue();

        event.tileEntitySign.onInventoryChanged();

        this.sendPacket(new Packet130UpdateSign(event.tileEntitySign.xCoord, event.tileEntitySign.yCoord, event.tileEntitySign.zCoord, event.tileEntitySign.signText));

        event.cancel();
    }));
}
