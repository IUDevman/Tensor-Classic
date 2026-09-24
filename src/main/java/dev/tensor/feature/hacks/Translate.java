package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.ChatSentEvent;
import dev.tensor.feature.managers.DeepLManager;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.StringSetting;

/**
 * @author Hoosiers
 * @since 09-23-2026
 */

@Hack.Info(name = "Translate", category = Category.Misc, description = "Translate sent chat messages with DeepL.")
public final class Translate extends Hack {

    public final StringSetting deepL_API_Code = new StringSetting("DeepL API Code", "Input your DeepL API Code here!", "Set the API code for Translate to use.") {

        @Override
        public void setValue(String VALUE) {
            super.setValue(VALUE);

            if (this.failsNullCheck() && !(this.getMinecraft() != null && this.getMinecraft().currentScreen instanceof TensorGUI)) {
                return;
            }

            DeepLManager deepLManager = this.getDeepLManager();

            if (deepLManager != null) {
                deepLManager.setAPICode(this.getValue());
            }
        }
    };
    public final EnumSetting language = new EnumSetting("Language", DeepLManager.Language.Spanish, new String[]{
            "Select what language you want your messages translated to."
    });

    @SuppressWarnings("unused")
    private final EventAction<ChatSentEvent> chatSentEventAction = this.addHackEventAction(new EventAction<>(ChatSentEvent.class, EventPriority.Highest, event -> {
        if (event.ARGS == null || event.ARGS.isEmpty() || event.ARGS.equalsIgnoreCase(" ") || event.ARGS.startsWith("/")) {
            return;
        }

        if (event.ARGS.startsWith("®")) {
            event.ARGS = event.ARGS.substring(1);
            return;
        }

        Commands commands = this.getHackManager().getHack(Commands.class);

        if (commands != null && commands.isEnabled() && event.ARGS.startsWith(commands.prefix.getValue())) {
            return;
        }

        this.getDeepLManager().addMessageToQueue(event.ARGS, (DeepLManager.Language) this.language.getValue());

        event.cancel();
    }));
}
