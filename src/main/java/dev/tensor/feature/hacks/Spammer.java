package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.ChatSentEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.helpers.SpammerList;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.GeneralButtonSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import dev.tensor.misc.imp.setting.settings.SpammerListSetting;
import net.minecraft.src.Packet3Chat;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 07-04-2026
 */

@Hack.Info(name = "Spammer", category = Category.Misc, description = "Spams bullshit from a file into chat.")
public final class Spammer extends Hack {

    public final GeneralButtonSetting refresh = new GeneralButtonSetting("Refresh File List", GeneralButtonSetting.ButtonType.CLICK_ACTION, "Refresh the loaded files from the '.minecraft/Tensor Classic/Spammer/' folder. Use this if you added/removed files with the game loaded.") {

        @Override
        public void onChange() {
            Spammer spammer = this.getHackManager().getHack(Spammer.class);

            if (spammer != null) {
                SpammerListSetting list = spammer.spammerList;

                if (list != null) {
                    list.getValue().resetSpammer(this.getConfigManager().getConfigPath());
                }
            }
        }
    };
    public final SpammerListSetting spammerList = new SpammerListSetting("File List", new SpammerList(this.getConfigManager().getConfigPath()), "List of files in '.minecraft/Tensor Classic/Spammer/' folder. Reload the list after adding files with the 'Refresh File List' button.") {

        @Override
        public void onChange() {
            if (this.failsNullCheck()) {
                return;
            }

            Spammer spammer = this.getHackManager().getHack(Spammer.class);

            if (spammer != null && spammer.isEnabled()) {
                spammer.resetSpammer();
            }
        }
    };
    public final BooleanSetting greenText = new BooleanSetting("GreenText", true, "Enable to add a '>' to the start of spammer messages, which makes the text green on most servers.");
    public final IntegerSetting tickDelay = new IntegerSetting("Tick Delay", 60, 1, 200, "Ticks", "The time between sending messages. Too low of a delay may cause you to be kicked for spamming.");

    private int delay = 0;
    private int index = 0;

    @Override
    public void onEnable(boolean isNull) {
        this.resetSpammer();
    }

    @Override
    public void onClientTick() {
        if (!this.isMultiplayer()) {
            return;
        }

        if (this.delay < this.tickDelay.getValue()) {
            this.delay++;
            return;

        } else {
            this.delay = 0;
        }

        ArrayList<String> lines = this.spammerList.getValue().getSpammerLines();

        if (lines.isEmpty()) {
            return;
        }

        if (this.index > lines.size() - 1) {
            this.index = 0;
        }

        String message = lines.get(this.index);
        this.index += 1;

        if (this.greenText.getValue()) {
            message = ">" + message;
        }

        if (message.length() >= 100) {
            message = message.substring(0, 99);
        }

        this.sendPacket(new Packet3Chat(message));
    }

    @SuppressWarnings("unused")
    private final EventAction<ChatSentEvent> chatSentEventAction = new EventAction<>(ChatSentEvent.class, EventPriority.Lowest, event -> {
        if (event.isCancelled()) {
            return;
        }

        this.delay = this.tickDelay.getValue();
    });

    public void resetSpammer() {
        this.delay = this.tickDelay.getValue();
        this.index = 0;
    }
}
